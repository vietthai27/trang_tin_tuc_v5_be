package com.thai27.trang_tin_tuc_v5_be.ServiceImplement;

import com.thai27.trang_tin_tuc_v5_be.Controller.NotificationController;
import com.thai27.trang_tin_tuc_v5_be.DTO.BaiBaoByDanhMucCon;
import com.thai27.trang_tin_tuc_v5_be.DTO.BaiBaoDetail;
import com.thai27.trang_tin_tuc_v5_be.Entity.BaiBao;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.BaiBaoRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.DanhMucConRepo;
import com.thai27.trang_tin_tuc_v5_be.ServicerInterface.BaiBaoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class BaiBaoSrvImp implements BaiBaoService {

    @Autowired
    BaiBaoRepo baiBaoRepo;

    @Autowired
    DanhMucConRepo danhMucConRepo;

    @Autowired
    NotificationController notificationController;

    @Override
    public Optional<BaiBao> getBaiBaoById(Long id) throws ResourceNotFoundException {
        if (!baiBaoRepo.existsById(id)) throw new ResourceNotFoundException("Không tìm thấy bài báo với id: " + id);
        else return baiBaoRepo.findById(id);
    }

    @Override
    public BaiBaoDetail getBaiBaoDetailById(Long id) throws ResourceNotFoundException {
        if (!baiBaoRepo.existsById(id)) throw new ResourceNotFoundException("Không tìm thấy bài báo với id: " + id);
        else
            return baiBaoRepo.getBaiBaoDetailById(id);
    }

    @Override
    public BaiBao addBaiBao(BaiBao baiBao, Long idDanhMucCon) {
        BaiBao addBaiBao = new BaiBao();
        addBaiBao.setThumbnail(baiBao.getThumbnail());
        addBaiBao.setTenBaiBao(baiBao.getTenBaiBao());
        addBaiBao.setTieuDe(baiBao.getTieuDe());
        addBaiBao.setNoiDung(baiBao.getNoiDung());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        addBaiBao.setNgayDang(dtf.format(now));
        addBaiBao.setTacGia(baiBao.getTacGia());
        addBaiBao.setLuotXem(0);
        addBaiBao.setDanhMucCon(danhMucConRepo.getReferenceById(idDanhMucCon));

        notificationController.sendNotification(
                new NotificationMessage("ARTICLE_ADDED", "Bài báo " + baiBao.getTenBaiBao() + " vừa được thêm")
        );
        return baiBaoRepo.save(addBaiBao);
    }

    @Data
    @AllArgsConstructor
    public class NotificationMessage {
        private String type;
        private String content;
    }

    @Override
    public BaiBao editBaiBao(Long id, Long idCon, BaiBao baiBao) throws ResourceNotFoundException {
        if (!baiBaoRepo.existsById(id)) throw new ResourceNotFoundException("Không tìm thấy bài báo với id: " + id);
        else {
            BaiBao editBaiBao = baiBaoRepo.findById(id).orElse(null);
            editBaiBao.setThumbnail(baiBao.getThumbnail());
            editBaiBao.setTenBaiBao(baiBao.getTenBaiBao());
            editBaiBao.setTieuDe(baiBao.getTieuDe());
            editBaiBao.setNoiDung(baiBao.getNoiDung());
            editBaiBao.setTacGia(baiBao.getTacGia());
            editBaiBao.setDanhMucCon(danhMucConRepo.getReferenceById(idCon));
            return baiBaoRepo.save(editBaiBao);
        }
    }

    @Override
    public void deleteBaiBao(Long id) {
        baiBaoRepo.deleteById(id);
    }

    @Override
    public List<BaiBao> findByOrderByNgayDangDesc() {
        return baiBaoRepo.findByOrderByNgayDangDesc();
    }

    @Override
    public Page<BaiBaoDetail> getAllBaiBao(Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        return baiBaoRepo.getAllBaiBao(pageRequest);
    }

    @Override
    public Page<BaiBaoByDanhMucCon> getAllBaiBaoByDanhMucConId(Integer pageNum, Integer pageSize, Long id) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        return baiBaoRepo.getAllBaiBaoByDanhMucCon(pageRequest, id);
    }

    @Override
    public Page<BaiBaoDetail> searchAllBaiBao(String tenBaiBao, Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        return baiBaoRepo.searchAllBaiBao(tenBaiBao, pageRequest);
    }

    @Override
    public void addViewBaiBao(Long id) {
        Optional<BaiBao> baiBao = baiBaoRepo.findById(id);
        if (baiBao.isPresent()) {
            BaiBao baiBaoData = baiBao.get();
            baiBaoData.setLuotXem(baiBaoData.getLuotXem() + 1);
            baiBaoRepo.save(baiBaoData);
        }
    }


}
