package com.thai27.trang_tin_tuc_v5_be.ServicerInterface;

import com.thai27.trang_tin_tuc_v5_be.DTO.BaiBaoByDanhMucCon;
import com.thai27.trang_tin_tuc_v5_be.DTO.BaiBaoDetail;
import com.thai27.trang_tin_tuc_v5_be.Entity.BaiBao;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BaiBaoService {

    Optional<BaiBao> getBaiBaoById (Long id) throws ResourceNotFoundException;

    BaiBaoDetail getBaiBaoDetailById (Long id) throws ResourceNotFoundException;

    BaiBao addBaiBao (BaiBao baiBao, Long idDanhMucCon);

    BaiBao editBaiBao (Long id,Long idCon, BaiBao baiBao) throws ResourceNotFoundException;

    void deleteBaiBao (Long id);

    List<BaiBao> findByOrderByNgayDangDesc ();

    Page<BaiBaoDetail> getAllBaiBao(Integer pageNum, Integer pageSize);

    Page<BaiBaoByDanhMucCon> getAllBaiBaoByDanhMucConId(Integer pageNum, Integer pageSize, Long id);

    Page<BaiBaoDetail> searchAllBaiBao(String tenBaiBao, Integer pageNum, Integer pageSize);

}
