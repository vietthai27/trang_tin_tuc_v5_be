package com.thai27.trang_tin_tuc_v5_be.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"comments", "likes", "images", "subCategory", "tags"})
@EqualsAndHashCode(exclude = {"comments", "likes", "images", "subCategory", "tags"})
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String writer;

    @Column
    private String thumbnail;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ImageKit> images = new ArrayList<>();

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<NewsLike> likes;

    @OneToMany(mappedBy = "news", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    @ManyToMany(targetEntity = NewsTag.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<NewsTag> newsTags;
}
