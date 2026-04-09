package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.CategoryRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CategoryResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public Page<CategoryResponse> searchAllCategory(String search, int pageNum, int pageSize) {
        PageRequest searchCategoryPaging = PageRequest.of(pageNum, pageSize);
        String searchLike = "%" + search + "%";
        return categoryRepo
                .findAllByNameLikeIgnoreCaseOrderById(searchLike, searchCategoryPaging)
                .map(this::toCategoryResponse);
    }

    public List<CategoryResponse> getAllCategory() {
        return categoryRepo.findAll().stream().map(this::toCategoryResponse).toList();
    }

    public CategoryResponse addCategory(CategoryRequest category) {
        Category addCategory = new Category();
        addCategory.setName(category.getName());
        addCategory.setIcon(category.getIcon());
        return toCategoryResponse(categoryRepo.save(addCategory));
    }

    public CategoryResponse editCategory(Long id, CategoryRequest category) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng");
        }
        Category editCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y danh má»¥c vá»›i id:" + id));
        editCategory.setName(category.getName());
        editCategory.setIcon(category.getIcon());
        editCategory.setId(id);
        return toCategoryResponse(categoryRepo.save(editCategory));
    }

    public CategoryResponse getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng");
        }
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y danh má»¥c vá»›i id:" + id));
        return toCategoryResponse(category);
    }

    public void deleteCategory(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng");
        }
        Category deleteCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y danh má»¥c vá»›i id:" + id));
        categoryRepo.delete(deleteCategory);
    }

    private CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getIcon());
    }
}
