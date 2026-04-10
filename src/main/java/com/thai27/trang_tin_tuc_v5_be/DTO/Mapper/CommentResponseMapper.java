package com.thai27.trang_tin_tuc_v5_be.DTO.Mapper;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CommentResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentResponseMapper {
    CommentResponse mapToDto(Comment comment);
}
