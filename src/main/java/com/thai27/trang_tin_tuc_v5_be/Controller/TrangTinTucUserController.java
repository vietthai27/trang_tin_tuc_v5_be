package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserChangePasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserResetPasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserValidateSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.LoginResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Exception.*;
import com.thai27.trang_tin_tuc_v5_be.Service.TrangTinTucUserService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class TrangTinTucUserController {

    @Autowired
    private TrangTinTucUserService userService;

    /**
     * LOGIN
     * POST /api/auth/login
     */
    @PostMapping("/permit/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody TrangTinTucUser user
    ) {
        return userService.login(user);
    }

    /**
     * CREATE SIGNUP REQUEST (send mail)
     * POST /api/auth/signup-request
     */
    @PostMapping("/permit/signup-request")
    public ResponseEntity<ApiResponse<Object>> signupRequest(
            @RequestBody UserSignupRequest request
    ) throws UserInfoAlreadyExistException {
        return userService.createSignupRequest(request);
    }

    /**
     * VALIDATE SIGNUP (verify code)
     * POST /api/auth/signup-validate
     */
    @PostMapping("/permit/signup")
    public ResponseEntity<ApiResponse<Object>> validateSignup(
            @RequestBody UserValidateSignupRequest request
    ) throws ResourceNotFoundException, SignUpCodeExpiredException {
        return userService.userSignup(request);
    }

    /**
     * RESET PASSWORD (forgot password)
     * POST /api/auth/reset-password
     */
    @PostMapping("/permit/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(
            @RequestBody UserResetPasswordRequest request
    ) throws ResourceNotFoundException {
        return userService.resetPassword(request);
    }

    /**
     * CHANGE PASSWORD (logged in)
     * POST /api/auth/change-password
     */
    @PostMapping("/auth/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @RequestBody UserChangePasswordRequest request
    ) throws ResourceNotFoundException {
        return userService.changePassword(request);
    }

    /**
     * GET CLAIMS FROM TOKEN
     * POST /api/auth/token-info
     */
    @GetMapping("/permit/get-claims-from-token")
    public ResponseEntity<ApiResponse<Claims>> getTokenInfo(
            @RequestParam String token
    ) throws TokenExpiredException {
        return userService.getClaimsFromToken(token);
    }
}

