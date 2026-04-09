package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.NewsCreateRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.GetNewsByIdResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.ImageKitResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsListDTO;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsResponse;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.ImageKitService;
import com.thai27.trang_tin_tuc_v5_be.Service.NewsService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final ImageKitService imageKitService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@Valid @RequestBody NewsCreateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        newsService.create(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .message("ThÃªm bÃ i bÃ¡o thÃ nh cÃ´ng")
                .data(null)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> edit(
            @PathVariable Long id,
            @Valid @RequestBody NewsCreateRequest request
    ) throws ResourceNotFoundException {
        newsService.edit(id, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Sá»­a bÃ i bÃ¡o thÃ nh cÃ´ng")
                .data(null)
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<NewsListDTO>>> searchCategory(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(ApiResponse.<Page<NewsListDTO>>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(newsService.searchAllNews(title, pageNum, pageSize))
                .build());
    }

    @PostMapping("/imagekit/upload")
    public ResponseEntity<ApiResponse<ImageKitResponse>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(ApiResponse.<ImageKitResponse>builder()
                .message("Upload áº£nh thÃ nh cÃ´ng")
                .data(imageKitService.upload(file))
                .build());
    }

    @DeleteMapping("/imagekit")
    public ResponseEntity<ApiResponse<Object>> deleteImage(@RequestParam String url) {
        imageKitService.deleteImage(url);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("XÃ³a áº£nh thÃ nh cÃ´ng")
                .data(null)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteNews(@PathVariable Long id) throws ResourceNotFoundException {
        newsService.deleteNews(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("XÃ³a bÃ i bÃ¡o thÃ nh cÃ´ng")
                .data(null)
                .build());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<GetNewsByIdResponse>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<GetNewsByIdResponse>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(newsService.getById(id))
                .build());
    }

    @GetMapping("/permit/get-latest-news")
    public ResponseEntity<ApiResponse<List<NewsListDTO>>> getLatestNews() {
        return ResponseEntity.ok(ApiResponse.<List<NewsListDTO>>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(newsService.getLatestNews())
                .build());
    }

    @GetMapping("/permit/get-news-detail/{id}")
    public ResponseEntity<ApiResponse<NewsResponse>> getNewsDetail(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<NewsResponse>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(newsService.getNewsDetail(id))
                .build());
    }

    @GetMapping("/permit/get-news-by-sub-category")
    public ResponseEntity<ApiResponse<Page<NewsListDTO>>> getNewsBySubCategory(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "0") Long categoryId,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(ApiResponse.<Page<NewsListDTO>>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(newsService.getNewsBySubCategory(title, categoryId, pageNum, pageSize))
                .build());
    }
}
