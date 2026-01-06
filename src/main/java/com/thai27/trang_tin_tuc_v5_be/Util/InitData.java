package com.thai27.trang_tin_tuc_v5_be.Util;

import com.thai27.trang_tin_tuc_v5_be.Entity.Role;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Repository.RoleRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitData {

    @Autowired
    TrangTinTucUserRepo trangTinTucUserRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    PasswordEncoder encoder;

    @PostConstruct
    private void createData() {
        if (trangTinTucUserRepo.findByUsername(Constant.ADMIN_USERNAME).isEmpty()) {

            Role adminRole = roleRepo.findByRolename(Constant.ROLE_ADMIN)
                    .orElseGet(() -> roleRepo.save(new Role(Constant.ROLE_ADMIN)));

            roleRepo.findByRolename(Constant.ROLE_MODER)
                    .orElseGet(() -> roleRepo.save(new Role(Constant.ROLE_MODER)));

            roleRepo.findByRolename(Constant.ROLE_USER)
                    .orElseGet(() -> roleRepo.save(new Role(Constant.ROLE_USER)));

            TrangTinTucUser initAdmin = new TrangTinTucUser();
            initAdmin.setUsername(Constant.ADMIN_USERNAME);
            initAdmin.setPassword(encoder.encode(Constant.ADMIN_USERNAME));
            initAdmin.setRoles(List.of(adminRole));

            trangTinTucUserRepo.save(initAdmin);

        }
    }

}
