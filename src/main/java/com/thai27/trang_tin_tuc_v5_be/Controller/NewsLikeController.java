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
    public ResponseEntity<ApiResponse<Object>> like(@PathVariable Long newsId, @RequestParam String username) {
        newsLikeService.likeNews(username, newsId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Da like bai bao")
                .data(null)
                .build());
    }

    @DeleteMapping("/{newsId}/unlike")
    public ResponseEntity<ApiResponse<Object>> unlike(@PathVariable Long newsId, @RequestParam String username) {
        newsLikeService.unlikeNews(username, newsId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Da huy like bai bao")
                .data(null)
                .build());
    }

    @GetMapping("/permit/{newsId}/count")
    public ResponseEntity<ApiResponse<Long>> countLikes(@PathVariable Long newsId) {
        return ResponseEntity.ok(ApiResponse.<Long>builder()
                .message("ÄÃ£ like bÃ i bÃ¡o")
                .data(newsLikeService.countLikes(newsId))
                .build());
    }

    @GetMapping("/{newsId}/is-like-by-user")
    public ResponseEntity<ApiResponse<Boolean>> isLikeByUser(@PathVariable Long newsId, @RequestParam String username) {
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .message("ÄÃ£ like bÃ i bÃ¡o")
                .data(newsLikeService.isLikedByUser(username, newsId))
                .build());
    }
}
