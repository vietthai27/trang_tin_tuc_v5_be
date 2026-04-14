package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.Service.MessageService;
import com.thai27.trang_tin_tuc_v5_be.Service.NewsLikeService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news-like")
@RequiredArgsConstructor
public class NewsLikeController {

    private final NewsLikeService newsLikeService;

    private final MessageService messageService;

    @PostMapping("/{newsId}/like")
    public ResponseEntity<ApiResponse<Void>> like(@PathVariable Long newsId, @RequestParam String username) {
        newsLikeService.likeNews(username, newsId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message("Like thành công")
                .build());
    }

    @DeleteMapping("/{newsId}/unlike")
    public ResponseEntity<ApiResponse<Void>> unlike(@PathVariable Long newsId, @RequestParam String username) {
        newsLikeService.unlikeNews(username, newsId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message("Hủy like thành công")
                .build());
    }

    @GetMapping("/permit/{newsId}/count")
    public ResponseEntity<ApiResponse<Long>> countLikes(@PathVariable Long newsId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Long>builder()
                .message(messageService.getMessage("get.success"))
                .data(newsLikeService.countLikes(newsId))
                .build());
    }

    @GetMapping("/{newsId}/is-like-by-user")
    public ResponseEntity<ApiResponse<Boolean>> isLikeByUser(@PathVariable Long newsId, @RequestParam String username) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Boolean>builder()
                .message(messageService.getMessage("get.success"))
                .data(newsLikeService.isLikedByUser(username, newsId))
                .build());
    }
}
