package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.CategoryRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.CategoryResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Mapper.CategoryResponseMapper;
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
    private final CategoryResponseMapper categoryResponseMapper;

    public Page<CategoryResponse> searchAllCategory(String search, int pageNum, int pageSize) {
        PageRequest searchCategoryPaging = PageRequest.of(pageNum, pageSize);
        String searchLike = "%" + search + "%";
        return categoryRepo
                .findAllByNameLikeIgnoreCaseOrderById(searchLike, searchCategoryPaging)
                .map(categoryResponseMapper::mapToDto);
    }

    public List<CategoryResponse> getAllCategory() {
        return categoryRepo.findAll().stream().map(categoryResponseMapper::mapToDto).toList();
    }

    public CategoryResponse addCategory(CategoryRequest category) {
        Category addCategory = new Category();
        addCategory.setName(category.getName());
        return categoryResponseMapper.mapToDto(categoryRepo.save(addCategory));
    }

    public CategoryResponse editCategory(Long id, CategoryRequest category) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        Category editCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi ID:" + id));
        editCategory.setName(category.getName());
        editCategory.setId(id);
        return categoryResponseMapper.mapToDto(categoryRepo.save(editCategory));
    }

    public CategoryResponse getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi ID:" + id));
        return categoryResponseMapper.mapToDto(category);
    }

    public void deleteCategory(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        Category deleteCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi ID:" + id));
        categoryRepo.delete(deleteCategory);
    }

}
