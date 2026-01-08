package com.thai27.trang_tin_tuc_v5_be.Controller;

import com.thai27.trang_tin_tuc_v5_be.Entity.Management;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Service.ManagementService;
import com.thai27.trang_tin_tuc_v5_be.Util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managements")
public class ManagementController {

    @Autowired
    private ManagementService managementService;

    /**
     * Get all managements by username (from user's roles)
     * GET /api/managements?username=admin
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Management>>> getAllManagement(
            @RequestParam String username
    ) throws ResourceNotFoundException {
        return managementService.getAllManagement(username);
    }

    /**
     * Add management + assign roles
     * POST /api/managements
     *
     * body:
     * {
     *   "name": "USER_MANAGEMENT"
     * }
     *
     * params:
     * roleIds=1,2,3
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Management>> addManagement(
            @RequestBody Management management,
            @RequestParam(required = false) List<Long> roleIds
    ) throws ResourceNotFoundException {
        return managementService.addManagement(management, roleIds);
    }

    /**
     * Edit management + update roles
     * PUT /api/managements/{id}
     *
     * params:
     * roleIds=1,2,3
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Management>> editManagement(
            @PathVariable Long id,
            @RequestBody Management management,
            @RequestParam(required = false) List<Long> roleIds
    ) throws ResourceNotFoundException {
        return managementService.editManagement(id, management, roleIds);
    }
}
