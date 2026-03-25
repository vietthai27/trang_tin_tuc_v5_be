package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Entity.NewsTag;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsTagRepo;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsTagService {

    private final NewsTagRepo newsTagRepo;
    private final NewsRepo newsRepo;

    public ResponseEntity<ApiResponse<NewsTag>> addNewsTag(NewsTag newsTag, Long newsId) throws ResourceNotFoundException {
        if (newsId == null) {
            throw new ResourceNotFoundException("ID tin tức không được để trống");
        }
        News news = newsRepo.findById(newsId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tin tức với id: " + newsId));
        
        NewsTag addNewsTag = new NewsTag();
        addNewsTag.setTagName(newsTag.getTagName());
        addNewsTag.setNews(news);
        
        return ResponseEntity.ok(ApiResponse.<NewsTag>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Thêm dữ liệu thành công")
                .data(newsTagRepo.save(addNewsTag))
                .build());
    }

    public ResponseEntity<ApiResponse<NewsTag>> editNewsTag(Long id, NewsTag newsTag, Long newsId) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        NewsTag editNewsTag = newsTagRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tag với id: " + id));
        
        if (newsTag.getTagName() != null && !newsTag.getTagName().isBlank()) {
            editNewsTag.setTagName(newsTag.getTagName());
        }
        
        if (newsId != null) {
            News news = newsRepo.findById(newsId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tin tức với id: " + newsId));
            editNewsTag.setNews(news);
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
}
