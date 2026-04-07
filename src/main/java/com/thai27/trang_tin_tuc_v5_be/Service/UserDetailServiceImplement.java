package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Response.UserDetail;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImplement implements UserDetailsService {

    private final TrangTinTucUserRepo trangTinTucUserRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TrangTinTucUser userInfo = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Tài khoản " + username + " không tồn tại trong hệ thống"
                        )
                );
        return UserDetail.build(userInfo);
    }

}
