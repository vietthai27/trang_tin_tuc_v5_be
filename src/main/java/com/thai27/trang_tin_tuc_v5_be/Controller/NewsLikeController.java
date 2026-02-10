package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.Service.NewsLikeService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news-like")
@RequiredArgsConstructor
public class NewsLikeController {

    private final NewsLikeService newsLikeService;

    @PostMapping("/{newsId}/like")
    public ResponseEntity<ApiResponse<Object>> like(
            @PathVariable Long newsId,
            @RequestParam String username) {
        return newsLikeService.likeNews(username, newsId);
    }

    @DeleteMapping("/{newsId}/unlike")
    public ResponseEntity<ApiResponse<Object>> unlike(
            @PathVariable Long newsId,
            @RequestParam String username) {
        return newsLikeService.unlikeNews(username, newsId);
    }

    @GetMapping("/permit/{newsId}/count")
    public ResponseEntity<ApiResponse<Long>> countLikes(@PathVariable Long newsId) {
        return newsLikeService.countLikes(newsId);
    }

    @GetMapping("/{newsId}/is-like-by-user")
    public ResponseEntity<ApiResponse<Boolean>> isLikeByUser(@PathVariable Long newsId,  @RequestParam String username) {
        return newsLikeService.isLikedByUser(username, newsId);
    }
}
