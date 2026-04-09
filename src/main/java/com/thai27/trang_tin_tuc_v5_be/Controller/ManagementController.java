package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.ManagementRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.ManagementResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.RoleResponse;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.ManagementService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managements")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ManagementResponse>>> searchManagement(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(ApiResponse.<Page<ManagementResponse>>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(managementService.searchAllManagement(search, pageNum, pageSize))
                .build());
    }

    @GetMapping("/get-all-role")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRole() {
        return ResponseEntity.ok(ApiResponse.<List<RoleResponse>>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(managementService.getAllRole())
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ManagementResponse>>> getAllManagement(@RequestParam String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<List<ManagementResponse>>builder()
                .message("Láº¥y dá»¯ liá»‡u quáº£n lÃ½ thÃ nh cÃ´ng")
                .data(managementService.getAllManagement(username))
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ManagementResponse>> addManagement(
            @RequestBody ManagementRequest management,
            @RequestParam(required = false) List<Long> roleIds
    ) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ManagementResponse>builder()
                .message("ThÃªm quáº£n lÃ½ thÃ nh cÃ´ng")
                .data(managementService.addManagement(management, roleIds))
                .build());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<ManagementResponse>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<ManagementResponse>builder()
                .message("Láº¥y dá»¯ liá»‡u thÃ nh cÃ´ng")
                .data(managementService.getById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ManagementResponse>> editManagement(
            @PathVariable Long id,
            @RequestBody ManagementRequest management,
            @RequestParam(required = false) List<Long> roleIds
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.<ManagementResponse>builder()
                .message("Sá»­a quáº£n lÃ½ thÃ nh cÃ´ng")
                .data(managementService.editManagement(id, management, roleIds))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteManagement(@PathVariable Long id) throws ResourceNotFoundException {
        managementService.deleteManagement(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("XÃ³a thÃ nh cÃ´ng")
                .data(null)
                .build());
    }
}
