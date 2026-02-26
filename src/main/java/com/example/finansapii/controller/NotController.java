package com.example.finansapii.controller;

import com.example.finansapii.dto.*;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.service.NotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notlar")
public class NotController {

    private final NotService service;

    public NotController(NotService service) {
        this.service = service;
    }

    // ✅ POST /api/notlar  (USER veya FAMILY)
    @PostMapping
    public ResponseEntity<NotResponse> create(@Valid @RequestBody NotCreateRequest req) {
        Long currentUserId = CurrentUser.id();
        return ResponseEntity.ok(service.create(currentUserId, req));
    }

    // ✅ GET /api/notlar  => benim USER notlarım
    @GetMapping
    public List<NotResponse> listMine() {
        Long currentUserId = CurrentUser.id();
        return service.listMyUserNotes(currentUserId);
    }

    // ✅ GET /api/notlar/{id} => USER not
    @GetMapping("/{id}")
    public NotResponse getOne(@PathVariable Long id) {
        Long currentUserId = CurrentUser.id();
        return service.getMyUserNote(currentUserId, id);
    }

    // ✅ PUT /api/notlar/{id} => USER not update
    @PutMapping("/{id}")
    public NotResponse update(@PathVariable Long id, @Valid @RequestBody NotUpdateRequest req) {
        Long currentUserId = CurrentUser.id();
        return service.updateUser(currentUserId, id, req);
    }

    // ✅ DELETE /api/notlar/{id} => USER not delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Long currentUserId = CurrentUser.id();
        service.deleteUser(currentUserId, id);
        return ResponseEntity.noContent().build();
    }

    // -------------------
    // ✅ AİLE NOTLARI
    // -------------------

    // ✅ GET /api/notlar/aile  => aile notları (herkes görür)
    @GetMapping("/aile")
    public List<NotResponse> listFamily() {
        Long currentUserId = CurrentUser.id();
        return service.listMyFamilyNotes(currentUserId);
    }

    // ✅ GET /api/notlar/aile/{id}
    @GetMapping("/aile/{id}")
    public NotResponse getFamily(@PathVariable Long id) {
        Long currentUserId = CurrentUser.id();
        return service.getMyFamilyNote(currentUserId, id);
    }

    // ✅ PUT /api/notlar/aile/{id}
    @PutMapping("/aile/{id}")
    public NotResponse updateFamily(@PathVariable Long id, @Valid @RequestBody NotUpdateRequest req) {
        Long currentUserId = CurrentUser.id();
        return service.updateFamily(currentUserId, id, req);
    }

    // ✅ DELETE /api/notlar/aile/{id}
    @DeleteMapping("/aile/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable Long id) {
        Long currentUserId = CurrentUser.id();
        service.deleteFamily(currentUserId, id);
        return ResponseEntity.noContent().build();
    }
}