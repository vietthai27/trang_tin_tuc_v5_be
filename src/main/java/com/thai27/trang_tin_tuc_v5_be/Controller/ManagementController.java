package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.Entity.Management;
import com.thai27.trang_tin_tuc_v5_be.Entity.Role;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.ManagementService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managements")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Management>>> searchCategory(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return managementService.searchAllManagement(search, pageNum, pageSize);
    }

    @GetMapping("/get-all-role")
    public ResponseEntity<ApiResponse<List<Role>>> getAllRole() {
        return managementService.getAllRole();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Management>>> getAllManagement(
            @RequestParam String username
    ) throws ResourceNotFoundException {
        return managementService.getAllManagement(username);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Management>> addManagement(
            @RequestBody Management management,
            @RequestParam(required = false) List<Long> roleIds
    ) throws ResourceNotFoundException {
        return managementService.addManagement(management, roleIds);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<Management>> getById(@PathVariable Long id
    ) throws ResourceNotFoundException {
        return managementService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Management>> editManagement(
            @PathVariable Long id,
            @RequestBody Management management,
            @RequestParam(required = false) List<Long> roleIds
    ) throws ResourceNotFoundException {
        return managementService.editManagement(id, management, roleIds);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteManagement(
            @PathVariable Long id
    ) throws ResourceNotFoundException {
        return managementService.deleteManagement(id);
    }
}
