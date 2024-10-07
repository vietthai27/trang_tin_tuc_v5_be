package com.thai27.trang_tin_tuc_v5_be.ServiceImplement;

import com.thai27.trang_tin_tuc_v5_be.Entity.Like;
import com.thai27.trang_tin_tuc_v5_be.Repository.LikeRepo;
import com.thai27.trang_tin_tuc_v5_be.ServicerInterface.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeSrvImp implements LikeService {

    @Autowired
    LikeRepo likeRepo;
    @Override
    public int getAllLike(Long baiBaoId) {
        List<Like> likeById= likeRepo.findByBaiBaoId(baiBaoId);
        return likeById.size();
    }
}
