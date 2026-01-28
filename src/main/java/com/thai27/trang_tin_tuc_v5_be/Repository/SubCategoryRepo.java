package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import com.thai27.trang_tin_tuc_v5_be.Entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findByCategory(Category category);

    Page<SubCategory> findByCategoryId(Long categoryId, Pageable pageable);

    Page<SubCategory> findAllByNameLikeIgnoreCaseAndCategory_IdOrderById(String search, Long categoryId, Pageable pageable);

}
