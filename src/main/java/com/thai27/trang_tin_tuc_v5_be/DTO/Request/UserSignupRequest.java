package com.thai27.trang_tin_tuc_v5_be.DTO.Request;

import lombok.Data;

@Data
public class UserSignupRequest {
    private String username;
    private String password;
    private String email;
}
