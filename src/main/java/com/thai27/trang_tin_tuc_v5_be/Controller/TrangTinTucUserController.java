package com.thai27.trang_tin_tuc_v5_be.Controller;



import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Exception.TokenExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Exception.UsernameAlreadyExistException;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import com.thai27.trang_tin_tuc_v5_be.Response.UserListResponse;
import com.thai27.trang_tin_tuc_v5_be.Security.JWTAuthenProvider;
import com.thai27.trang_tin_tuc_v5_be.Security.JWTUltil;
import com.thai27.trang_tin_tuc_v5_be.ServiceImplement.TrangTinTucUserServiceImplement;
import com.thai27.trang_tin_tuc_v5_be.ServiceImplement.UserDetailServiceImplement;
import com.thai27.trang_tin_tuc_v5_be.ServiceImplement.UserSignupRequestSrvImp;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String login(@RequestBody TrangTinTucUser userData) {
        return trangTinTucUserServiceImplement.login(userData);
    }

    @PostMapping("/permit/userSignup")
    public String userSignup(@RequestParam String validateCode, @RequestParam String email) throws ResourceNotFoundException {
        return trangTinTucUserServiceImplement.userSignup(validateCode, email);
    }

    @PostMapping("/permit/userSignupRequest")
    public String userSignupRequest(@RequestBody UserSignupRequest userData) throws UsernameAlreadyExistException {
        return userSignupRequestSrvImp.createSignupRequest(userData);
    }

    @PostMapping("/permit/getUsernameByToken")
    public String getUsernameByToken(@RequestParam String token) {
        return trangTinTucUserServiceImplement.getUsernameByToken(token);
    }
    @PostMapping("/permit/tokenIsExpired")
    public Boolean tokenIsExpired(@RequestParam String token) throws TokenExpiredException  {
        return trangTinTucUserServiceImplement.checkTokenExpired(token);
    }
    @GetMapping("/permit/resetPassword")
    public String resetPassword (@RequestParam String email,@RequestParam String username)throws ResourceNotFoundException {
        return trangTinTucUserServiceImplement.resetPassword(username, email);
    }
    @GetMapping("/auth/changePassword")
    public String changePassword (@RequestParam String token,@RequestParam String oldPassword, @RequestParam String newPassword)throws ResourceNotFoundException {
        return trangTinTucUserServiceImplement.changePassword(token, oldPassword, newPassword);
    }

    @PostMapping("/permit/getRoleByUsername")
    public List<String> getRoleByUsername(@RequestParam String username) {
        return trangTinTucUserServiceImplement.getRoleByUsername(username);
    }

    @GetMapping("/auth/getAllUser")
    public ResponseEntity<UserListResponse> getAllUser (@RequestParam Integer pageNum, @RequestParam Integer pageSize ) {
        UserListResponse userListData = trangTinTucUserServiceImplement.getAllUser(pageNum, pageSize);
        return ResponseEntity.ok().body(userListData);
    }

    @GetMapping("/auth/searchAllUser")
    public UserListResponse searchAllUser (@RequestParam String search ,@RequestParam Integer pageNum, @RequestParam Integer pageSize ) {
        return trangTinTucUserServiceImplement.findAllByUsername(search,pageNum,pageSize);
    }

    @GetMapping("/auth/getUserById/{userId}")
    public TrangTinTucUser getUserById(@PathVariable Long userId) throws ResourceNotFoundException {
        return trangTinTucUserServiceImplement.getUserById(userId);
    }

    @PutMapping("/auth/setModerRole/{userId}")
    public void setModerRole (@PathVariable Long userId) throws ResourceNotFoundException {
        trangTinTucUserServiceImplement.setModerRole(userId);
    }

    @DeleteMapping("/auth/deleteUserById/{userId}")
    public void deleteUserById (@PathVariable Long userId) {
        trangTinTucUserRepo.deleteUserRole(userId);
        trangTinTucUserRepo.deleteUser(userId);
    }

    @DeleteMapping("/auth/deleteUserModer/{userId}")
    public void deleteUserModer (@PathVariable Long userId) {
        trangTinTucUserRepo.deleteUserRoleModer(userId);
    }

}
