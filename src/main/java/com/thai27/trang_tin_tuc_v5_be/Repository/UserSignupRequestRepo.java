package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSignupRequestRepo extends JpaRepository<UserSignupRequest, Long> {
    Optional<UserSignupRequest> findByRequestCode(String validateCode);
    @Query(value = "SELECT request_code\n" +
            "FROM user_signup_request where email = :email and request_time = ( \n" +
            "SELECT \n" +
            "MAX(request_time) from user_signup_request)",nativeQuery = true)
    String getCodeByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    void deleteAllByEmail (String email);
}
