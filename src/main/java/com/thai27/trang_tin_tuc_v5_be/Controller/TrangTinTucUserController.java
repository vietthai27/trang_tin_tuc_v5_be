package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Exception.SignUpCodeExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Exception.TokenExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Exception.UserInfoAlreadyExistException;
import com.thai27.trang_tin_tuc_v5_be.Request.UserChangePasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.Request.UserResetPasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.Request.UserValidateSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Response.UserListResponse;
import com.thai27.trang_tin_tuc_v5_be.ServiceImplement.TrangTinTucUserServiceImplement;
import com.thai27.trang_tin_tuc_v5_be.ServiceImplement.UserSignupRequestSrvImp;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class TrangTinTucUserController {

    @Autowired
    TrangTinTucUserServiceImplement trangTinTucUserServiceImplement;

    @Autowired
    UserSignupRequestSrvImp userSignupRequestSrvImp;

    @PostMapping("/permit/login")
    public ResponseEntity<String> login(@RequestBody TrangTinTucUser userData) {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.login(userData));
    }

    @PostMapping("/permit/user-signup-request")
    public ResponseEntity<String> userSignupRequest(@RequestBody UserSignupRequest userData) throws UserInfoAlreadyExistException {
        return ResponseEntity.ok(userSignupRequestSrvImp.createSignupRequest(userData));
    }

    @PostMapping("/permit/user-signup")
    public ResponseEntity<String> userSignup(@RequestBody UserValidateSignupRequest request) throws ResourceNotFoundException, SignUpCodeExpiredException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.userSignup(request));
    }

    @PostMapping("/permit/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody UserResetPasswordRequest userRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.resetPassword(userRequest));
    }

    @PostMapping("/auth/change-password")
    public ResponseEntity<String> changePassword(@RequestBody UserChangePasswordRequest userRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.changePassword(userRequest));
    }

    @GetMapping("/permit/get-claims-from-token")
    public ResponseEntity<Claims> getClaimsFromToken(@RequestParam String token) throws TokenExpiredException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.getClaimsFromToken(token));
    }

    @GetMapping("/auth/get-all-user")
    public ResponseEntity<UserListResponse> getAllUser(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.getAllUser(pageNum, pageSize));
    }

    @GetMapping("/auth/search-user")
    public ResponseEntity<UserListResponse> searchUser(@RequestParam String search, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.findAllByUsername(search, pageNum, pageSize));
    }

    @GetMapping("/auth/get-user-by-id/{userId}")
    public TrangTinTucUser getUserById(@PathVariable Long userId) throws ResourceNotFoundException {
        return trangTinTucUserServiceImplement.getUserById(userId);
    }

    @PutMapping("/auth/set-moder/{userId}")
    public ResponseEntity<String> setUserModerRole(@PathVariable Long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.setUserModerRole(userId));
    }

    @PutMapping("/auth/unset-moder/{userId}")
    public ResponseEntity<String> unsetUserModerRole(@PathVariable Long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.unsetUserModerRoles(userId));
    }

    @DeleteMapping("/auth/delete-user-by-id/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.deleteUserById(userId));
    }


}
