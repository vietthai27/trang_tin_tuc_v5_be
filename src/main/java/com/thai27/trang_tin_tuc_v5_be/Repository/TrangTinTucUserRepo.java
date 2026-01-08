package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrangTinTucUserRepo extends JpaRepository<TrangTinTucUser, Long>{

	Optional<TrangTinTucUser> findByUsername(String username);

	Optional<TrangTinTucUser> findByEmail(String username);

	Page<TrangTinTucUser> findAllByUsernameLikeIgnoreCase(String username, PageRequest pageRequest);
	
}
