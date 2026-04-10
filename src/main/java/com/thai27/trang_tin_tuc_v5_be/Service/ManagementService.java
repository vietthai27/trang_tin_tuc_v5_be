package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.ManagementRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.ManagementResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.RoleResponse;
import com.thai27.trang_tin_tuc_v5_be.Entity.Management;
import com.thai27.trang_tin_tuc_v5_be.Entity.Role;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Repository.ManagementRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.RoleRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final ManagementRepo managementRepo;
    private final RoleRepo roleRepo;
    private final TrangTinTucUserRepo trangTinTucUserRepo;

    public List<ManagementResponse> getAllManagement(String username) throws ResourceNotFoundException {
        TrangTinTucUser user = trangTinTucUserRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        return user.getRoles().stream()
                .flatMap(role -> role.getManagements().stream())
                .distinct()
                .map(this::toManagementResponse)
                .toList();
    }

    @Transactional
    public ManagementResponse addManagement(ManagementRequest management, List<Long> listIdRole) throws ResourceNotFoundException {
        Management addManagement = new Management();
        addManagement.setName(management.getName());
        addManagement.setIcon(management.getIcon());
        addManagement.setPath(management.getPath());

        if (listIdRole != null && !listIdRole.isEmpty()) {
            List<Role> roles = roleRepo.findAllById(listIdRole);
            if (roles.size() != listIdRole.size()) {
                throw new ResourceNotFoundException("Má»™t hoáº·c nhiá»u Role khÃ´ng tá»“n táº¡i");
            }
            addManagement.setRolesManage(roles);
        }

        return toManagementResponse(managementRepo.save(addManagement));
    }

    public ManagementResponse getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        Management management = managementRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y quáº£n lÃ½ vá»›i id: " + id));
        return toManagementResponse(management);
    }

    @Transactional
    public ManagementResponse editManagement(Long id, ManagementRequest request, List<Long> roleIds) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        Management management = managementRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y quáº£n lÃ½ vá»›i id: " + id));

        if (request.getName() != null && !request.getName().isBlank()) {
            management.setName(request.getName());
        }
        if (request.getIcon() != null) {
            management.setIcon(request.getIcon());
        }
        if (request.getPath() != null) {
            management.setPath(request.getPath());
        }

        if (roleIds != null) {
            if (roleIds.isEmpty()) {
                management.getRolesManage().clear();
            } else {
                List<Role> roles = roleRepo.findAllById(roleIds);
                if (roles.size() != roleIds.size()) {
                    throw new ResourceNotFoundException("Má»™t hoáº·c nhiá»u Role khÃ´ng tá»“n táº¡i");
                }
                management.setRolesManage(roles);
            }
        }

        return toManagementResponse(managementRepo.save(management));
    }

    public void deleteManagement(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new ResourceNotFoundException("ID không được để trống");
        }
        Management management = managementRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KhÃ´ng tÃ¬m tháº¥y quáº£n lÃ½ vá»›i id: " + id));
        management.getRolesManage().clear();
        managementRepo.delete(management);
    }

    public Page<ManagementResponse> searchAllManagement(String search, int pageNum, int pageSize) {
        PageRequest searchManagementPaging = PageRequest.of(pageNum, pageSize);
        String searchLike = "%" + search + "%";
        return managementRepo
                .findAllByNameLikeIgnoreCaseOrderById(searchLike, searchManagementPaging)
                .map(this::toManagementResponse);
    }

    public List<RoleResponse> getAllRole() {
        return roleRepo.findAll().stream().map(this::toRoleResponse).toList();
    }

    private ManagementResponse toManagementResponse(Management management) {
        return new ManagementResponse(
                management.getId(),
                management.getName(),
                management.getIcon(),
                management.getPath(),
                management.getRolesManage() == null ? List.of() : management.getRolesManage().stream().map(this::toRoleResponse).toList()
        );
    }

    private RoleResponse toRoleResponse(Role role) {
        return new RoleResponse(role.getId(), role.getRoleName());
    }
}
