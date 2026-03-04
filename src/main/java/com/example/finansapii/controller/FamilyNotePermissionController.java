package com.example.finansapii.controller;

import com.example.finansapii.dto.FamilyNotePermissionRequestResponse;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.service.FamilyNotePermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aile-not-izin")
public class FamilyNotePermissionController {

    private final FamilyNotePermissionService service;

    public FamilyNotePermissionController(FamilyNotePermissionService service) {
        this.service = service;
    }

    // ✅ Kullanıcı: izin iste
    // POST /api/aile-not-izin/request
    @PostMapping("/request")
    public ResponseEntity<FamilyNotePermissionRequestResponse> request() {
        Long currentUserId = CurrentUser.id();
        return ResponseEntity.ok(service.requestWritePermission(currentUserId));
    }

    // ✅ Yönetici: pending liste
    // GET /api/aile-not-izin/pending
    @GetMapping("/pending")
    public List<FamilyNotePermissionRequestResponse> pending() {
        Long currentUserId = CurrentUser.id();
        return service.listPendingRequests(currentUserId);
    }

    // ✅ Yönetici: approve
    // PUT /api/aile-not-izin/{id}/approve
    @PutMapping("/{id}/approve")
    public FamilyNotePermissionRequestResponse approve(@PathVariable Long id) {
        Long currentUserId = CurrentUser.id();
        return service.approve(currentUserId, id);
    }

    // ✅ Yönetici: reject
    // PUT /api/aile-not-izin/{id}/reject
    @PutMapping("/{id}/reject")
    public FamilyNotePermissionRequestResponse reject(@PathVariable Long id) {
        Long currentUserId = CurrentUser.id();
        return service.reject(currentUserId, id);
    }

    // ✅ Kullanıcı: benim isteklerim
    // GET /api/aile-not-izin/me
    @GetMapping("/me")
    public List<FamilyNotePermissionRequestResponse> myRequests() {
        Long currentUserId = CurrentUser.id();
        return service.listMyRequests(currentUserId);
    }
}