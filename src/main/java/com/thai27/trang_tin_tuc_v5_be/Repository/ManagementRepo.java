package com.thai27.trang_tin_tuc_v5_be.Repository;

import com.thai27.trang_tin_tuc_v5_be.Entity.Management;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementRepo extends JpaRepository<Management, Long> {

    Page<Management> findAllByNameLikeIgnoreCaseOrderById(String search, PageRequest pageRequest);

}
