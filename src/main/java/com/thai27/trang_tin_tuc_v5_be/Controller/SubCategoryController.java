package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CategoryNewResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.SubCategory;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.SubCategoryService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sub-categories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SubCategory>>> searchSubCategory(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") Long categoryId,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return subCategoryService.searchAllSubCategory(search, categoryId, pageNum, pageSize);
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryNewResponse>> getSubCategoriesByCategoryId(
            @PathVariable Long categoryId
    ) throws ResourceNotFoundException {
        return subCategoryService.getSubCategoriesByCategoryId(categoryId);
    }

    @GetMapping("/permit/by-category/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryNewResponse>> getSubCategoriesByCategoryIdPermit(
            @PathVariable Long categoryId
    ) throws ResourceNotFoundException {
        return subCategoryService.getSubCategoriesByCategoryId(categoryId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SubCategory>> addSubCategory(
            @RequestBody SubCategory subCategory,
            @RequestParam Long categoryId
    ) throws ResourceNotFoundException {
        return subCategoryService.addSubCategory(subCategory, categoryId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubCategory>> editSubCategory(
            @PathVariable Long id,
            @RequestBody SubCategory subCategory,
            @RequestParam(required = false) Long categoryId
    ) throws ResourceNotFoundException {
        return subCategoryService.editSubCategory(id, subCategory, categoryId);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<SubCategory>> getById(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return subCategoryService.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteSubCategory(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return subCategoryService.deleteSubCategory(id);
    }
}
