package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.Entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepo extends JpaRepository<Like, Long> {
    List<Like> findByBaiBaoId(Long baiBaoId);
}
