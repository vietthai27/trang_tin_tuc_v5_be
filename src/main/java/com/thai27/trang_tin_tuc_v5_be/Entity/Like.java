package com.thai27.trang_tin_tuc_v5_be.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "likes")
@Data
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "baibao_id")
    Long baiBaoId;
}
