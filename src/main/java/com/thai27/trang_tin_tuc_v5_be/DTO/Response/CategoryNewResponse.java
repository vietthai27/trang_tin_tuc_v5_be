package com.thai27.trang_tin_tuc_v5_be.DTO.Response;

import lombok.Data;

import java.util.List;

@Data
public class CategoryNewResponse {

    private List<SubCategoryResponse> data;
    private String categoryName;

}
