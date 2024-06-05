package com.thai27.trang_tin_tuc_v5_be.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserListDto {

    private Long id;
    private String username;
    private String email;
    private List<RoleListDto> listRoles;
}
