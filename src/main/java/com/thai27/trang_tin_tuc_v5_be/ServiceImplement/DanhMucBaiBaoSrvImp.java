package com.thai27.trang_tin_tuc_v5_be.ServiceImplement;

import com.thai27.trang_tin_tuc_v5_be.Entity.DanhMucBaiBao;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.DanhMucBaiBaoRepo;
import com.thai27.trang_tin_tuc_v5_be.ServicerInterface.DanhMucBaiBaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanhMucBaiBaoSrvImp implements DanhMucBaiBaoService {

    @Autowired
    DanhMucBaiBaoRepo danhMucBaiBaoRepo;
    @Override
    public List<DanhMucBaiBao> getAllDanhMuc() {
        return danhMucBaiBaoRepo.findAll();
    }

    @Override
    public Page<DanhMucBaiBao> searchAllDanhMuc(String search, int pageNum, int pageSize) {
        PageRequest searchDanhMucPaging = PageRequest.of(pageNum,pageSize);
        String searchLike = "%" + search + "%";
        return danhMucBaiBaoRepo.findAllByTenDanhMucLikeIgnoreCase(searchLike,searchDanhMucPaging);
    }

    @Override
    public Page<DanhMucBaiBao> getAllDanhMucPaging(int pageNum, int pageSize) {
        PageRequest getAllDanhMucPaging = PageRequest.of(pageNum,pageSize);
        return danhMucBaiBaoRepo.findAll(getAllDanhMucPaging);
    }

    @Override
    public DanhMucBaiBao addDanhMuc(DanhMucBaiBao danhMucBaiBao) {
        DanhMucBaiBao addDanhMucBaiBao = new DanhMucBaiBao();
        addDanhMucBaiBao.setTenDanhMuc(danhMucBaiBao.getTenDanhMuc());
        return danhMucBaiBaoRepo.save(addDanhMucBaiBao);
    }

    @Override
    public DanhMucBaiBao editDanhMuc(Long id, DanhMucBaiBao danhMucBaiBao) throws ResourceNotFoundException {
        DanhMucBaiBao editDanhMuc = danhMucBaiBaoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id:" + id));
        editDanhMuc.setTenDanhMuc(danhMucBaiBao.getTenDanhMuc());
        return danhMucBaiBaoRepo.save(editDanhMuc);
    }

}
