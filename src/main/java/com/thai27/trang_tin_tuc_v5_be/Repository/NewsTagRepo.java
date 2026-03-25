package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Entity.NewsTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsTagRepo extends JpaRepository<NewsTag, Long> {
    
    List<NewsTag> findByNews(News news);
    
    List<NewsTag> findByNewsId(Long newsId);
}
