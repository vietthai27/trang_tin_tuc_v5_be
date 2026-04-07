package com.thai27.trang_tin_tuc_v5_be.Util;

import com.thai27.trang_tin_tuc_v5_be.Entity.Management;
import com.thai27.trang_tin_tuc_v5_be.Entity.Role;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Repository.ManagementRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.RoleRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {

    private final TrangTinTucUserRepo trangTinTucUserRepo;

    private final RoleRepo roleRepo;

    private final ManagementRepo managementRepo;

    private final PasswordEncoder encoder;

    @PostConstruct
    private void createData() {
        if (trangTinTucUserRepo.findByUsername(Constant.ADMIN_USERNAME).isEmpty()) {

            Role adminRole = roleRepo.findByRoleName(Constant.ROLE_ADMIN)
                    .orElseGet(() -> roleRepo.save(new Role(Constant.ROLE_ADMIN)));

            roleRepo.findByRoleName(Constant.ROLE_MODER)
                    .orElseGet(() -> roleRepo.save(new Role(Constant.ROLE_MODER)));

            roleRepo.findByRoleName(Constant.ROLE_USER)
                    .orElseGet(() -> roleRepo.save(new Role(Constant.ROLE_USER)));

            TrangTinTucUser initAdmin = new TrangTinTucUser();
            initAdmin.setUsername(Constant.ADMIN_USERNAME);
            initAdmin.setPassword(encoder.encode(Constant.ADMIN_USERNAME));
            initAdmin.setRoles(List.of(adminRole));

            trangTinTucUserRepo.save(initAdmin);

        }
        if (managementRepo.findAll().isEmpty()) {

            Role adminRole = roleRepo.findByRoleName(Constant.ROLE_ADMIN)
                    .orElseGet(() -> roleRepo.save(new Role(Constant.ROLE_ADMIN)));

            Role moderRole = roleRepo.findByRoleName(Constant.ROLE_MODER)
                    .orElseGet(() -> roleRepo.save(new Role(Constant.ROLE_MODER)));

            Management managementPage = new Management();
            managementPage.setName(Constant.MANAGEMENT_PAGE);
            managementPage.setIcon(Constant.MANAGEMENT_PAGE_ICON);
            managementPage.setRolesManage(List.of(adminRole));
            managementPage.setPath(Constant.MANAGEMENT_PAGE_PATH);
            managementRepo.save(managementPage);

            Management categoryPage = new Management();
            categoryPage.setName(Constant.CATEGORY_PAGE);
            categoryPage.setIcon(Constant.CATEGORY_PAGE_ICON);
            categoryPage.setRolesManage(List.of(adminRole, moderRole));
            categoryPage.setPath(Constant.CATEGORY_PAGE_PATH);
            managementRepo.save(categoryPage);

            Management userPage = new Management();
            userPage.setName(Constant.USER_PAGE);
            userPage.setIcon(Constant.USER_PAGE_ICON);
            userPage.setRolesManage(List.of(adminRole));
            userPage.setPath(Constant.USER_PAGE_PATH);
            managementRepo.save(userPage);

            Management newsPage = new Management();
            newsPage.setName(Constant.NEWS_PAGE);
            newsPage.setIcon(Constant.NEWS_PAGE_ICON);
            newsPage.setRolesManage(List.of(adminRole, moderRole));
            newsPage.setPath(Constant.NEWS_PAGE_PATH);
            managementRepo.save(newsPage);

        }
    }

}
