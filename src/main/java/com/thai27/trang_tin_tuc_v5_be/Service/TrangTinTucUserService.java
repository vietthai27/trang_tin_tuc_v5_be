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
            throw new BadCredentialsException("Mật khẩu không chính xác");
        }

        Authentication authen =
                new UsernamePasswordAuthenticationToken(userDetail.getUsername(), null, userDetail.getAuthorities());
        String jwtToken = jwtUtil.generate(authen);
        return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Đăng nhập thành công")
                .data(new LoginResponse(jwtToken))
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> userSignup(UserValidateSignupRequest request) {
        UserSignupRequest userSignupRequest = userSignupRequestRepo.findByRequestCode(request.getValidateCode())
                .orElseThrow(() -> new ResourceNotFoundException("Mã xác thực " + request.getValidateCode() + " không tồn tại trong hệ thống"));

        if (!userSignupRequestRepo.getCodeByEmail(request.getEmail()).equals(request.getValidateCode())) {
            throw new SignUpCodeExpiredException("Mã xác thực không khớp, vui lòng kiểm tra lại email");
        }

        TrangTinTucUser userData = new TrangTinTucUser();
        userData.setUsername(userSignupRequest.getUsername());
        userData.setPassword(encoder.encode(userSignupRequest.getPassword()));
        userData.setEmail(userSignupRequest.getEmail());
        userData.setRoles(roleRepo.findListByRoleName(Constant.ROLE_USER));
        trangTinTucUserRepo.save(userData);

        userSignupRequestRepo.deleteAllByEmail(userSignupRequest.getEmail());

        String sendSignupSuccess = sendEmailService.sendSignupSuccess(
                userSignupRequest.getEmail(),
                userSignupRequest.getUsername()
        );

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
                    .message("Lấy thông tin token thành công")
                    .data(jwtUtil.getClaims(token))
                    .build());
        } catch (Exception e) {
            throw new TokenExpiredException("Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại");
        }
    }

    public ResponseEntity<ApiResponse<Object>> createSignupRequest(UserSignupRequest userSignupRequest) {
        if (trangTinTucUserRepo.findByUsername(userSignupRequest.getUsername()).isPresent()) {
            throw new ConflictException("Tên người dùng " + userSignupRequest.getUsername() + " đã tồn tại");
        }
        if (trangTinTucUserRepo.findByEmail(userSignupRequest.getEmail()).isPresent()) {
            throw new ConflictException("Email " + userSignupRequest.getEmail() + " đã tồn tại");
        }

        UserSignupRequest createUser = new UserSignupRequest();
        createUser.setUsername(userSignupRequest.getUsername());
        createUser.setPassword(userSignupRequest.getPassword());
        createUser.setEmail(userSignupRequest.getEmail());

        String requestCode = randomString.generateRandomCode(5);
        createUser.setRequestCode(requestCode);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        createUser.setRequestTime(now.format(format));

        userSignupRequestRepo.save(createUser);

        String sendSignup = sendEmailService.sendSignupRequest(
                userSignupRequest.getEmail(),
                userSignupRequest.getUsername(),
                requestCode
        );

        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message(sendSignup)
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> resetPassword(UserResetPasswordRequest userRequest) {
        TrangTinTucUser resetUser = trangTinTucUserRepo.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Tên người dùng không tồn tại trong hệ thống: " + userRequest.getUsername()));

        if (!userRequest.getEmail().equals(resetUser.getEmail())) {
            throw new BadRequestException("Email nhập vào không khớp với email đã đăng ký");
        }

        String newPassword = randomString.generateRandomCode(8);
        String newEncodedPassword = encoder.encode(newPassword);

        resetUser.setPassword(newEncodedPassword);
        trangTinTucUserRepo.save(resetUser);

        String sendNewPassword = sendEmailService.sendNewPassword(
                resetUser.getEmail(),
                resetUser.getUsername(),
                newPassword
        );

        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message(sendNewPassword)
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> changePassword(UserChangePasswordRequest userRequest) throws Exception {
        String username = jwtUtil.getUsername(userRequest.getToken());

        TrangTinTucUser changePassUser = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Tên người dùng không tồn tại trong hệ thống: " + username));

        if (!encoder.matches(userRequest.getOldPassword(), changePassUser.getPassword())) {
            throw new BadRequestException("Mật khẩu cũ không chính xác");
        }

        changePassUser.setPassword(encoder.encode(userRequest.getNewPassword()));
        trangTinTucUserRepo.save(changePassUser);

        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Đổi mật khẩu thành công, vui lòng đăng nhập lại")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUserByUsername(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<TrangTinTucUser> userPage =
                trangTinTucUserRepo.findByUsernameContainingIgnoreCase(keyword.trim(), pageable);

        Page<UserResponse> userPageResponse = userPage.map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getRoleName).toList()
        ));

        return ResponseEntity.ok(ApiResponse.<Page<UserResponse>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(userPageResponse)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> changeModerRole(ChangeModerRoleRequest request) {
        Long userId = request.getUserId();

        if (userId == null) {
            throw new BadRequestException("ID người dùng không được để trống");
        }

        TrangTinTucUser user = trangTinTucUserRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại trong hệ thống"));

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
                .message("Đổi vai trò thành công")
                .data(null)
                .build());
    }
}