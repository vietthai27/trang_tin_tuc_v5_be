package com.thai27.trang_tin_tuc_v5_be.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewsCreateRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 255)
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 500)
        String description,

        @NotNull(message = "Sub category is required")
        Long subCategoryId,

        @NotBlank(message = "Content is required")
        String content,

        List<String> imagesUrl
) {
}
