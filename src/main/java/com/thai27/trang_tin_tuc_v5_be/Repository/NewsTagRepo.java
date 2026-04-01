package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsTagDTO;
import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Entity.NewsTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NewsTagRepo extends JpaRepository<NewsTag, Long> {
    
    List<NewsTag> findByNews(News news);

    List<NewsTag> findByIdIn(List<Long> ids);

    @Transactional
    @Modifying
    @Query(value = """
    DELETE FROM news_tag 
    WHERE news_id = :newsId 
      AND tag_id IN (:tagIds)
    """, nativeQuery = true)
    void deleteByNewsIdAndTagIds(Long newsId, List<Long> tagIds);

    Page<NewsTagDTO> findAllByTagNameLikeIgnoreCaseOrderById(String search, PageRequest pageRequest);
}
