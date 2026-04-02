package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.Entity.Management;
import com.thai27.trang_tin_tuc_v5_be.Entity.Role;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.ManagementRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.RoleRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final ManagementRepo managementRepo;

    private final RoleRepo roleRepo;

    private final TrangTinTucUserRepo trangTinTucUserRepo;

    public ResponseEntity<ApiResponse<List<Management>>> getAllManagement(String username) throws ResourceNotFoundException {

        TrangTinTucUser user = trangTinTucUserRepo
                .findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Role> roles = user.getRoles();

        List<Management> managements = roles.stream()
                .flatMap(role -> role.getManagements().stream())
                .distinct()
                .toList();

        return ResponseEntity.ok(ApiResponse.<List<Management>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu quản lý thành công")
                .data(managements)
                .build());
    }

    @Transactional
    public ResponseEntity<ApiResponse<Management>> addManagement(
            Management management,
            List<Long> listIdRole) throws ResourceNotFoundException {

        Management addManagement = new Management();
        addManagement.setName(management.getName());

        if (listIdRole != null && !listIdRole.isEmpty()) {
            List<Role> roles = roleRepo.findAllById(listIdRole);

            if (roles.size() != listIdRole.size()) {
                throw new ResourceNotFoundException("Một hoặc nhiều Role không tồn tại");
            }

            addManagement.setRolesManage(roles);
        }

        Management savedManagement = managementRepo.save(addManagement);

        return ResponseEntity.ok(
                ApiResponse.<Management>builder()
                        .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                        .message("Thêm quản lý thành công")
                        .data(savedManagement)
                        .build()
        );
    }

    public ResponseEntity<ApiResponse<Management>> getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        Management management = managementRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Không tìm thấy quản lý với id: " + id));

        return ResponseEntity.ok(
                ApiResponse.<Management>builder()
                        .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                        .message("Lấy dữ liệu thành công")
                        .data(management)
                        .build()
        );
    }


    @Transactional
    public ResponseEntity<ApiResponse<Management>> editManagement(
            Long id,
            Management request,
            List<Long> roleIds) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        Management management = managementRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Không tìm thấy quản lý với id: " + id));

        if (request.getName() != null && !request.getName().isBlank()) {
            management.setName(request.getName());
        }

        if (roleIds != null) {
            if (roleIds.isEmpty()) {
                management.getRolesManage().clear();
            } else {
                List<Role> roles = roleRepo.findAllById(roleIds);

                if (roles.size() != roleIds.size()) {
                    throw new ResourceNotFoundException("Một hoặc nhiều Role không tồn tại");
                }

                management.setRolesManage(roles);
            }
        }

        if (management == null) {
            throw new ResourceNotFoundException("Không tìm thấy quản lý");
        }
        Management savedManagement = managementRepo.save(management);

        return ResponseEntity.ok(
                ApiResponse.<Management>builder()
                        .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                        .message("Sửa quản lý thành công")
                        .data(savedManagement)
                        .build()
        );
    }

    public ResponseEntity<ApiResponse<Object>> deleteManagement(
            Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        Management management = managementRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Không tìm thấy quản lý với id: " + id));
        management.getRolesManage().clear();
        managementRepo.delete(management);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                        .message("Xóa thành công")
                        .data(null)
                        .build()
        );
    }

    public ResponseEntity<ApiResponse<Page<Management>>> searchAllManagement(String search, int pageNum, int pageSize) {
        PageRequest searchManagementPaging = PageRequest.of(pageNum, pageSize);
        String searchLike = "%" + search + "%";
        return ResponseEntity.ok(ApiResponse.<Page<Management>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(managementRepo.findAllByNameLikeIgnoreCaseOrderById(searchLike, searchManagementPaging))
                .build());
    }

    public ResponseEntity<ApiResponse<List<Role>>> getAllRole() {
        return ResponseEntity.ok(ApiResponse.<List<Role>>builder()
                .responseCode(Constant.RESPONSE_CODE_SUCCESS)
                .message("Lấy dữ liệu thành công")
                .data(roleRepo.findAll())
                .build());
    }

}
