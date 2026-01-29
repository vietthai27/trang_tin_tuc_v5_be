package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.NewsCreateRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.GetNewsByIdResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsListDTO;
import com.thai27.trang_tin_tuc_v5_be.Entity.*;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.ImageKitRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.SubCategoryRepo;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsService {

    private final NewsRepo newsRepository;
    private final SubCategoryRepo subCategoryRepo;
    private final ImageKitRepo imageKitRepo;
    private final ImageKitService imageKitService;

    public ResponseEntity<ApiResponse<Object>> create(NewsCreateRequest request, String username) {

        SubCategory subCategory = subCategoryRepo.findById(request.subCategoryId())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        News news = new News();
        news.setTitle(request.title());
        news.setDescription(request.description());
        news.setContent(request.content());
        news.setSubCategory(subCategory);
        news.setCreatedAt(Instant.now());
        news.setWriter(username);
        List<String> imagesUrl = request.imagesUrl();
        news.setThumbnail(imagesUrl.get(0));
        newsRepository.save(news);
        if (!imagesUrl.isEmpty()) {
            for (String url : imagesUrl) {
                ImageKit image = imageKitRepo.findByUrl(url);
                image.setNews(news);
                imageKitRepo.save(image);
            }
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Thêm bài báo thành công")
                .data(null)
                .build());
    }

    public ResponseEntity<ApiResponse<Object>> edit(Long id, NewsCreateRequest request) throws ResourceNotFoundException {
        News news = newsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Không tìm thấy bài báo với id: " + id));

        SubCategory subCategory = subCategoryRepo.findById(request.subCategoryId())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        news.setTitle(request.title());
        news.setDescription(request.description());
        news.setContent(request.content());
        news.setSubCategory(subCategory);
        newsRepository.save(news);


        List<String> imagesUrl = request.imagesUrl();
        for (String url : imagesUrl) {
            ImageKit image = imageKitRepo.findByUrl(url);
            image.setNews(news);
            imageKitRepo.save(image);
        }

        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Sửa bài báo thành công")
                .data(null)
                .build());
    }


    public ResponseEntity<ApiResponse<Page<NewsListDTO>>> searchAllNews(String title, int pageNum, int pageSize) {
        PageRequest searchNewsPaging = PageRequest.of(pageNum, pageSize);
        return ResponseEntity.ok(ApiResponse.<Page<NewsListDTO>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(newsRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title, searchNewsPaging))
                .build());
    }

    public ResponseEntity<ApiResponse<List<NewsListDTO>>> getLatestNews() {
        return ResponseEntity.ok(ApiResponse.<List<NewsListDTO>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(newsRepository.findTop5ByOrderByIdDesc())
                .build());
    }

    public ResponseEntity<ApiResponse<GetNewsByIdResponse>> getById(Long id) throws ResourceNotFoundException {
        News news = newsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Không tìm thấy bài báo với id: " + id));
        List<ImageKit> listNewsImages = imageKitRepo.findByNewsId(id);
        GetNewsByIdResponse getNewsByIdResponse = new GetNewsByIdResponse();
        getNewsByIdResponse.setNews(news);
        getNewsByIdResponse.setListImage(listNewsImages);
        getNewsByIdResponse.setSubCategoryId(news.getSubCategory().getId());
        getNewsByIdResponse.setCategoryId(news.getSubCategory().getCategory().getId());
        return ResponseEntity.ok(ApiResponse.<GetNewsByIdResponse>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(getNewsByIdResponse)
                .build());
    }

    public ResponseEntity<ApiResponse<News>> getNewsDetail(Long id) throws ResourceNotFoundException {
        News news = newsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Không tìm thấy bài báo với id: " + id));

        return ResponseEntity.ok(ApiResponse.<News>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(news)
                .build());
    }


    public ResponseEntity<ApiResponse<Object>> deleteNews(Long id) throws ResourceNotFoundException {
        News news = newsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Không tìm thấy bài báo với id: " + id));

        List<ImageKit> listNewsImages = imageKitRepo.findByNewsId(id);
        for (ImageKit imageInfo : listNewsImages) {
            imageKitService.deleteImage(imageInfo.getUrl());
        }
        newsRepository.delete(news);
        return ResponseEntity.ok(ApiResponse.builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Xóa bài báo thành công")
                .data(null)
                .build());
    }

}

