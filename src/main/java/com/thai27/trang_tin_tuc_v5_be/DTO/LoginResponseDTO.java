package com.thai27.trang_tin_tuc_v5_be.DTO;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponseDTO {
    private String JWTtoken;
    private String username;
    private List<String> userRoles;
}
