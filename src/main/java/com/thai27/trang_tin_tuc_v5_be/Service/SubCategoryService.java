package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CategoryNewResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import com.thai27.trang_tin_tuc_v5_be.Entity.SubCategory;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.CategoryRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.SubCategoryRepo;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {

    @Autowired
    SubCategoryRepo subCategoryRepo;

    @Autowired
    CategoryRepo categoryRepo;

    public ResponseEntity<ApiResponse<SubCategory>> addSubCategory(SubCategory subCategory, Long categoryId) throws ResourceNotFoundException {
        if (categoryId == null) {
            throw new ResourceNotFoundException("ID danh mục không được để trống");
        }
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + categoryId));
        
        SubCategory addSubCategory = new SubCategory();
        addSubCategory.setName(subCategory.getName());
        addSubCategory.setIcon(subCategory.getIcon());
        addSubCategory.setCategory(category);
        
        return ResponseEntity.ok(ApiResponse.<SubCategory>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Thêm dữ liệu thành công")
                .data(subCategoryRepo.save(addSubCategory))
                .build());
    }

    public ResponseEntity<ApiResponse<SubCategory>> editSubCategory(Long id, SubCategory subCategory, Long categoryId) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        SubCategory editSubCategory = subCategoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục con với id: " + id));
        
        if (subCategory.getName() != null && !subCategory.getName().isBlank()) {
            editSubCategory.setName(subCategory.getName());
        }
        if (subCategory.getIcon() != null) {
            editSubCategory.setIcon(subCategory.getIcon());
        }
        
        if (categoryId != null) {
            Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + categoryId));
            editSubCategory.setCategory(category);
        }
        
        editSubCategory.setId(id);
        return ResponseEntity.ok(ApiResponse.<SubCategory>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Sửa dữ liệu thành công")
                .data(subCategoryRepo.save(editSubCategory))
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> deleteSubCategory(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        SubCategory deleteSubCategory = subCategoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục con với id: " + id));
        if (deleteSubCategory == null) {
            throw new ResourceNotFoundException("Không tìm thấy danh mục con");
        }
        subCategoryRepo.delete(deleteSubCategory);
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Xóa dữ liệu thành công")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<SubCategory>> getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        SubCategory subCategory = subCategoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục con với id: " + id));
        return ResponseEntity.ok(ApiResponse.<SubCategory>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(subCategory)
                .build());
    }

    public ResponseEntity<ApiResponse<Page<SubCategory>>> searchAllSubCategory(String search, Long categoryId, int pageNum, int pageSize) {
        PageRequest searchSubCategoryPaging = PageRequest.of(pageNum, pageSize);
        String searchLike = "%" + search + "%";
        return ResponseEntity.ok(ApiResponse.<Page<SubCategory>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(subCategoryRepo.findAllByNameLikeIgnoreCaseAndCategory_IdOrderById(searchLike, categoryId, searchSubCategoryPaging))
                .build());
    }

    public ResponseEntity<ApiResponse<CategoryNewResponse>> getSubCategoriesByCategoryId(Long categoryId) throws ResourceNotFoundException {
        if (categoryId == null) {
            throw new ResourceNotFoundException("ID danh mục không được để trống");
        }
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + categoryId));
        List<SubCategory> subCategories = subCategoryRepo.findByCategory(category);
        CategoryNewResponse categoryNewResponse = new CategoryNewResponse();
        categoryNewResponse.setCategoryName(category.getName());
        categoryNewResponse.setData(subCategories);
        return ResponseEntity.ok(ApiResponse.<CategoryNewResponse>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(categoryNewResponse)
                .build());
    }
}
