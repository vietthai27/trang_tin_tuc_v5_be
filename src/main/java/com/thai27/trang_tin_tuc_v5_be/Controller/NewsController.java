package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.NewsCreateRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.GetNewsByIdResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.ImageKitResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsListResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsResponse;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.ImageKitService;
import com.thai27.trang_tin_tuc_v5_be.Service.MessageService;
import com.thai27.trang_tin_tuc_v5_be.Service.NewsService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
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

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@Valid @RequestBody NewsCreateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        newsService.create(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Void>builder()
                .message(messageService.getMessage("add.success"))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> edit(
            @PathVariable Long id,
            @Valid @RequestBody NewsCreateRequest request
    ) throws ResourceNotFoundException {
        newsService.edit(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(messageService.getMessage("edit.success"))
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<NewsListResponse>>> searchCategory(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<NewsListResponse>>builder()
                .message(messageService.getMessage("get.success"))
                .data(newsService.searchAllNews(title, pageNum, pageSize))
                .build());
    }

    @PostMapping("/imagekit/upload")
    public ResponseEntity<ApiResponse<ImageKitResponse>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<ImageKitResponse>builder()
                .message(messageService.getMessage("upload.success"))
                .data(imageKitService.upload(file))
                .build());
    }

    @DeleteMapping("/imagekit")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@RequestParam String url) {
        imageKitService.deleteImage(url);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(messageService.getMessage("delete.success"))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNews(@PathVariable Long id) throws ResourceNotFoundException {
        newsService.deleteNews(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(messageService.getMessage("delete.success"))
                .build());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<GetNewsByIdResponse>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<GetNewsByIdResponse>builder()
                .message(messageService.getMessage("get.success"))
                .data(newsService.getById(id))
                .build());
    }

    @GetMapping("/permit/get-latest-news")
    public ResponseEntity<ApiResponse<List<NewsListResponse>>> getLatestNews() {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<NewsListResponse>>builder()
                .message(messageService.getMessage("get.success"))
                .data(newsService.getLatestNews())
                .build());
    }

    @GetMapping("/permit/get-news-detail/{id}")
    public ResponseEntity<ApiResponse<NewsResponse>> getNewsDetail(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<NewsResponse>builder()
                .message(messageService.getMessage("get.success"))
                .data(newsService.getNewsDetail(id))
                .build());
    }

    @GetMapping("/permit/get-news-by-sub-category")
    public ResponseEntity<ApiResponse<Page<NewsListResponse>>> getNewsBySubCategory(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "0") Long categoryId,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<NewsListResponse>>builder()
                .message(messageService.getMessage("get.success"))
                .data(newsService.getNewsBySubCategory(title, categoryId, pageNum, pageSize))
                .build());
    }
}
