package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Entity.NewsLike;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsLikeRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsLikeService {

    private final NewsLikeRepo newsLikeRepo;
    private final TrangTinTucUserRepo trangTinTucUserRepo;
    private final NewsRepo newsRepo;

    public void likeNews(String username, Long newsId) {
        TrangTinTucUser user = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài báo không tồn tại"));

        NewsLike like = new NewsLike();
        like.setUser(user);
        like.setNews(news);
        newsLikeRepo.save(like);
    }

    public void unlikeNews(String username, Long newsId) {
        TrangTinTucUser user = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài báo không tồn tại"));

        newsLikeRepo.findByUserAndNews(user, news).ifPresent(newsLikeRepo::delete);
    }

    public Long countLikes(Long newsId) {
        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài báo không tồn tại"));
        return newsLikeRepo.countByNews(news);
    }

    public Boolean isLikedByUser(String username, Long newsId) {
        TrangTinTucUser user = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("Bài báo không tồn tại"));

        return newsLikeRepo.existsByUserAndNews(user, news);
    }
}
