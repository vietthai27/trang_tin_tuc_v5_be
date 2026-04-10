package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.CategoryRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CategoryResponse;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.CategoryService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
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
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<CategoryResponse>>builder()
                .message(Constant.GET_DATA_SUCCESS)
                .data(categoryService.searchAllCategory(search, pageNum, pageSize))
                .build());
    }

    @GetMapping("/get-by-category-id/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getByCategoryId(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<CategoryResponse>builder()
                .message(Constant.GET_DATA_SUCCESS)
                .data(categoryService.getById(id))
                .build());
    }

    @GetMapping("/permit/get-all")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategory() {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<CategoryResponse>>builder()
                .message(Constant.GET_DATA_SUCCESS)
                .data(categoryService.getAllCategory())
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> addCategory(@RequestBody CategoryRequest category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<CategoryResponse>builder()
                .message(Constant.ADDED)
                .data(categoryService.addCategory(category))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> editCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest category
    ) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<CategoryResponse>builder()
                .message(Constant.EDITED)
                .data(categoryService.editCategory(id, category))
                .build());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<CategoryResponse>builder()
                .message(Constant.GET_DATA_SUCCESS)
                .data(categoryService.getById(id))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) throws ResourceNotFoundException {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(Constant.DELETED)
                .build());
    }
}
