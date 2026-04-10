package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Mapper.CommentResponseMapper;
import com.thai27.trang_tin_tuc_v5_be.DTO.Request.CommentRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CommentResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.Comment;
import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Exception.ForbiddenException;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.CommentRepository;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepo;
    private final NewsRepo newsRepo;
    private final TrangTinTucUserRepo userRepo;
    private final CommentResponseMapper commentResponseMapper;

    public void addComment(String username, Long newsId, CommentRequest request) {
        TrangTinTucUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài báo không tồn tại"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setNews(news);
        commentRepo.save(comment);
    }

    public void editComment(Long commentId, String username, Long newsId, CommentRequest request) {
        TrangTinTucUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment không tồn tại"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Không khớp user");
        }
        if (!comment.getNews().getId().equals(newsId)) {
            throw new ForbiddenException("Không khớp bài báo");
        }

        comment.setContent(request.getContent());
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment không tồn tại"));
        commentRepo.delete(comment);
    }

    public List<CommentResponse> getCommentsByNews(Long newsId) {
        return commentRepo.findByNews_IdOrderByCreatedAtDesc(newsId).stream().map(commentResponseMapper::mapToDto).toList();
    }
}
