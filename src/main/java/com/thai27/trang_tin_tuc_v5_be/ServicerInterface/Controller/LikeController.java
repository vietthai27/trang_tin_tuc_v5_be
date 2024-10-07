package com.thai27.trang_tin_tuc_v5_be.ServicerInterface.Controller;

import com.thai27.trang_tin_tuc_v5_be.ServiceImplement.LikeSrvImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    LikeSrvImp likeSrvImp;

    @GetMapping("/get/getLikesByBaiBaoId/{id}")
    public int getLikesByBaiBaoId(@PathVariable Long id) {
        return likeSrvImp.getAllLike(id);
    }
}
