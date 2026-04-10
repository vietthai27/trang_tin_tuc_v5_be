package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.SubCategoryRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CategoryNewResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.SubCategoryResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import com.thai27.trang_tin_tuc_v5_be.Entity.SubCategory;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.CategoryRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.SubCategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepo subCategoryRepo;
    private final CategoryRepo categoryRepo;

    public SubCategoryResponse addSubCategory(SubCategoryRequest subCategory, Long categoryId) throws ResourceNotFoundException {
        if (categoryId == null) {
            throw new ResourceNotFoundException("ID danh má»¥c khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng");
        }
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi ID: " + categoryId));

        SubCategory addSubCategory = new SubCategory();
        addSubCategory.setName(subCategory.getName());
        addSubCategory.setIcon(subCategory.getIcon());
        addSubCategory.setCategory(category);
        return toSubCategoryResponse(subCategoryRepo.save(addSubCategory));
    }

    public SubCategoryResponse editSubCategory(Long id, SubCategoryRequest subCategory, Long categoryId) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        SubCategory editSubCategory = subCategoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y danh má»¥c con vá»›i id: " + id));

        if (subCategory.getName() != null && !subCategory.getName().isBlank()) {
            editSubCategory.setName(subCategory.getName());
        }
        if (subCategory.getIcon() != null) {
            editSubCategory.setIcon(subCategory.getIcon());
        }
        if (categoryId != null) {
            Category category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi ID: " + categoryId));
            editSubCategory.setCategory(category);
        }

        editSubCategory.setId(id);
        return toSubCategoryResponse(subCategoryRepo.save(editSubCategory));
    }

    public void deleteSubCategory(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        SubCategory deleteSubCategory = subCategoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y danh má»¥c con vá»›i id: " + id));
        subCategoryRepo.delete(deleteSubCategory);
    }

    public SubCategoryResponse getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        SubCategory subCategory = subCategoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y danh má»¥c con vá»›i id: " + id));
        return toSubCategoryResponse(subCategory);
    }

    public Page<SubCategoryResponse> searchAllSubCategory(String search, Long categoryId, int pageNum, int pageSize) {
        PageRequest searchSubCategoryPaging = PageRequest.of(pageNum, pageSize);
        String searchLike = "%" + search + "%";
        return subCategoryRepo
                .findAllByNameLikeIgnoreCaseAndCategory_IdOrderById(searchLike, categoryId, searchSubCategoryPaging)
                .map(this::toSubCategoryResponse);
    }

    public CategoryNewResponse getSubCategoriesByCategoryId(Long categoryId) throws ResourceNotFoundException {
        if (categoryId == null) {
            throw new ResourceNotFoundException("ID danh má»¥c khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng");
        }
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi ID: " + categoryId));
        List<SubCategory> subCategories = subCategoryRepo.findByCategory(category);
        CategoryNewResponse categoryNewResponse = new CategoryNewResponse();
        categoryNewResponse.setCategoryName(category.getName());
        categoryNewResponse.setData(subCategories.stream().map(this::toSubCategoryResponse).toList());
        return categoryNewResponse;
    }

    private SubCategoryResponse toSubCategoryResponse(SubCategory subCategory) {
        return new SubCategoryResponse(
                subCategory.getId(),
                subCategory.getName(),
                subCategory.getIcon(),
                subCategory.getCategory() != null ? subCategory.getCategory().getId() : null,
                subCategory.getCategory() != null ? subCategory.getCategory().getName() : null
        );
    }
}
