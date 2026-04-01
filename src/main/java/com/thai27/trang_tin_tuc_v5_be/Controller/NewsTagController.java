package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsTagDTO;
import com.thai27.trang_tin_tuc_v5_be.Entity.NewsTag;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.NewsTagService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news-tags")
@AllArgsConstructor
public class NewsTagController {

    private final NewsTagService newsTagService;

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
    @GetMapping("/permit/by-news/{newsId}")
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
            @RequestBody NewsTag newsTag
    ) throws ResourceNotFoundException {
        return newsTagService.addNewsTag(newsTag);
    }

    /**
     * Edit news tag
     * PUT /api/news-tags/{id}?newsId=1
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsTag>> editNewsTag(
            @PathVariable Long id,
            @RequestBody NewsTag newsTag
    ) throws ResourceNotFoundException {
        return newsTagService.editNewsTag(id, newsTag);
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

    @DeleteMapping("/remove-from-news/{newsId}")
    public ResponseEntity<ApiResponse<Object>> deleteTagToNews(
            @PathVariable Long newsId,
            @RequestBody List<Long> tagIds
    ) {
        return newsTagService.deleteTagToNews(newsId, tagIds);
    }

    /**
     * Search news tags (pagination)
     * GET /api/news-tags/search?search=abc&pageNum=0&pageSize=10
     */
    @GetMapping("/permit/search")
    public ResponseEntity<ApiResponse<Page<NewsTagDTO>>> searchNewsTag(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return newsTagService.searchNewsTag(search, pageNum, pageSize);
    }
}
