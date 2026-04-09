package com.thai27.trang_tin_tuc_v5_be.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryResponse {
    private Long id;
    private String name;
    private String icon;
    private Long categoryId;
    private String categoryName;
}
