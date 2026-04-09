package com.thai27.trang_tin_tuc_v5_be.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageKitResponse {
    private Long id;
    private String url;
    private String fileId;
}
