package com.example.finansapii.controller;

import com.example.finansapii.dto.*;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.service.FamilyPermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ailecuzdani/izin")
public class FamilyWalletPermissionController {

    private final FamilyPermissionService service;

    public FamilyWalletPermissionController(FamilyPermissionService service) {
        this.service = service;
    }

    @PostMapping("/request")
    public ResponseEntity<PermissionRequestResponse> request() {
        Long currentUserId = CurrentUser.id();
        return ResponseEntity.ok(service.requestPermission(currentUserId));
    }

    @GetMapping("/status")
    public PermissionStatusResponse status() {
        Long currentUserId = CurrentUser.id();
        return service.status(currentUserId);
    }

    @GetMapping("/inbox")
    public List<PermissionInboxItem> inbox() {
        Long currentUserId = CurrentUser.id();
        return service.inbox(currentUserId);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        Long currentUserId = CurrentUser.id();
        service.approve(currentUserId, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        Long currentUserId = CurrentUser.id();
        service.reject(currentUserId, id);
        return ResponseEntity.ok().build();
    }
}
