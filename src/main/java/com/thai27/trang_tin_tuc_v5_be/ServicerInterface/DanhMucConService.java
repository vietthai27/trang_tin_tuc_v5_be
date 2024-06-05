package com.thai27.trang_tin_tuc_v5_be.ServicerInterface;

import com.thai27.trang_tin_tuc_v5_be.DTO.QuanLyDanhMucCon;
import com.thai27.trang_tin_tuc_v5_be.Entity.DanhMucCon;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DanhMucConService {
    List<DanhMucCon> getAllDanhMucConByIdCha(Long id);

    DanhMucCon addDanhMucCon(DanhMucCon danhMucCon, Long idCha);

    DanhMucCon editDanhMucCon(Long id, DanhMucCon danhMucCon, Long newId) throws ResourceNotFoundException;

    void deleteDanhMucCon(Long id);

    Page<QuanLyDanhMucCon> getDanhMucConByIdCha(int pageNum, int pageSize, Long idCha);

    Page<QuanLyDanhMucCon> searchDanhMucConByIdCha( String search, int pageNum, int pageSize,Long idCha);
}
