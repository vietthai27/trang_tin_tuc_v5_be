package com.thai27.trang_tin_tuc_v5_be.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagementResponse {
    private Long id;
    private String name;
    private String icon;
    private String path;
    private List<RoleResponse> rolesManage;
}
