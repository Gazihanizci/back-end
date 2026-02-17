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

    // POST /api/notlar
    @PostMapping
    public ResponseEntity<NotResponse> create(@Valid @RequestBody NotCreateRequest req) {
        Long currentUserId = CurrentUser.id();
        return ResponseEntity.ok(service.create(currentUserId, req));
    }

    // GET /api/notlar
    @GetMapping
    public List<NotResponse> listMine() {
        Long currentUserId = CurrentUser.id();
        return service.listMyNotes(currentUserId);
    }

    // GET /api/notlar/{id}
    @GetMapping("/{id}")
    public NotResponse getOne(@PathVariable Long id) {
        Long currentUserId = CurrentUser.id();
        return service.getMyNote(currentUserId, id);
    }

    // PUT /api/notlar/{id}
    @PutMapping("/{id}")
    public NotResponse update(@PathVariable Long id, @Valid @RequestBody NotUpdateRequest req) {
        Long currentUserId = CurrentUser.id();
        return service.update(currentUserId, id, req);
    }

    // DELETE /api/notlar/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Long currentUserId = CurrentUser.id();
        service.delete(currentUserId, id);
        return ResponseEntity.noContent().build();
    }
}
