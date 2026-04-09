package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.CommentRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CommentResponseDTO;
import com.thai27.trang_tin_tuc_v5_be.Service.CommentService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> addComment(
            @RequestParam String username,
            @RequestParam Long newsId,
            @RequestBody CommentRequest request) {
        commentService.addComment(username, newsId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .message("ÄÃ£ thÃªm bÃ¬nh luáº­n")
                .data(null)
                .build());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Object>> editComment(
            @RequestParam Long commentId,
            @RequestParam String username,
            @RequestParam Long newsId,
            @RequestBody CommentRequest request) {
        commentService.editComment(commentId, username, newsId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Da sua binh luan")
                .data(null)
                .build());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("ÄÃ£ xÃ³a bÃ¬nh luáº­n")
                .data(null)
                .build());
    }

    @GetMapping("/permit/news/{newsId}")
    public ResponseEntity<ApiResponse<List<CommentResponseDTO>>> getCommentsByNews(@PathVariable Long newsId) {
        return ResponseEntity.ok(ApiResponse.<List<CommentResponseDTO>>builder()
                .message("Láº¥y danh sÃ¡ch bÃ¬nh luáº­n thÃ nh cÃ´ng")
                .data(commentService.getCommentsByNews(newsId))
                .build());
    }
}
