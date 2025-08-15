package com.thai27.trang_tin_tuc_v5_be.Controller;


import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Exception.TokenExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Exception.UserInfoAlreadyExistException;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import com.thai27.trang_tin_tuc_v5_be.Request.UserChangePasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.Request.UserResetPasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.Request.UserValidateSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Response.UserListResponse;
import com.thai27.trang_tin_tuc_v5_be.Security.JWTAuthenProvider;
import com.thai27.trang_tin_tuc_v5_be.Security.JWTUltil;
import com.thai27.trang_tin_tuc_v5_be.ServiceImplement.TrangTinTucUserServiceImplement;
import com.thai27.trang_tin_tuc_v5_be.ServiceImplement.UserDetailServiceImplement;
import com.thai27.trang_tin_tuc_v5_be.ServiceImplement.UserSignupRequestSrvImp;
import io.jsonwebtoken.Claims;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class TrangTinTucUserController {

    @Autowired
    JWTUltil jwtUtil;

    @Autowired
    JWTAuthenProvider jwtAuth;

    @Autowired
    UserDetailServiceImplement userSrvImp;

    @Autowired
    TrangTinTucUserServiceImplement trangTinTucUserServiceImplement;

    @Autowired
    TrangTinTucUserRepo trangTinTucUserRepo;

    @Autowired
    UserDetailServiceImplement userDetailSrvImp;

    @Autowired
    UserSignupRequestSrvImp userSignupRequestSrvImp;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/permit/login")
    public ResponseEntity<String> login(@RequestBody TrangTinTucUser userData) {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.login(userData));
    }

    @PostMapping("/permit/userSignup")
    public ResponseEntity<String> userSignup(@RequestBody UserValidateSignupRequest request) throws ResourceNotFoundException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.userSignup(request));
    }

    @PostMapping("/permit/userSignupRequest")
    public ResponseEntity<String> userSignupRequest(@RequestBody UserSignupRequest userData) throws UserInfoAlreadyExistException {
        return ResponseEntity.ok(userSignupRequestSrvImp.createSignupRequest(userData));
    }

    @PostMapping("/permit/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody UserResetPasswordRequest userRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.resetPassword(userRequest));
    }

    @GetMapping("/permit/getClaimsFromToken")
    public ResponseEntity<Claims> getClaimsFromToken(@RequestParam String token) throws TokenExpiredException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.getClaimsFromToken(token));
    }

    @PostMapping("/auth/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody UserChangePasswordRequest userRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.changePassword(userRequest));
    }

    @GetMapping("/auth/getAllUser")
    public ResponseEntity<UserListResponse> getAllUser(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.getAllUser(pageNum, pageSize));
    }

    @GetMapping("/auth/searchUser")
    public ResponseEntity<UserListResponse> searchUser(@RequestParam String search, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.findAllByUsername(search, pageNum, pageSize));
    }

    @GetMapping("/auth/getUserById/{userId}")
    public TrangTinTucUser getUserById(@PathVariable Long userId) throws ResourceNotFoundException {
        return trangTinTucUserServiceImplement.getUserById(userId);
    }

    @PutMapping("/auth/setUserModerRole/{userId}")
    public ResponseEntity<String> setUserModerRole(@PathVariable Long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.setUserModerRole(userId));
    }

    @PutMapping("/auth/unsetUserModerRole/{userId}")
    public ResponseEntity<String> unsetUserModerRole(@PathVariable Long userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.unsetUserModerRoles(userId));
    }

    @DeleteMapping("/auth/deleteUserById/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(trangTinTucUserServiceImplement.deleteUserById(userId));
    }


}
