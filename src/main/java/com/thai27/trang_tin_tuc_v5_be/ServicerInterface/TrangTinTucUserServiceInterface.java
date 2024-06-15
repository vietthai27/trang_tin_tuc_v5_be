package com.thai27.trang_tin_tuc_v5_be.ServicerInterface;


import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Exception.TokenExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Request.UserChangePasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.Request.UserResetPasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.Request.UserValidateSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Response.UserListResponse;
import io.jsonwebtoken.Claims;

public interface TrangTinTucUserServiceInterface {

    String userSignup(UserValidateSignupRequest userRequest) throws ResourceNotFoundException;

    String login(TrangTinTucUser userData);

    String resetPassword(UserResetPasswordRequest userRequest) throws ResourceNotFoundException;

    String changePassword(UserChangePasswordRequest userRequest) throws ResourceNotFoundException;

    Claims getClaimsFromToken(String token);

    UserListResponse getAllUser(Integer pageNum, Integer pageSize);

    TrangTinTucUser getUserById(Long id) throws ResourceNotFoundException;

    String setUserModerRole(Long userId) throws ResourceNotFoundException;

    String unsetUserModerRoles(Long userId) throws ResourceNotFoundException;

    UserListResponse findAllByUsername(String username, Integer pageNum, Integer pageSize);

    String deleteUserById(Long userId);

}
