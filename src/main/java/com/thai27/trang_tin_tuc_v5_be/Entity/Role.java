package com.thai27.trang_tin_tuc_v5_be.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(targetEntity = TrangTinTucUser.class, mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TrangTinTucUser> trangTinTucUsers;

    @ManyToMany(targetEntity = Management.class, mappedBy = "rolesManage", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Management> managements;

    public Role (String roleName){
        this.roleName = roleName;
    }
}