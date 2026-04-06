package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.ChangeModerRoleRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserChangePasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserResetPasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserValidateSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.LoginResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.UserResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Exception.*;
import com.thai27.trang_tin_tuc_v5_be.Service.TrangTinTucUserService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class TrangTinTucUserController {

    private final TrangTinTucUserService userService;

    @PostMapping("/permit/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody TrangTinTucUser user
    ) throws Exception {
        return userService.login(user);
    }

    @PostMapping("/permit/signup-request")
    public ResponseEntity<ApiResponse<Object>> signupRequest(
            @RequestBody UserSignupRequest request
    ) throws UserInfoAlreadyExistException {
        return userService.createSignupRequest(request);
    }

    @PostMapping("/permit/signup")
    public ResponseEntity<ApiResponse<Object>> validateSignup(
            @RequestBody UserValidateSignupRequest request
    ) throws ResourceNotFoundException, SignUpCodeExpiredException {
        return userService.userSignup(request);
    }

    @PostMapping("/permit/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(
            @RequestBody UserResetPasswordRequest request
    ) throws ResourceNotFoundException {
        return userService.resetPassword(request);
    }

    @PostMapping("/auth/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @RequestBody UserChangePasswordRequest request
    ) throws Exception {
        return userService.changePassword(request);
    }

    @PostMapping("/auth/change-role-moder")
    public ResponseEntity<ApiResponse<Object>> changeRoleModer(
            @RequestBody ChangeModerRoleRequest request
    ) throws ResourceNotFoundException {
        return userService.changeModerRole(request);
    }

    @GetMapping("/auth/search-users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUsers(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return userService.searchUserByUsername(search, pageNum, pageSize);
    }

    @GetMapping("/permit/get-claims-from-token")
    public ResponseEntity<ApiResponse<Claims>> getTokenInfo(
            @RequestParam String token
    ) throws TokenExpiredException {
        return userService.getClaimsFromToken(token);
    }
}

