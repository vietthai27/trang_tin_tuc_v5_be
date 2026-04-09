package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.CategoryRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CategoryResponse;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.CategoryService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> searchCategory(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(ApiResponse.<Page<CategoryResponse>>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(categoryService.searchAllCategory(search, pageNum, pageSize))
                .build());
    }

    @GetMapping("/get-by-category-id/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getByCategoryId(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(categoryService.getById(id))
                .build());
    }

    @GetMapping("/permit/get-all")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategory() {
        return ResponseEntity.ok(ApiResponse.<List<CategoryResponse>>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(categoryService.getAllCategory())
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> addCategory(@RequestBody CategoryRequest category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<CategoryResponse>builder()
                .message("ThÃªm dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(categoryService.addCategory(category))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> editCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest category
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .message("Sá»­a dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(categoryService.editCategory(id, category))
                .build());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(categoryService.getById(id))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(@PathVariable Long id) throws ResourceNotFoundException {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("XÃ³a dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(null)
                .build());
    }
}
