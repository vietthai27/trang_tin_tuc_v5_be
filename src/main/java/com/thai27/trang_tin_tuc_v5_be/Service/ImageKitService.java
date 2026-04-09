package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.ImageKitResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.ImageKit;
import com.thai27.trang_tin_tuc_v5_be.Repository.ImageKitRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageKitService {

    private final ImageKitRepo imageKitRepo;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${imagekit.private-key}")
    private String privateKey;

    public ImageKitResponse upload(MultipartFile file) throws IOException {
        String auth = Base64.getEncoder().encodeToString((privateKey + ":").getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Basic " + auth);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
        body.add("fileName", UUID.randomUUID() + "_" + file.getOriginalFilename());

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://upload.imagekit.io/api/v1/files/upload",
                request,
                Map.class
        );

        ImageKit imageKit = new ImageKit();
        imageKit.setUrl(response.getBody().get("url").toString());
        imageKit.setFileId(response.getBody().get("fileId").toString());
        imageKitRepo.save(imageKit);

        return toImageKitResponse(imageKit);
    }

    public void deleteImage(String url) {
        ImageKit image = imageKitRepo.findByUrl(url);

        String auth = Base64.getEncoder().encodeToString((privateKey + ":").getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + auth);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(
                "https://api.imagekit.io/v1/files/" + image.getFileId(),
                HttpMethod.DELETE,
                request,
                Void.class
        );

        imageKitRepo.delete(image);
    }

    public ImageKitResponse toImageKitResponse(ImageKit imageKit) {
        return new ImageKitResponse(imageKit.getId(), imageKit.getUrl(), imageKit.getFileId());
    }
}
