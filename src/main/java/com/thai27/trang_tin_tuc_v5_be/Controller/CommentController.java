package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.CommentRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CommentResponse;
import com.thai27.trang_tin_tuc_v5_be.Service.CommentService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addComment(
            @RequestParam String username,
            @RequestParam Long newsId,
            @RequestBody CommentRequest request) {
        commentService.addComment(username, newsId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>builder()
                        .message(Constant.ADDED)
                        .build());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> editComment(
            @RequestParam Long commentId,
            @RequestParam String username,
            @RequestParam Long newsId,
            @RequestBody CommentRequest request) {
        commentService.editComment(commentId, username, newsId, request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(Constant.EDITED)
                .build());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(Constant.DELETED)
                .build());
    }

    @GetMapping("/permit/news/{newsId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByNews(@PathVariable Long newsId) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<CommentResponse>>builder()
                .message(Constant.GET_DATA_SUCCESS)
                .data(commentService.getCommentsByNews(newsId))
                .build());
    }
}
