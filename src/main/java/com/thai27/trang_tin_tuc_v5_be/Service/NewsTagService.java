package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsTagDTO;
import com.thai27.trang_tin_tuc_v5_be.Entity.Category;
import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Entity.NewsTag;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsTagRepo;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsTagService {

    private final NewsTagRepo newsTagRepo;
    private final NewsRepo newsRepo;

    public ResponseEntity<ApiResponse<NewsTag>> addNewsTag(NewsTag newsTag) throws ResourceNotFoundException {

        NewsTag addNewsTag = new NewsTag();
        addNewsTag.setTagName(newsTag.getTagName());

        return ResponseEntity.ok(ApiResponse.<NewsTag>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Thêm dữ liệu thành công")
                .data(newsTagRepo.save(addNewsTag))
                .build());
    }

    public ResponseEntity<ApiResponse<NewsTag>> editNewsTag(Long id, NewsTag newsTag) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        NewsTag editNewsTag = newsTagRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tag với id: " + id));

        if (newsTag.getTagName() != null && !newsTag.getTagName().isBlank()) {
            editNewsTag.setTagName(newsTag.getTagName());
        }

        editNewsTag.setId(id);
        return ResponseEntity.ok(ApiResponse.<NewsTag>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Sửa dữ liệu thành công")
                .data(newsTagRepo.save(editNewsTag))
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> deleteNewsTag(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        NewsTag deleteNewsTag = newsTagRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tag với id: " + id));
        if (deleteNewsTag == null) {
            throw new ResourceNotFoundException("Không tìm thấy tag");
        }
        newsTagRepo.delete(deleteNewsTag);
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Xóa dữ liệu thành công")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<NewsTag>> getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        NewsTag newsTag = newsTagRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tag với id: " + id));
        return ResponseEntity.ok(ApiResponse.<NewsTag>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(newsTag)
                .build());
    }

    public ResponseEntity<ApiResponse<List<NewsTag>>> getAllNewsTag() {
        return ResponseEntity.ok(ApiResponse.<List<NewsTag>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(newsTagRepo.findAll())
                .build());
    }

    public ResponseEntity<ApiResponse<List<NewsTag>>> getNewsTagsByNewsId(Long newsId) throws ResourceNotFoundException {
        if (newsId == null) {
            throw new ResourceNotFoundException("ID tin tức không được để trống");
        }
        News news = newsRepo.findById(newsId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tin tức với id: " + newsId));
        List<NewsTag> newsTags = newsTagRepo.findByNews(news);
        return ResponseEntity.ok(ApiResponse.<List<NewsTag>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(newsTags)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> addTagToNews(Long newsId, List<Long> tagIds)
            throws ResourceNotFoundException {

        // 1. Check news tồn tại
        News news = newsRepo.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy tin tức với id: " + newsId));

        // 2. Lấy tags
        List<NewsTag> tags = newsTagRepo.findAllById(tagIds);

        if (tags.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy tag nào");
        }

        // 3. Lấy danh sách tag đã tồn tại của news (để tránh duplicate)
        List<NewsTag> existingNewsTags = newsTagRepo.findByNewsId(newsId);

        Set<Long> existingTagIds = existingNewsTags.stream()
                .map(nt -> nt.getTag().getId())
                .collect(Collectors.toSet());

        // 4. Tạo mới NewsTag nếu chưa tồn tại
        List<NewsTag> newNewsTags = tags.stream()
                .filter(tag -> !existingTagIds.contains(tag.getId()))
                .map(tag -> new NewsTag(news, tag))
                .toList();

        // 5. Save
        if (!newNewsTags.isEmpty()) {
            newsTagRepo.saveAll(newNewsTags);
        }

        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Thêm tag cho bài báo thành công")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> deleteTagToNews(Long newsId, List<Long> tagIds) {
        newsTagRepo.deleteByNewsIdAndTagIds(newsId, tagIds);
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Xóa tag cho bài báo thành công")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Page<NewsTagDTO>>> searchNewsTag(String search, int pageNum, int pageSize) {
        PageRequest searchCategoryPaging = PageRequest.of(pageNum, pageSize);
        String searchLike = "%" + search + "%";
        return ResponseEntity.ok(ApiResponse.<Page<NewsTagDTO>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(newsTagRepo.findAllByTagNameLikeIgnoreCaseOrderById(searchLike, searchCategoryPaging))
                .build());
    }
}
