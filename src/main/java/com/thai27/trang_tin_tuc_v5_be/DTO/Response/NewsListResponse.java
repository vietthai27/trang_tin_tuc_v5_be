package com.thai27.trang_tin_tuc_v5_be.DTO.Response;

import java.time.Instant;

public interface NewsListResponse {
    Long getId();
    String getTitle();
    String getDescription();
    String getThumbnail();
    Instant getCreatedAt();
    String getWriter();
}