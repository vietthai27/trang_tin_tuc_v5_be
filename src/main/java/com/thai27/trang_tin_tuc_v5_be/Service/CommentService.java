package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.CommentRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CommentResponseDTO;
import com.thai27.trang_tin_tuc_v5_be.Entity.Comment;
import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Exception.ForbiddenException;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.CommentRepository;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepo;
    private final NewsRepo newsRepo;
    private final TrangTinTucUserRepo userRepo;

    public ResponseEntity<ApiResponse<Object>> addComment(String username, Long newsId, CommentRequest request) {
        TrangTinTucUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News không tồn tại"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setNews(news);
        commentRepo.save(comment);

        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Đã thêm bình luận")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> editComment(
            Long commentId,
            String username,
            Long newsId,
            CommentRequest request) {

        TrangTinTucUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment không tồn tại"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Không có quyền sửa comment này");
        }

        if (!comment.getNews().getId().equals(newsId)) {
            throw new ForbiddenException("Comment không thuộc bài viết này");
        }

        comment.setContent(request.getContent());

        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Da sua binh luan")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> deleteComment(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment không tồn tại"));

        commentRepo.delete(comment);

        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Đã xóa bình luận")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<List<CommentResponseDTO>>> getCommentsByNews(Long newsId) {
        List<CommentResponseDTO> comments = commentRepo
                .findByNews_IdOrderByCreatedAtDesc(newsId)
                .stream()
                .map(c -> new CommentResponseDTO(
                        c.getId(),
                        c.getContent(),
                        c.getCreatedAt(),
                        c.getUser().getId(),
                        c.getUser().getUsername()
                ))
                .toList();

        return ResponseEntity.ok(ApiResponse.<List<CommentResponseDTO>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy danh sách bình luận thành công")
                .data(comments)
                .build());
    }

}
