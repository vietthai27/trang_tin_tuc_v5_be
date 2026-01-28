package com.thai27.trang_tin_tuc_v5_be.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "image_kit")
@Data
public class ImageKit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @Column
    private String fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

}
