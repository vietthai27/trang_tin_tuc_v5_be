package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.CategoryService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Search + pagination
     * GET /api/categories/search?search=abc&pageNum=0&pageSize=10
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Category>>> searchCategory(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return categoryService.searchAllCategory(search, pageNum, pageSize);
    }

    /**
     * Get all categories
     * GET /api/categories
     */
    @GetMapping("/permit/get-all")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategory() {
        return categoryService.getAllCategory();
    }

    /**
     * Add category
     * POST /api/categories
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> addCategory(
            @RequestBody Category category
    ) {
        return categoryService.addCategory(category);
    }

    /**
     * Edit category
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> editCategory(
            @PathVariable Long id,
            @RequestBody Category category
    ) throws ResourceNotFoundException {
        return categoryService.editCategory(id, category);
    }
}
