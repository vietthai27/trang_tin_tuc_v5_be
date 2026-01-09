package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;

import com.thai27.trang_tin_tuc_v5_be.Repository.CategoryRepo;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    public ResponseEntity<ApiResponse<Page<Category>>> searchAllCategory(String search, int pageNum, int pageSize) {
        PageRequest searchCategoryPaging = PageRequest.of(pageNum, pageSize);
        String searchLike = "%" + search + "%";
        return ResponseEntity.ok(ApiResponse.<Page<Category>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(categoryRepo.findAllByNameLikeIgnoreCaseOrderById(searchLike, searchCategoryPaging))
                .build());
    }

    public ResponseEntity<ApiResponse<List<Category>>> getAllCategory() {
        return ResponseEntity.ok(ApiResponse.<List<Category>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(categoryRepo.findAll())
                .build());
    }

    public ResponseEntity<ApiResponse<Category>> addCategory(Category category) {
        Category addCategory = new Category();
        addCategory.setName(category.getName());
        return ResponseEntity.ok(ApiResponse.<Category>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Thêm dữ liệu thành công")
                .data(categoryRepo.save(addCategory))
                .build());
    }

    public ResponseEntity<ApiResponse<Category>> editCategory(Long id, Category category) throws ResourceNotFoundException {
        Category editCategory = categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id:" + id));
        editCategory.setName(category.getName());
        editCategory.setId(id);
        return ResponseEntity.ok(ApiResponse.<Category>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Sửa dữ liệu thành công")
                .data(categoryRepo.save(editCategory))
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> deleteCategory(Long id) throws ResourceNotFoundException {
        Category deleteCategory = categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id:" + id));
        categoryRepo.delete(deleteCategory);
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Xóa dữ liệu thành công")
                .data(null)
                .build());
    }

}
