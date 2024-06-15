package com.thai27.trang_tin_tuc_v5_be.Request;

import lombok.Data;

@Data
public class UserValidateSignupRequest {
    private String validateCode;
    private String email;
}
