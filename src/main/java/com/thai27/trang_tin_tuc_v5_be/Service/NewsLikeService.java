package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Entity.NewsLike;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsLikeRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsLikeService {

    private final NewsLikeRepo newsLikeRepo;
    private final TrangTinTucUserRepo trangTinTucUserRepo;
    private final NewsRepo newsRepo;

    public ResponseEntity<ApiResponse<Object>> likeNews(String username, Long newsId) {
        TrangTinTucUser user = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News không tồn tại"));

        NewsLike like = new NewsLike();
        like.setUser(user);
        like.setNews(news);
        newsLikeRepo.save(like);

        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Da like bai bao")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> unlikeNews(String username, Long newsId) {
        TrangTinTucUser user = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News không tồn tại"));

        newsLikeRepo.findByUserAndNews(user, news).ifPresent(newsLikeRepo::delete);

        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Da huy like bai bao")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Long>> countLikes(Long newsId) {
        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News không tồn tại"));

        return ResponseEntity.ok(ApiResponse.<Long>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Đã like bài báo")
                .data(newsLikeRepo.countByNews(news))
                .build());
    }

    public ResponseEntity<ApiResponse<Boolean>> isLikedByUser(String username, Long newsId) {
        TrangTinTucUser user = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News không tồn tại"));

        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Đã like bài báo")
                .data(newsLikeRepo.existsByUserAndNews(user, news))
                .build());
    }

}
