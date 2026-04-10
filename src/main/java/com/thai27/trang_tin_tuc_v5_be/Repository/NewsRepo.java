package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsListResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepo extends JpaRepository<News, Long> {

    Page<NewsListResponse> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(
            String title,
            Pageable pageable
    );

    Page<NewsListResponse> findByTitleContainingIgnoreCaseAndSubCategory_IdOrderByCreatedAtDesc(
            String title,
            Long subCategoryId,
            Pageable pageable
    );

    List<NewsListResponse> findTop5ByOrderByIdDesc();

    @Query("""
            SELECT n
            FROM News n
            LEFT JOIN FETCH n.subCategory sc
            LEFT JOIN FETCH sc.category
            WHERE n.id = :id
            """)
    Optional<News> findDetailById(Long id);

    @Query(value = """
            SELECT CAST(created_at AS date) AS createdDate, COUNT(*) AS totalNews
            FROM news
            GROUP BY CAST(created_at AS date)
            ORDER BY createdDate
            """, nativeQuery = true)
    List<Object[]> countNewsByCreatedDate();

}
