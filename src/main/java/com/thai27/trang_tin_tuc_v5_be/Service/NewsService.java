package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.NewsCreateRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.GetNewsByIdResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsListDTO;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.NewsResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.ImageKit;
import com.thai27.trang_tin_tuc_v5_be.Entity.News;
import com.thai27.trang_tin_tuc_v5_be.Entity.SubCategory;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.ImageKitRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.NewsRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.SubCategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public void create(NewsCreateRequest request, String username) {
        SubCategory subCategory = subCategoryRepo.findById(request.subCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Danh má»¥c con khÃ´ng tá»“n táº¡i"));

        News news = new News();
        news.setTitle(request.title());
        news.setDescription(request.description());
        news.setContent(request.content());
        news.setSubCategory(subCategory);
        news.setCreatedAt(Instant.now());
        news.setWriter(username);
        newsRepository.save(news);

        List<String> imagesUrl = request.imagesUrl();
        if (imagesUrl != null && !imagesUrl.isEmpty()) {
            news.setThumbnail(imagesUrl.getFirst());
            newsRepository.save(news);
            for (String url : imagesUrl) {
                ImageKit image = imageKitRepo.findByUrl(url);
                image.setNews(news);
                imageKitRepo.save(image);
            }
        }
    }

    public void edit(Long id, NewsCreateRequest request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y bÃ i bÃ¡o vá»›i id: " + id));

        SubCategory subCategory = subCategoryRepo.findById(request.subCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Danh má»¥c con khÃ´ng tá»“n táº¡i"));

        news.setTitle(request.title());
        news.setDescription(request.description());
        news.setContent(request.content());
        news.setSubCategory(subCategory);
        newsRepository.save(news);

        List<String> imagesUrl = request.imagesUrl();
        if (imagesUrl != null) {
            for (String url : imagesUrl) {
                ImageKit image = imageKitRepo.findByUrl(url);
                image.setNews(news);
                imageKitRepo.save(image);
            }
        }
    }

    public Page<NewsListDTO> searchAllNews(String title, int pageNum, int pageSize) {
        PageRequest searchNewsPaging = PageRequest.of(pageNum, pageSize);
        return newsRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title, searchNewsPaging);
    }

    public Page<NewsListDTO> getNewsBySubCategory(String title, Long categoryId, int pageNum, int pageSize) {
        PageRequest searchNewsPaging = PageRequest.of(pageNum, pageSize);
        return newsRepository.findByTitleContainingIgnoreCaseAndSubCategory_IdOrderByCreatedAtDesc(title, categoryId, searchNewsPaging);
    }

    public List<NewsListDTO> getLatestNews() {
        return newsRepository.findTop5ByOrderByIdDesc();
    }

    public GetNewsByIdResponse getById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y bÃ i bÃ¡o vá»›i id: " + id));
        List<ImageKit> listNewsImages = imageKitRepo.findByNewsId(id);
        GetNewsByIdResponse getNewsByIdResponse = new GetNewsByIdResponse();
        getNewsByIdResponse.setNews(toNewsResponse(news));
        getNewsByIdResponse.setListImage(listNewsImages.stream().map(imageKitService::toImageKitResponse).toList());
        getNewsByIdResponse.setSubCategoryId(news.getSubCategory().getId());
        getNewsByIdResponse.setCategoryId(news.getSubCategory().getCategory().getId());
        return getNewsByIdResponse;
    }

    public NewsResponse getNewsDetail(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y bÃ i bÃ¡o vá»›i id: " + id));
        return toNewsResponse(news);
    }

    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y bÃ i bÃ¡o vá»›i id: " + id));

        List<ImageKit> listNewsImages = imageKitRepo.findByNewsId(id);
        for (ImageKit imageInfo : listNewsImages) {
            imageKitService.deleteImage(imageInfo.getUrl());
        }
        newsRepository.delete(news);
    }

    private NewsResponse toNewsResponse(News news) {
        return new NewsResponse(
                news.getId(),
                news.getTitle(),
                news.getDescription(),
                news.getWriter(),
                news.getThumbnail(),
                news.getContent(),
                news.getCreatedAt(),
                news.getSubCategory() != null ? news.getSubCategory().getId() : null,
                news.getSubCategory() != null ? news.getSubCategory().getName() : null,
                news.getSubCategory() != null && news.getSubCategory().getCategory() != null ? news.getSubCategory().getCategory().getId() : null,
                news.getSubCategory() != null && news.getSubCategory().getCategory() != null ? news.getSubCategory().getCategory().getName() : null
        );
    }
}
