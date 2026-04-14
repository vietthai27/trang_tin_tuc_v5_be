package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.DTO.Request.ManagementRequest;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.ManagementResponse;
import com.thai27.trang_tin_tuc_v5_be.DTO.Response.RoleResponse;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.ManagementService;
import com.thai27.trang_tin_tuc_v5_be.Service.MessageService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
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

    private final MessageService messageService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ManagementResponse>>> searchManagement(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<ManagementResponse>>builder()
                .message(messageService.getMessage("get.success"))
                .data(managementService.searchAllManagement(search, pageNum, pageSize))
                .build());
    }

    @GetMapping("/get-all-role")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRole() {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<RoleResponse>>builder()
                .message(messageService.getMessage("get.success"))
                .data(managementService.getAllRole())
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ManagementResponse>>> getAllManagement(@RequestParam String username) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<ManagementResponse>>builder()
                .message(messageService.getMessage("get.success"))
                .data(managementService.getAllManagement(username))
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ManagementResponse>> addManagement(
            @RequestBody ManagementRequest management,
            @RequestParam(required = false) List<Long> roleIds
    ) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ManagementResponse>builder()
                .message(messageService.getMessage("add.success"))
                .data(managementService.addManagement(management, roleIds))
                .build());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<ManagementResponse>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<ManagementResponse>builder()
                .message(messageService.getMessage("get.success"))
                .data(managementService.getById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ManagementResponse>> editManagement(
            @PathVariable Long id,
            @RequestBody ManagementRequest management,
            @RequestParam(required = false) List<Long> roleIds
    ) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<ManagementResponse>builder()
                .message(messageService.getMessage("edit.success"))
                .data(managementService.editManagement(id, management, roleIds))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteManagement(@PathVariable Long id) throws ResourceNotFoundException {
        managementService.deleteManagement(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(messageService.getMessage("delete.success"))
                .build());
    }
}
