package com.example.finansapii.service;

import com.example.finansapii.dto.*;
import com.example.finansapii.entity.Not;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.NotRepository;
import com.example.finansapii.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NotService {

    private final NotRepository notRepository;
    private final UserRepository userRepository;

    public NotService(NotRepository notRepository, UserRepository userRepository) {
        this.notRepository = notRepository;
        this.userRepository = userRepository;
    }

    private Long getUserAileIdOrThrow(Long currentUserId) {
        User u = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));
        if (u.getAileId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aile notu için kullanıcı bir aileye bağlı olmalı");
        }
        return u.getAileId();
    }

    private NotResponse toResponse(Not n) {
        return new NotResponse(
                n.getNotId(),
                n.getNotMetini(),
                n.getCreatedAd(),
                n.getNotTuru(),
                n.getAileId()
        );
    }

    // ✅ USER veya FAMILY not oluştur
    @Transactional
    public NotResponse create(Long currentUserId, NotCreateRequest req) {
        String metin = req.notMetini().trim();

        Not n = new Not();
        n.setKullaniciId(currentUserId);
        n.setNotMetini(metin);
        n.setNotTuru(req.notTuru());

        if (req.notTuru() == NotTuru.FAMILY) {
            Long aileId = getUserAileIdOrThrow(currentUserId);
            n.setAileId(aileId);
        } else {
            n.setAileId(null);
        }

        Not saved = notRepository.save(n);
        return toResponse(saved);
    }

    // ✅ Benim USER notlarım
    @Transactional(readOnly = true)
    public List<NotResponse> listMyUserNotes(Long currentUserId) {
        return notRepository.findByKullaniciIdOrderByNotIdDesc(currentUserId)
                .stream().map(this::toResponse).toList();
    }

    // ✅ Benim FAMILY notlarım (ailedeki herkesin notları)
    @Transactional(readOnly = true)
    public List<NotResponse> listMyFamilyNotes(Long currentUserId) {
        Long aileId = getUserAileIdOrThrow(currentUserId);
        return notRepository.findByAileIdOrderByNotIdDesc(aileId)
                .stream().map(this::toResponse).toList();
    }

    // ✅ Tek USER not
    @Transactional(readOnly = true)
    public NotResponse getMyUserNote(Long currentUserId, Long notId) {
        Not n = notRepository.findByNotIdAndKullaniciId(notId, currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not bulunamadı"));
        return toResponse(n);
    }

    // ✅ Tek FAMILY not (aileye ait mi kontrol)
    @Transactional(readOnly = true)
    public NotResponse getMyFamilyNote(Long currentUserId, Long notId) {
        Long aileId = getUserAileIdOrThrow(currentUserId);
        Not n = notRepository.findByNotIdAndAileId(notId, aileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aile notu bulunamadı"));
        return toResponse(n);
    }

    // ✅ USER not güncelle
    @Transactional
    public NotResponse updateUser(Long currentUserId, Long notId, NotUpdateRequest req) {
        Not n = notRepository.findByNotIdAndKullaniciId(notId, currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not bulunamadı"));
        n.setNotMetini(req.notMetini().trim());
        return toResponse(notRepository.save(n));
    }

    // ✅ FAMILY not güncelle (aileye ait mi kontrol)
    @Transactional
    public NotResponse updateFamily(Long currentUserId, Long notId, NotUpdateRequest req) {
        Long aileId = getUserAileIdOrThrow(currentUserId);
        Not n = notRepository.findByNotIdAndAileId(notId, aileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aile notu bulunamadı"));
        n.setNotMetini(req.notMetini().trim());
        return toResponse(notRepository.save(n));
    }

    // ✅ USER not sil
    @Transactional
    public void deleteUser(Long currentUserId, Long notId) {
        Not n = notRepository.findByNotIdAndKullaniciId(notId, currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not bulunamadı"));
        notRepository.delete(n);
    }

    // ✅ FAMILY not sil
    @Transactional
    public void deleteFamily(Long currentUserId, Long notId) {
        Long aileId = getUserAileIdOrThrow(currentUserId);
        Not n = notRepository.findByNotIdAndAileId(notId, aileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aile notu bulunamadı"));
        notRepository.delete(n);
    }
}