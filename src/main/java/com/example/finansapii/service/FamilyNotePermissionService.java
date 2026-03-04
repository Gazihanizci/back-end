package com.example.finansapii.service;

import com.example.finansapii.dto.FamilyNotePermissionRequestResponse;
import com.example.finansapii.entity.Aile;
import com.example.finansapii.entity.FamilyNotePermission;
import com.example.finansapii.entity.User;
import com.example.finansapii.permissions.PermissionStatus;
import com.example.finansapii.repository.AileRepository;
import com.example.finansapii.repository.FamilyNotePermissionRepository;
import com.example.finansapii.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FamilyNotePermissionService {

    private final FamilyNotePermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final AileRepository aileRepository;

    public FamilyNotePermissionService(
            FamilyNotePermissionRepository permissionRepository,
            UserRepository userRepository,
            AileRepository aileRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.aileRepository = aileRepository;
    }

    private Long getUserAileIdOrThrow(Long currentUserId) {
        User u = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));
        if (u.getAileId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "İşlem için kullanıcı bir aileye bağlı olmalı");
        }
        return u.getAileId();
    }

    private boolean isFamilyOwner(Long currentUserId, Long aileId) {
        Aile aile = aileRepository.findById(aileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aile bulunamadı"));
        return aile.getAileSahibiKullaniciId() != null && aile.getAileSahibiKullaniciId().equals(currentUserId);
    }

    private FamilyNotePermissionRequestResponse toResponse(FamilyNotePermission p) {
        return new FamilyNotePermissionRequestResponse(
                p.getId(),
                p.getFamilyId(),
                p.getRequesterUserId(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                p.getResolvedByUserId(),
                p.getResolvedAt()
        );
    }

    // ✅ Kullanıcı: Aile notu yazma izni iste
    @Transactional
    public FamilyNotePermissionRequestResponse requestWritePermission(Long currentUserId) {
        Long aileId = getUserAileIdOrThrow(currentUserId);

        // Aile sahibi zaten yönetici -> otomatik izinli say (istersen tabloya da yazdırabiliriz)
        if (isFamilyOwner(currentUserId, aileId)) {
            // Owner için kayıt oluşturmayı şart koşmuyorum; direkt APPROVED gibi davranıyoruz.
            return new FamilyNotePermissionRequestResponse(
                    -1L, aileId, currentUserId, PermissionStatus.APPROVED,
                    LocalDateTime.now(), null, currentUserId, LocalDateTime.now()
            );
        }

        // Daha önce istek var mı?
        var existingOpt = permissionRepository.findByFamilyIdAndRequesterUserId(aileId, currentUserId);

        if (existingOpt.isPresent()) {
            FamilyNotePermission existing = existingOpt.get();

            // APPROVED ise zaten var
            if (existing.getStatus() == PermissionStatus.APPROVED) {
                return toResponse(existing);
            }

            // PENDING ise tekrar oluşturma
            if (existing.getStatus() == PermissionStatus.PENDING) {
                return toResponse(existing);
            }

            // REJECTED ise tekrar istek atmasına izin verelim -> status PENDING’e çek
            existing.setStatus(PermissionStatus.PENDING);
            existing.setResolvedAt(null);
            existing.setResolvedByUserId(null);
            FamilyNotePermission saved = permissionRepository.save(existing);
            return toResponse(saved);
        }

        // Yeni istek
        FamilyNotePermission p = new FamilyNotePermission();
        p.setFamilyId(aileId);
        p.setRequesterUserId(currentUserId);
        p.setStatus(PermissionStatus.PENDING);

        FamilyNotePermission saved = permissionRepository.save(p);
        return toResponse(saved);
    }

    // ✅ Aile yöneticisi: PENDING istekleri listele
    @Transactional(readOnly = true)
    public List<FamilyNotePermissionRequestResponse> listPendingRequests(Long currentUserId) {
        Long aileId = getUserAileIdOrThrow(currentUserId);

        if (!isFamilyOwner(currentUserId, aileId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sadece aile yöneticisi görebilir");
        }

        return permissionRepository.findByFamilyIdAndStatusOrderByIdDesc(aileId, PermissionStatus.PENDING)
                .stream().map(this::toResponse).toList();
    }

    // ✅ Aile yöneticisi: Approve
    @Transactional
    public FamilyNotePermissionRequestResponse approve(Long currentUserId, Long permissionId) {
        Long aileId = getUserAileIdOrThrow(currentUserId);

        if (!isFamilyOwner(currentUserId, aileId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sadece aile yöneticisi onaylayabilir");
        }

        FamilyNotePermission p = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "İzin isteği bulunamadı"));

        if (!p.getFamilyId().equals(aileId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bu istek sizin ailenize ait değil");
        }

        p.setStatus(PermissionStatus.APPROVED);
        p.setResolvedByUserId(currentUserId);
        p.setResolvedAt(LocalDateTime.now());

        return toResponse(permissionRepository.save(p));
    }

    // ✅ Aile yöneticisi: Reject
    @Transactional
    public FamilyNotePermissionRequestResponse reject(Long currentUserId, Long permissionId) {
        Long aileId = getUserAileIdOrThrow(currentUserId);

        if (!isFamilyOwner(currentUserId, aileId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sadece aile yöneticisi reddedebilir");
        }

        FamilyNotePermission p = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "İzin isteği bulunamadı"));

        if (!p.getFamilyId().equals(aileId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bu istek sizin ailenize ait değil");
        }

        p.setStatus(PermissionStatus.REJECTED);
        p.setResolvedByUserId(currentUserId);
        p.setResolvedAt(LocalDateTime.now());

        return toResponse(permissionRepository.save(p));
    }

    // ✅ Kullanıcı: kendi isteklerim
    @Transactional(readOnly = true)
    public List<FamilyNotePermissionRequestResponse> listMyRequests(Long currentUserId) {
        return permissionRepository.findByRequesterUserIdOrderByIdDesc(currentUserId)
                .stream().map(this::toResponse).toList();
    }

    // ✅ Not yazma kontrolü (NotService kullanacak)
    @Transactional(readOnly = true)
    public boolean canWriteFamilyNote(Long currentUserId, Long aileId) {
        // owner otomatik izinli
        if (isFamilyOwner(currentUserId, aileId)) return true;

        return permissionRepository.existsByFamilyIdAndRequesterUserIdAndStatus(
                aileId, currentUserId, PermissionStatus.APPROVED
        );
    }
}