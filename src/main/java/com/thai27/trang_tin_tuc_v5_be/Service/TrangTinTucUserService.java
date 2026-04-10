package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.ChangeModerRoleRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.LoginRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserChangePasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserResetPasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserValidateSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.LoginResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.UserResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.Role;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Exception.BadRequestException;
import com.thai27.trang_tin_tuc_v5_be.Exception.ConflictException;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Exception.SignUpCodeExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Exception.TokenExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Repository.RoleRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.UserSignupRequestRepo;
import com.thai27.trang_tin_tuc_v5_be.Security.JWTUtil;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import com.thai27.trang_tin_tuc_v5_be.Util.GenerateRandomString;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public LoginResponse login(LoginRequest userData) throws Exception {
        UserDetails userDetail = userDetailService.loadUserByUsername(userData.getUsername());
        if (!encoder.matches(userData.getPassword(), userDetail.getPassword())) {
            throw new BadCredentialsException("Máº­t kháº©u khÃ´ng chÃ­nh xÃ¡c");
        }

        Authentication authen = new UsernamePasswordAuthenticationToken(
                userDetail.getUsername(),
                null,
                userDetail.getAuthorities()
        );
        String jwtToken = jwtUtil.generate(authen);
        return new LoginResponse(jwtToken);
    }

    public void userSignup(UserValidateSignupRequest request) {
        com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest userSignupRequest = userSignupRequestRepo.findByRequestCode(request.getValidateCode())
                .orElseThrow(() -> new ResourceNotFoundException("MÃ£ xÃ¡c thá»±c " + request.getValidateCode() + " khÃ´ng tá»“n táº¡i trong há»‡ thá»‘ng"));

        if (!userSignupRequestRepo.getCodeByEmail(request.getEmail()).equals(request.getValidateCode())) {
            throw new SignUpCodeExpiredException("MÃ£ xÃ¡c thá»±c khÃ´ng khá»›p, vui lÃ²ng kiá»ƒm tra láº¡i email");
        }

        TrangTinTucUser userData = new TrangTinTucUser();
        userData.setUsername(userSignupRequest.getUsername());
        userData.setPassword(encoder.encode(userSignupRequest.getPassword()));
        userData.setEmail(userSignupRequest.getEmail());
        userData.setRoles(roleRepo.findListByRoleName(Constant.ROLE_USER));
        trangTinTucUserRepo.save(userData);

        userSignupRequestRepo.deleteAllByEmail(userSignupRequest.getEmail());
        sendEmailService.sendSignupSuccess(userSignupRequest.getEmail(), userSignupRequest.getUsername());
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return jwtUtil.getClaims(token);
        } catch (Exception e) {
            throw new TokenExpiredException("PhiÃªn Ä‘Äƒng nháº­p Ä‘Ã£ háº¿t háº¡n, vui lÃ²ng Ä‘Äƒng nháº­p láº¡i");
        }
    }

    public void createSignupRequest(UserSignupRequest userSignupRequest) {
        if (trangTinTucUserRepo.findByUsername(userSignupRequest.getUsername()).isPresent()) {
            throw new ConflictException("TÃªn ngÆ°á»i dÃ¹ng " + userSignupRequest.getUsername() + " Ä‘Ã£ tá»“n táº¡i");
        }
        if (trangTinTucUserRepo.findByEmail(userSignupRequest.getEmail()).isPresent()) {
            throw new ConflictException("Email " + userSignupRequest.getEmail() + " Ä‘Ã£ tá»“n táº¡i");
        }

        com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest createUser = new com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest();
        createUser.setUsername(userSignupRequest.getUsername());
        createUser.setPassword(userSignupRequest.getPassword());
        createUser.setEmail(userSignupRequest.getEmail());
        String requestCode = randomString.generateRandomCode(5);
        createUser.setRequestCode(requestCode);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        createUser.setRequestTime(now.format(format));
        userSignupRequestRepo.save(createUser);

        sendEmailService.sendSignupRequest(
                userSignupRequest.getEmail(),
                userSignupRequest.getUsername(),
                requestCode
        );
    }

    public void resetPassword(UserResetPasswordRequest userRequest) {
        TrangTinTucUser resetUser = trangTinTucUserRepo.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("TÃªn ngÆ°á»i dÃ¹ng khÃ´ng tá»“n táº¡i trong há»‡ thá»‘ng: " + userRequest.getUsername()));

        if (!userRequest.getEmail().equals(resetUser.getEmail())) {
            throw new BadRequestException("Email nháº­p vÃ o khÃ´ng khá»›p vá»›i email Ä‘Ã£ Ä‘Äƒng kÃ½");
        }

        String newPassword = randomString.generateRandomCode(8);
        resetUser.setPassword(encoder.encode(newPassword));
        trangTinTucUserRepo.save(resetUser);

        sendEmailService.sendNewPassword(resetUser.getEmail(), resetUser.getUsername(), newPassword);
    }

    public void changePassword(UserChangePasswordRequest userRequest) throws Exception {
        String username = jwtUtil.getUsername(userRequest.getToken());

        TrangTinTucUser changePassUser = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("TÃªn ngÆ°á»i dÃ¹ng khÃ´ng tá»“n táº¡i trong há»‡ thá»‘ng: " + username));

        if (!encoder.matches(userRequest.getOldPassword(), changePassUser.getPassword())) {
            throw new BadRequestException("Máº­t kháº©u cÅ© khÃ´ng chÃ­nh xÃ¡c");
        }

        changePassUser.setPassword(encoder.encode(userRequest.getNewPassword()));
        trangTinTucUserRepo.save(changePassUser);
    }

    public Page<UserResponse> searchUserByUsername(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TrangTinTucUser> userPage = trangTinTucUserRepo.findByUsernameContainingIgnoreCase(keyword.trim(), pageable);
        return userPage.map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getRoleName).toList()
        ));
    }

    public void changeModerRole(ChangeModerRoleRequest request) {
        Long userId = request.getUserId();
        if (userId == null) {
            throw new BadRequestException("ID ngÆ°á»i dÃ¹ng khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng");
        }

        TrangTinTucUser user = trangTinTucUserRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("NgÆ°á»i dÃ¹ng khÃ´ng tá»“n táº¡i trong há»‡ thá»‘ng"));

        if (request.isSetModer()) {
            user.setRoles(roleRepo.findListByRoleName(Constant.ROLE_MODER));
        } else {
            List<Role> roles = user.getRoles();
            roles.removeIf(r -> r.getRoleName().equals(Constant.ROLE_MODER));
            user.setRoles(roles);
        }

        trangTinTucUserRepo.save(user);
    }
}
