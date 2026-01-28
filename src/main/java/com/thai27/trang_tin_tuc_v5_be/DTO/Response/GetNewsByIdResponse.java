package com.thai27.trang_tin_tuc_v5_be.DTO.Response;

import com.thai27.trang_tin_tuc_v5_be.Entity.ImageKit;
import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import lombok.Data;

import java.util.List;

@Data
public class GetNewsByIdResponse {
    private News news;
    private List<ImageKit> listImage;
    private Long subCategoryId;
    private Long categoryId;
}
