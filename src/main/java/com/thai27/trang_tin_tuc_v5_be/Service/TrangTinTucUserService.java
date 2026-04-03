package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.ChangeModerRoleRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserChangePasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserResetPasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserValidateSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.LoginResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.UserResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.Role;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Exception.BadRequestException;
import com.thai27.trang_tin_tuc_v5_be.Exception.ConflictException;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Exception.SignUpCodeExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Exception.TokenExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Repository.RoleRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.UserSignupRequestRepo;
import com.thai27.trang_tin_tuc_v5_be.Security.JWTUtil;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import com.thai27.trang_tin_tuc_v5_be.Util.GenerateRandomString;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrangTinTucUserService {

    private final JWTUtil jwtUtil;
    private final UserDetailServiceImplement userDetailService;
    private final PasswordEncoder encoder;
    private final TrangTinTucUserRepo trangTinTucUserRepo;
    private final UserSignupRequestRepo userSignupRequestRepo;
    private final RoleRepo roleRepo;
    private final GenerateRandomString randomString;
    private final SendEmailService sendEmailService;

    public ResponseEntity<ApiResponse<LoginResponse>> login(TrangTinTucUser userData) throws Exception {
        UserDetails userDetail = userDetailService.loadUserByUsername(userData.getUsername());
        if (!encoder.matches(userData.getPassword(), userDetail.getPassword())) {
            throw new BadCredentialsException("Mat khau khong chinh xac");
        }

        Authentication authen =
                new UsernamePasswordAuthenticationToken(userDetail.getUsername(), null, userDetail.getAuthorities());
        String jwtToken = jwtUtil.generate(authen);
        return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Dang nhap thanh cong")
                .data(new LoginResponse(jwtToken))
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> userSignup(UserValidateSignupRequest request) {
        UserSignupRequest userSignupRequest = userSignupRequestRepo.findByRequestCode(request.getValidateCode())
                .orElseThrow(() -> new ResourceNotFoundException("Ma xac thuc " + request.getValidateCode() + " khong ton tai trong he thong"));

        if (!userSignupRequestRepo.getCodeByEmail(request.getEmail()).equals(request.getValidateCode())) {
            throw new SignUpCodeExpiredException("Ma xac thuc khong khop vui long kiem tra lai email");
        }

        TrangTinTucUser userData = new TrangTinTucUser();
        userData.setUsername(userSignupRequest.getUsername());
        userData.setPassword(encoder.encode(userSignupRequest.getPassword()));
        userData.setEmail(userSignupRequest.getEmail());
        userData.setRoles(roleRepo.findListByRoleName(Constant.ROLE_USER));
        trangTinTucUserRepo.save(userData);
        userSignupRequestRepo.deleteAllByEmail(userSignupRequest.getEmail());
        String sendSignupSuccess = sendEmailService.sendSignupSuccess(userSignupRequest.getEmail(), userSignupRequest.getUsername());
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message(sendSignupSuccess)
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Claims>> getClaimsFromToken(String token) {
        try {
            return ResponseEntity.ok(ApiResponse.<Claims>builder()
                    .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                    .message("Lay thong tin token thanh cong")
                    .data(jwtUtil.getClaims(token))
                    .build());
        } catch (Exception e) {
            throw new TokenExpiredException("Phien dang nhap het han vui long dang nhap lai");
        }
    }

    public ResponseEntity<ApiResponse<Object>> createSignupRequest(UserSignupRequest userSignupRequest) {
        if (trangTinTucUserRepo.findByUsername(userSignupRequest.getUsername()).isPresent()) {
            throw new ConflictException("Ten nguoi dung " + userSignupRequest.getUsername() + " da ton tai");
        }
        if (trangTinTucUserRepo.findByEmail(userSignupRequest.getEmail()).isPresent()) {
            throw new ConflictException("Email " + userSignupRequest.getEmail() + " da ton tai");
        }

        UserSignupRequest createUser = new UserSignupRequest();
        createUser.setUsername(userSignupRequest.getUsername());
        createUser.setPassword(userSignupRequest.getPassword());
        createUser.setEmail(userSignupRequest.getEmail());
        String requestCode = randomString.generateRandomCode(5);
        createUser.setRequestCode(requestCode);
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        createUser.setRequestTime(formattedDate);
        userSignupRequestRepo.save(createUser);
        String sendSignup = sendEmailService.sendSignupRequest(userSignupRequest.getEmail(), userSignupRequest.getUsername(), requestCode);
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message(sendSignup)
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> resetPassword(UserResetPasswordRequest userRequest) {
        TrangTinTucUser resetUser = trangTinTucUserRepo.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Ten nguoi dung khong ton tai trong he thong: " + userRequest.getUsername()));
        if (!userRequest.getEmail().equals(resetUser.getEmail())) {
            throw new BadRequestException("Email nguoi dung nhap khong khop voi email da dang ky");
        }

        String newPassword = randomString.generateRandomCode(8);
        String newEncodedPassword = encoder.encode(newPassword);
        resetUser.setPassword(newEncodedPassword);
        trangTinTucUserRepo.save(resetUser);
        String sendNewPassword = sendEmailService.sendNewPassword(resetUser.getEmail(), resetUser.getUsername(), newPassword);
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message(sendNewPassword)
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> changePassword(UserChangePasswordRequest userRequest) throws Exception {
        String username = jwtUtil.getUsername(userRequest.getToken());
        TrangTinTucUser changePassUser = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Ten nguoi dung khong ton tai trong he thong: " + username));
        if (!encoder.matches(userRequest.getOldPassword(), changePassUser.getPassword())) {
            throw new BadRequestException("Mat khau nguoi dung nhap khong trung voi mat khau tren he thong");
        }

        changePassUser.setPassword(encoder.encode(userRequest.getNewPassword()));
        trangTinTucUserRepo.save(changePassUser);
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Doi mat khau thanh cong vui long dang nhap lai")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUserByUsername(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TrangTinTucUser> userPage = trangTinTucUserRepo.findByUsernameContainingIgnoreCase(keyword.trim(), pageable);

        Page<UserResponse> userPageResponse = userPage.map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getRoleName).toList()
        ));
        return ResponseEntity.ok(ApiResponse.<Page<UserResponse>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lay du lieu thanh cong")
                .data(userPageResponse)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> changeModerRole(ChangeModerRoleRequest request) {
        Long userId = request.getUserId();
        if (userId == null) {
            throw new BadRequestException("ID nguoi dung khong duoc de trong");
        }
        TrangTinTucUser user = trangTinTucUserRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nguoi dung khong ton tai trong he thong"));
        if (request.isSetModer()) {
            user.setRoles(roleRepo.findListByRoleName(Constant.ROLE_MODER));
        } else {
            List<Role> roles = user.getRoles();
            roles.removeIf(r -> r.getRoleName().equals(Constant.ROLE_MODER));
            user.setRoles(roles);
        }
        trangTinTucUserRepo.save(user);
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Doi role thanh cong")
                .data(null)
                .build());
    }

}
