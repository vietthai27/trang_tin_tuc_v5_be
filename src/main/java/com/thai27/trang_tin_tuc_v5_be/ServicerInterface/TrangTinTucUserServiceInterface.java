package com.thai27.trang_tin_tuc_v5_be.ServicerInterface;


import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Exception.TokenExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Response.UserListResponse;

import java.util.List;

public interface TrangTinTucUserServiceInterface {

    String userSignup(String validateCode,String email) throws ResourceNotFoundException;

    String login(TrangTinTucUser userData);

    String getUsernameByToken(String token);

    List<String> getRoleByUsername(String username);

    UserListResponse getAllUser(Integer pageNum, Integer pageSize);

    TrangTinTucUser getUserById(Long id) throws ResourceNotFoundException;

    void setModerRole ( Long userId) throws ResourceNotFoundException;

    UserListResponse findAllByUsername(String username, Integer pageNum, Integer pageSize);
    
    String resetPassword (String username, String email) throws ResourceNotFoundException;

    String changePassword (String username, String oldPassword, String newPassword) throws ResourceNotFoundException;

    Boolean checkTokenExpired (String token) throws TokenExpiredException;

}
