package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.ChangeModerRoleRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.LoginRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserChangePasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserResetPasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserValidateSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.LoginResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.UserResponse;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Exception.SignUpCodeExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Exception.TokenExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Exception.UserInfoAlreadyExistException;
import com.thai27.trang_tin_tuc_v5_be.Service.MessageService;
import com.thai27.trang_tin_tuc_v5_be.Service.TrangTinTucUserService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class TrangTinTucUserController {

    private final TrangTinTucUserService userService;

    private final MessageService messageService;

    @PostMapping("/permit/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest user) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<LoginResponse>builder()
                .message(messageService.getMessage("login.success"))
                .data(userService.login(user))
                .build());
    }

    @PostMapping("/permit/signup-request")
    public ResponseEntity<ApiResponse<Void>> signupRequest(@RequestBody UserSignupRequest request) throws UserInfoAlreadyExistException {
        userService.createSignupRequest(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message("Gửi yêu cầu đăng ký thành cng")
                .build());
    }

    @PostMapping("/permit/signup")
    public ResponseEntity<ApiResponse<Void>> validateSignup(@RequestBody UserValidateSignupRequest request)
            throws ResourceNotFoundException, SignUpCodeExpiredException {
        userService.userSignup(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message("Đăng ký thành công")
                .build());
    }

    @PostMapping("/permit/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody UserResetPasswordRequest request)
            throws ResourceNotFoundException {
        userService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message("Đặt lại mật khẩu thành")
                .build());
    }

    @PostMapping("/auth/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody UserChangePasswordRequest request) throws Exception {
        userService.changePassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message("Đổi mật khẩu thành công, vui lòng đăng nhập lại")
                .build());
    }

    @PostMapping("/auth/change-role-moder")
    public ResponseEntity<ApiResponse<Void>> changeRoleModer(@RequestBody ChangeModerRoleRequest request) throws ResourceNotFoundException {
        userService.changeModerRole(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message("Đổi role thành công")
                .build());
    }

    @GetMapping("/auth/search-users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUsers(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<UserResponse>>builder()
                .message(messageService.getMessage("get.success"))
                .data(userService.searchUserByUsername(search, pageNum, pageSize))
                .build());
    }

    @GetMapping("/permit/get-claims-from-token")
    public ResponseEntity<ApiResponse<Claims>> getTokenInfo(@RequestParam String token) throws TokenExpiredException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Claims>builder()
                .message("Lấy thông tin token thành công")
                .data(userService.getClaimsFromToken(token))
                .build());
    }
}
