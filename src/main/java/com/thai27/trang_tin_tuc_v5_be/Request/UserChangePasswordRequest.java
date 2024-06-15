package com.thai27.trang_tin_tuc_v5_be.Request;

import lombok.Data;

@Data
public class UserChangePasswordRequest {
    private String token;
    private String oldPassword;
    private String newPassword;
}
