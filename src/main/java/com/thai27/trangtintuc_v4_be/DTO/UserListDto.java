package com.thai27.trangtintuc_v4_be.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserListDto {

    private Long id;
    private String username;
    private String email;
    private List<RoleListDto> listRoles;
}
