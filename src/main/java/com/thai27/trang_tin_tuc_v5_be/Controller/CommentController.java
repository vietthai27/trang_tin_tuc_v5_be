package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.CommentRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CommentResponseDTO;
import com.thai27.trang_tin_tuc_v5_be.Entity.Comment;
import com.thai27.trang_tin_tuc_v5_be.Service.CommentService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
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
        return commentService.addComment(username, newsId, request);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Object>> editComment(
            @RequestParam Long commentId,
            @RequestParam String username,
            @RequestParam Long newsId,
            @RequestBody CommentRequest request) {
        return commentService.editComment(commentId, username, newsId, request);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(
            @PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }

    @GetMapping("/permit/news/{newsId}")
    public ResponseEntity<ApiResponse<List<CommentResponseDTO>>> getCommentsByNews(
            @PathVariable Long newsId) {
        return commentService.getCommentsByNews(newsId);
    }
}
