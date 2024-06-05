package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.DTO.BaiBaoByDanhMucCon;
import com.thai27.trang_tin_tuc_v5_be.DTO.BaiBaoDetail;
import com.thai27.trang_tin_tuc_v5_be.Entity.BaiBao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiBaoRepo extends JpaRepository<BaiBao,Long> {

    List<BaiBao> findByOrderByNgayDangDesc();

    @Query(value = "select \n" +
            "a.id as id, \n" +
            "a.luot_xem as luotXem,\n" +
            "a.ngay_dang as ngayDang, \n" +
            "a.noidung as noiDung, \n" +
            "a.ten_bai_bao as tenBaiBao,\n" +
            "a.thumbnail as thumbnail,\n" +
            "a.tieu_de as tieuDe,\n" +
            "a.tac_gia as tacGia,\n" +
            "a.danh_muc_con_id as danhMucConId,\n" +
            "b.danh_muc_bai_bao_id as danhMucChaId,\n" +
            "b.ten_danh_muc_con as tenDanhMucCon,\n" +
            "c.ten_danh_muc as tenDanhMucCha\n" +
            "from \n" +
            "bai_bao a, danh_muc_con b, danh_muc c\n" +
            "where \n" +
            "a.danh_muc_con_id = b.id and \n" +
            "b.danh_muc_bai_bao_id = c.id and \n" +
            "LOWER(a.ten_bai_bao)  LIKE LOWER (concat('%', :tenBaiBao,'%')) ",nativeQuery = true)
    Page<BaiBaoDetail> searchAllBaiBao(@Param("tenBaiBao")String tenBaiBao, PageRequest pageRequest);

    @Query(value = "select \n" +
            "a.id as id, \n" +
            "a.luot_xem as luotXem,\n" +
            "a.ngay_dang as ngayDang, \n" +
            "a.noidung as noiDung, \n" +
            "a.ten_bai_bao as tenBaiBao,\n" +
            "a.thumbnail as thumbnail,\n" +
            "a.tieu_de as tieuDe,\n" +
            "a.tac_gia as tacGia,\n" +
            "a.danh_muc_con_id as danhMucConId,\n" +
            "b.danh_muc_bai_bao_id as danhMucChaId,\n" +
            "b.ten_danh_muc_con as tenDanhMucCon,\n" +
            "c.ten_danh_muc as tenDanhMucCha\n" +
            "from \n" +
            "bai_bao a, danh_muc_con b, danh_muc c\n" +
            "where \n" +
            "a.danh_muc_con_id = b.id and \n" +
            "b.danh_muc_bai_bao_id = c.id ",nativeQuery = true)
    Page<BaiBaoDetail> getAllBaiBao(PageRequest pageRequest);


    @Query(value = "select \n" +
            "a.id as id, \n" +
            "a.luot_xem as luotXem,\n" +
            "a.ngay_dang as ngayDang, \n" +
            "a.noidung as noiDung, \n" +
            "a.ten_bai_bao as tenBaiBao,\n" +
            "a.thumbnail as thumbnail,\n" +
            "a.tieu_de as tieuDe,\n" +
            "a.tac_gia as tacGia,\n" +
            "a.danh_muc_con_id as danhMucConId,\n" +
            "b.danh_muc_bai_bao_id as danhMucChaId,\n" +
            "b.ten_danh_muc_con as tenDanhMucCon,\n" +
            "c.ten_danh_muc as tenDanhMucCha\n" +
            "from \n" +
            "bai_bao a, danh_muc_con b, danh_muc c\n" +
            "where \n" +
            "a.danh_muc_con_id = b.id and \n" +
            "b.danh_muc_bai_bao_id = c.id and\n" +
            "a.id = :id",nativeQuery = true)
    BaiBaoDetail getBaiBaoDetailById(@Param("id")Long id);

    @Query(value = "select \n" +
            " \ta.id as id, a.luot_xem as luotXem, a.ngay_dang as ngayDang, a.ten_bai_bao as tenBaiBao, a.thumbnail as thumbnail, a.tac_gia as tacGia, a.danh_muc_con_id as danhMucConId,\n" +
            "\tb.ten_danh_muc_con as tenDanhMucCon\n" +
            "from \n" +
            " \tbai_bao a,\n" +
            "\tdanh_muc_con b\n" +
            "where \n" +
            "\ta.danh_muc_con_id = b.id and\n" +
            "\tb.id = :id ",nativeQuery = true)
    Page<BaiBaoByDanhMucCon> getAllBaiBaoByDanhMucCon(PageRequest pageRequest, @Param("id")Long id);
}
