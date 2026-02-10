package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Entity.NewsLike;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsLikeRepo extends JpaRepository<NewsLike, Long> {

    boolean existsByUserAndNews(TrangTinTucUser user, News news);

    Optional<NewsLike> findByUserAndNews(TrangTinTucUser user, News news);

    long countByNews(News news);

}
