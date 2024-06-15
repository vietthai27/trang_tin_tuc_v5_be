package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

	List<Role> findByRolename(String userRole);

	@Query(value = "INSERT INTO trangtintuc_user_roles(\n" +
			"\ttrang_tin_tuc_users_id, roles_id)\n" +
			"\tVALUES (:userId, (select id from role where role_name = 'MODER'))",nativeQuery = true)
	@Modifying
	@Transactional
	void setUserModerRole(@Param("userId") Long userId);
}
