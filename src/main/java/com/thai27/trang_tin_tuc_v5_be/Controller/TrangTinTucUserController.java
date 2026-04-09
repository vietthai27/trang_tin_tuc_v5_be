package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.ChangeModerRoleRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.LoginRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserChangePasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserResetPasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserSignupRequestDTO;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.UserValidateSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.LoginResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.UserResponse;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Exception.SignUpCodeExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Exception.TokenExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Exception.UserInfoAlreadyExistException;
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
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest user) throws Exception {
        return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()
                .message("ÄÄƒng nháº­p thÃ nh cÃ´ng")
                .data(userService.login(user))
                .build());
    }

    @PostMapping("/permit/signup-request")
    public ResponseEntity<ApiResponse<Object>> signupRequest(@RequestBody UserSignupRequestDTO request) throws UserInfoAlreadyExistException {
        userService.createSignupRequest(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Gá»­i yÃªu cáº§u Ä‘Äƒng kÃ½ thÃ nh cÃ´ng")
                .data(null)
                .build());
    }

    @PostMapping("/permit/signup")
    public ResponseEntity<ApiResponse<Object>> validateSignup(@RequestBody UserValidateSignupRequest request)
            throws ResourceNotFoundException, SignUpCodeExpiredException {
        userService.userSignup(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("ÄÄƒng kÃ½ tÃ i khoáº£n thÃ nh cÃ´ng")
                .data(null)
                .build());
    }

    @PostMapping("/permit/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@RequestBody UserResetPasswordRequest request)
            throws ResourceNotFoundException {
        userService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Äáº·t láº¡i máº­t kháº©u thÃ nh cÃ´ng")
                .data(null)
                .build());
    }

    @PostMapping("/auth/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(@RequestBody UserChangePasswordRequest request) throws Exception {
        userService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Äá»•i máº­t kháº©u thÃ nh cÃ´ng, vui lÃ²ng Ä‘Äƒng nháº­p láº¡i")
                .data(null)
                .build());
    }

    @PostMapping("/auth/change-role-moder")
    public ResponseEntity<ApiResponse<Object>> changeRoleModer(@RequestBody ChangeModerRoleRequest request) throws ResourceNotFoundException {
        userService.changeModerRole(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Äá»•i vai trÃ² thÃ nh cÃ´ng")
                .data(null)
                .build());
    }

    @GetMapping("/auth/search-users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUsers(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(ApiResponse.<Page<UserResponse>>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(userService.searchUserByUsername(search, pageNum, pageSize))
                .build());
    }

    @GetMapping("/permit/get-claims-from-token")
    public ResponseEntity<ApiResponse<Claims>> getTokenInfo(@RequestParam String token) throws TokenExpiredException {
        return ResponseEntity.ok(ApiResponse.<Claims>builder()
                .message("Láº¥y thÃ´ng tin token thÃ nh cÃ´ng")
                .data(userService.getClaimsFromToken(token))
                .build());
    }
}
