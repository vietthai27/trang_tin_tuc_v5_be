package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.NewsCreateRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.GetNewsByIdResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsListDTO;
import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import com.thai27.trang_tin_tuc_v5_be.Entity.ImageKit;
import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.ImageKitService;
import com.thai27.trang_tin_tuc_v5_be.Service.NewsService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResponse<Object>> create(
            @Valid @RequestBody NewsCreateRequest request
    ) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String username = authentication.getName();
        return newsService.create(request, username);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> edit(
            @PathVariable Long id,
            @Valid @RequestBody NewsCreateRequest request
    ) throws ResourceNotFoundException {
        return newsService.edit(id, request);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<NewsListDTO>>> searchCategory(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return newsService.searchAllNews(title, pageNum, pageSize);
    }


    @PostMapping("/imagekit/upload")
    public ResponseEntity<ApiResponse<ImageKit>> uploadImage(@RequestParam("file") MultipartFile file)
            throws IOException {
        return imageKitService.upload(file);
    }

    @DeleteMapping("/imagekit")
    public ResponseEntity<ApiResponse<Object>> deleteImage(@RequestParam String url) {
        return imageKitService.deleteImage(url);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteNews(@PathVariable Long id) throws ResourceNotFoundException {
        return newsService.deleteNews(id);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<GetNewsByIdResponse>> getById(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return newsService.getById(id);
    }

    @GetMapping("/permit/get-latest-news")
    public ResponseEntity<ApiResponse<List<NewsListDTO>>> getLatestNews() {
        return newsService.getLatestNews();
    }

    @GetMapping("/permit/get-news-detail/{id}")
    public ResponseEntity<ApiResponse<News>> getNewsDetail(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return newsService.getNewsDetail(id);
    }
}
