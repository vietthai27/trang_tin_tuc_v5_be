package com.thai27.trang_tin_tuc_v5_be.DTO.Response;

import com.thai27.trang_tin_tuc_v5_be.Entity.SubCategory;
import lombok.Data;

import java.util.List;

@Data
public class CategoryNewResponse {

    private List<SubCategory> data;
    private String categoryName;

}
