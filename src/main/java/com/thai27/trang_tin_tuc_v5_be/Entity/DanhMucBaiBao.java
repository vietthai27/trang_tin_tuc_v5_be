package com.thai27.trang_tin_tuc_v5_be.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "danh_muc")
@Data
public class DanhMucBaiBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ten_danh_muc",nullable = false)
    private String tenDanhMuc;


    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "danhMucBaiBao")
    private List<DanhMucCon> danhMucCon;
}
