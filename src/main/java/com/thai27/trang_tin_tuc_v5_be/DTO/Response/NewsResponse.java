package com.thai27.trang_tin_tuc_v5_be.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {
    private Long id;
    private String title;
    private String description;
    private String writer;
    private String thumbnail;
    private String content;
    private Instant createdAt;
    private Long subCategoryId;
    private String subCategoryName;
    private Long categoryId;
    private String categoryName;
}
