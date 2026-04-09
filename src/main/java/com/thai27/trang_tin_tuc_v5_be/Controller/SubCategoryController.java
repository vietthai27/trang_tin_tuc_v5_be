package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.SubCategoryRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CategoryNewResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.SubCategoryResponse;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.SubCategoryService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sub-categories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SubCategoryResponse>>> searchSubCategory(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") Long categoryId,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(ApiResponse.<Page<SubCategoryResponse>>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(subCategoryService.searchAllSubCategory(search, categoryId, pageNum, pageSize))
                .build());
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryNewResponse>> getSubCategoriesByCategoryId(@PathVariable Long categoryId) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<CategoryNewResponse>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(subCategoryService.getSubCategoriesByCategoryId(categoryId))
                .build());
    }

    @GetMapping("/permit/by-category/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryNewResponse>> getSubCategoriesByCategoryIdPermit(@PathVariable Long categoryId) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<CategoryNewResponse>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(subCategoryService.getSubCategoriesByCategoryId(categoryId))
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SubCategoryResponse>> addSubCategory(
            @RequestBody SubCategoryRequest subCategory,
            @RequestParam Long categoryId
    ) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<SubCategoryResponse>builder()
                .message("ThÃªm dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(subCategoryService.addSubCategory(subCategory, categoryId))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubCategoryResponse>> editSubCategory(
            @PathVariable Long id,
            @RequestBody SubCategoryRequest subCategory,
            @RequestParam(required = false) Long categoryId
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<SubCategoryResponse>builder()
                .message("Sá»­a dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(subCategoryService.editSubCategory(id, subCategory, categoryId))
                .build());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<SubCategoryResponse>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<SubCategoryResponse>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(subCategoryService.getById(id))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteSubCategory(@PathVariable Long id) throws ResourceNotFoundException {
        subCategoryService.deleteSubCategory(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("XÃ³a dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(null)
                .build());
    }
}
