package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.Entity.NewsTag;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.NewsTagService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news-tags")
public class NewsTagController {

    @Autowired
    private NewsTagService newsTagService;

    /**
     * Get all news tags
     * GET /api/news-tags
     */
    @GetMapping("/permit/get-all")
    public ResponseEntity<ApiResponse<List<NewsTag>>> getAllNewsTag() {
        return newsTagService.getAllNewsTag();
    }

    /**
     * Get news tags by news ID
     * GET /api/news-tags/by-news/{newsId}
     */
    @GetMapping("/by-news/{newsId}")
    public ResponseEntity<ApiResponse<List<NewsTag>>> getNewsTagsByNewsId(
            @PathVariable Long newsId
    ) throws ResourceNotFoundException {
        return newsTagService.getNewsTagsByNewsId(newsId);
    }

    /**
     * Add news tag
     * POST /api/news-tags?newsId=1
     */
    @PostMapping
    public ResponseEntity<ApiResponse<NewsTag>> addNewsTag(
            @RequestBody NewsTag newsTag,
            @RequestParam Long newsId
    ) throws ResourceNotFoundException {
        return newsTagService.addNewsTag(newsTag, newsId);
    }

    /**
     * Edit news tag
     * PUT /api/news-tags/{id}?newsId=1
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsTag>> editNewsTag(
            @PathVariable Long id,
            @RequestBody NewsTag newsTag,
            @RequestParam(required = false) Long newsId
    ) throws ResourceNotFoundException {
        return newsTagService.editNewsTag(id, newsTag, newsId);
    }

    /**
     * Get news tag by ID
     * GET /api/news-tags/get-by-id/{id}
     */
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<NewsTag>> getById(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return newsTagService.getById(id);
    }

    /**
     * Delete news tag
     * DELETE /api/news-tags/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteNewsTag(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return newsTagService.deleteNewsTag(id);
    }
}
