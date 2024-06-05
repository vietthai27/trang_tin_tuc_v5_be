package com.thai27.trang_tin_tuc_v5_be.Response;

import com.thai27.trang_tin_tuc_v5_be.DTO.UserListDto;
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
