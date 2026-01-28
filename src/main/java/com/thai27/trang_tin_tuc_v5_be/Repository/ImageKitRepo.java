package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.Entity.ImageKit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageKitRepo extends JpaRepository<ImageKit, Long> {

    ImageKit findByUrl(String url);

    List<ImageKit> findByNewsId(Long newsId);
}
