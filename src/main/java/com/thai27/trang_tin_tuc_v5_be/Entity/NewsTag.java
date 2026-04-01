package com.thai27.trang_tin_tuc_v5_be.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "news_tag")
public class NewsTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name")
    private String tagName;

    @ManyToMany(targetEntity = News.class, fetch = FetchType.LAZY)
    private List<News> news;

}
