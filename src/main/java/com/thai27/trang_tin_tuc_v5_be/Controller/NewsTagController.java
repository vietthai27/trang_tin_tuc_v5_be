package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.Entity.NewsTag;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.NewsTagService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news-tags")
@AllArgsConstructor
public class NewsTagController {

    private final NewsTagService newsTagService;

    @GetMapping("/permit/get-all")
    public ResponseEntity<ApiResponse<List<NewsTag>>> getAllNewsTag() {
        return newsTagService.getAllNewsTag();
    }

    @GetMapping("/permit/by-news/{newsId}")
    public ResponseEntity<ApiResponse<List<NewsTag>>> getNewsTagsByNewsId(
            @PathVariable Long newsId
    ) throws ResourceNotFoundException {
        return newsTagService.getNewsTagsByNewsId(newsId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NewsTag>> addNewsTag(
            @RequestBody NewsTag newsTag
    ) throws ResourceNotFoundException {
        return newsTagService.addNewsTag(newsTag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsTag>> editNewsTag(
            @PathVariable Long id,
            @RequestBody NewsTag newsTag
    ) throws ResourceNotFoundException {
        return newsTagService.editNewsTag(id, newsTag);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<NewsTag>> getById(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return newsTagService.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteNewsTag(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return newsTagService.deleteNewsTag(id);
    }

    @DeleteMapping("/remove-from-news/{newsId}")
    public ResponseEntity<ApiResponse<Object>> deleteTagToNews(
            @PathVariable Long newsId,
            @RequestBody List<Long> tagIds
    ) {
        return newsTagService.deleteTagToNews(newsId, tagIds);
    }

}
