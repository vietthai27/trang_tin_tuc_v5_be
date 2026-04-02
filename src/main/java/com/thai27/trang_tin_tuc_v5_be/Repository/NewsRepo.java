package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsListDTO;
import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepo extends JpaRepository<News, Long> {

    Page<NewsListDTO> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(
            String title,
            Pageable pageable
    );

    Page<NewsListDTO> findByTitleContainingIgnoreCaseAndSubCategory_IdOrderByCreatedAtDesc(
            String title,
            Long subCategoryId,
            Pageable pageable
    );

    List<NewsListDTO> findTop5ByOrderByIdDesc();

    @Query(value = """
            SELECT CAST(created_at AS date) AS createdDate, COUNT(*) AS totalNews
            FROM news
            GROUP BY CAST(created_at AS date)
            ORDER BY createdDate
            """, nativeQuery = true)
    List<Object[]> countNewsByCreatedDate();

}
