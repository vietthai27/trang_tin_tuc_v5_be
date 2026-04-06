package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.CategoryService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Category>>> searchCategory(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return categoryService.searchAllCategory(search, pageNum, pageSize);
    }

    @GetMapping("/get-by-category-id/{id}")
    public ResponseEntity<ApiResponse<Category>> getByCategoryId(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return categoryService.getById(id);
    }

    @GetMapping("/permit/get-all")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> addCategory(
            @RequestBody Category category
    ) {
        return categoryService.addCategory(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> editCategory(
            @PathVariable Long id,
            @RequestBody Category category
    ) throws ResourceNotFoundException {
        return categoryService.editCategory(id, category);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<Category>> getById(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return categoryService.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return categoryService.deleteCategory(id);
    }
}
