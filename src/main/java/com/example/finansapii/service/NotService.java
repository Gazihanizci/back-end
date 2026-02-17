package com.example.finansapii.service;

import com.example.finansapii.dto.*;
import com.example.finansapii.entity.Not;
import com.example.finansapii.repository.NotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NotService {

    private final NotRepository notRepository;

    public NotService(NotRepository notRepository) {
        this.notRepository = notRepository;
    }

    private NotResponse toResponse(Not n) {
        return new NotResponse(n.getNotId(), n.getNotMetini(), n.getCreatedAd());
    }

    @Transactional
    public NotResponse create(Long currentUserId, NotCreateRequest req) {
        Not n = new Not();
        n.setKullaniciId(currentUserId);
        n.setNotMetini(req.notMetini().trim());
        Not saved = notRepository.save(n);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<NotResponse> listMyNotes(Long currentUserId) {
        return notRepository.findByKullaniciIdOrderByNotIdDesc(currentUserId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public NotResponse getMyNote(Long currentUserId, Long notId) {
        Not n = notRepository.findByNotIdAndKullaniciId(notId, currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not bulunamadı"));
        return toResponse(n);
    }

    @Transactional
    public NotResponse update(Long currentUserId, Long notId, NotUpdateRequest req) {
        Not n = notRepository.findByNotIdAndKullaniciId(notId, currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not bulunamadı"));

        n.setNotMetini(req.notMetini().trim());
        Not saved = notRepository.save(n);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long currentUserId, Long notId) {
        Not n = notRepository.findByNotIdAndKullaniciId(notId, currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not bulunamadı"));
        notRepository.delete(n);
    }
}
