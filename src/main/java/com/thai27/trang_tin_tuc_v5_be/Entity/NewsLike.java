package com.thai27.trang_tin_tuc_v5_be.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(
    name = "news_like",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "news_id"})
    }
)
@Data
public class NewsLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user like
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private TrangTinTucUser user;

    // bài báo được like
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @Column(name = "liked_at", updatable = false)
    private Instant likedAt = Instant.now();
}
