package com.thai27.trang_tin_tuc_v5_be.DTO.Mapper;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CategoryResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryResponseMapper {
    CategoryResponse mapToDto(Category category);
}
