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

    public void addComment(String username, Long newsId, CommentRequest request) {
        TrangTinTucUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User khÃ´ng tá»“n táº¡i"));

        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News khÃ´ng tá»“n táº¡i"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setNews(news);
        commentRepo.save(comment);
    }

    public void editComment(Long commentId, String username, Long newsId, CommentRequest request) {
        TrangTinTucUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User khÃ´ng tá»“n táº¡i"));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment khÃ´ng tá»“n táº¡i"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("KhÃ´ng cÃ³ quyá»n sá»­a comment nÃ y");
        }
        if (!comment.getNews().getId().equals(newsId)) {
            throw new ForbiddenException("Comment khÃ´ng thuá»™c bÃ i viáº¿t nÃ y");
        }

        comment.setContent(request.getContent());
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment khÃ´ng tá»“n táº¡i"));
        commentRepo.delete(comment);
    }

    public List<CommentResponseDTO> getCommentsByNews(Long newsId) {
        return commentRepo.findByNews_IdOrderByCreatedAtDesc(newsId)
                .stream()
                .map(c -> new CommentResponseDTO(
                        c.getId(),
                        c.getContent(),
                        c.getCreatedAt(),
                        c.getUser().getId(),
                        c.getUser().getUsername()
                ))
                .toList();
    }
}
