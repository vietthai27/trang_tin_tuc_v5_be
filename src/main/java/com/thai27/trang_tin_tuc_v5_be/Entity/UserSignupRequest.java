package com.thai27.trang_tin_tuc_v5_be.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_signup_request")
@Data
public class UserSignupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "requestCode")
    private String requestCode;

    @Column(name = "requestTime")
    private String requestTime;

}
