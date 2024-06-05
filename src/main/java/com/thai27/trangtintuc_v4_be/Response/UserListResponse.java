package com.thai27.trangtintuc_v4_be.Response;

import com.thai27.trangtintuc_v4_be.DTO.UserListDto;
import lombok.Data;

import java.util.List;

@Data
public class UserListResponse {

    private List<UserListDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
