package com.thai27.trang_tin_tuc_v5_be.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "news_tag")
public class NewsTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name")
    private String tagName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

}
