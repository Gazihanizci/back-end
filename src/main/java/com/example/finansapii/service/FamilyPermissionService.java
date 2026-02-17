package com.example.finansapii.service;

import com.example.finansapii.dto.*;
import com.example.finansapii.entity.Aile;
import com.example.finansapii.entity.FamilyPermission;
import com.example.finansapii.entity.User;
import com.example.finansapii.permissions.PermissionStatus;
import com.example.finansapii.repository.AileRepository;
import com.example.finansapii.repository.FamilyPermissionRepository;
import com.example.finansapii.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FamilyPermissionService {

    private final FamilyPermissionRepository permissionRepo;
    private final UserRepository userRepo;
    private final AileRepository aileRepo;

    public FamilyPermissionService(FamilyPermissionRepository permissionRepo,
                                   UserRepository userRepo,
                                   AileRepository aileRepo) {
        this.permissionRepo = permissionRepo;
        this.userRepo = userRepo;
        this.aileRepo = aileRepo;
    }

    private User mustGetUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));
    }

    private Aile mustGetFamily(Long familyId) {
        return aileRepo.findById(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aile bulunamadı"));
    }

    private Long mustGetCurrentFamilyId(Long currentUserId) {
        User user = mustGetUser(currentUserId);
        if (user.getAileId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kullanıcının aile_id değeri yok");
        }
        return user.getAileId();
    }

    private void assertOwner(Long familyId, Long currentUserId) {
        Aile aile = mustGetFamily(familyId);
        if (!aile.getAileSahibiKullaniciId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sadece aile sahibi işlem yapabilir");
        }
    }

    @Transactional
    public PermissionRequestResponse requestPermission(Long currentUserId) {
        Long familyId = mustGetCurrentFamilyId(currentUserId);

        // 1) Aynı anda 1 PENDING
        if (permissionRepo.existsByFamilyIdAndRequesterUserIdAndStatus(familyId, currentUserId, PermissionStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Zaten bekleyen (PENDING) bir izin isteğin var");
        }

        // 2) Son durum APPROVED ise zaten onaylı
        var last = permissionRepo.findTopByFamilyIdAndRequesterUserIdOrderByCreatedAtDesc(familyId, currentUserId);
        if (last.isPresent() && last.get().getStatus() == PermissionStatus.APPROVED) {
            return new PermissionRequestResponse("APPROVED", true);
        }

        // 3) Yeni PENDING oluştur
        FamilyPermission fp = new FamilyPermission();
        fp.setFamilyId(familyId);
        fp.setRequesterUserId(currentUserId);
        fp.setStatus(PermissionStatus.PENDING);
        permissionRepo.save(fp);

        return new PermissionRequestResponse("PENDING", false);
    }

    @Transactional(readOnly = true)
    public PermissionStatusResponse status(Long currentUserId) {
        User user = mustGetUser(currentUserId);

        if (user.getAileId() == null) {
            return new PermissionStatusResponse("NONE");
        }

        var last = permissionRepo.findTopByFamilyIdAndRequesterUserIdOrderByCreatedAtDesc(user.getAileId(), currentUserId);
        return new PermissionStatusResponse(last.map(x -> x.getStatus().name()).orElse("NONE"));
    }

    @Transactional(readOnly = true)
    public List<PermissionInboxItem> inbox(Long ownerId) {
        Long familyId = mustGetCurrentFamilyId(ownerId);
        assertOwner(familyId, ownerId);
        return permissionRepo.inbox(familyId, PermissionStatus.PENDING);
    }

    @Transactional
    public void approve(Long ownerId, Long permissionId) {
        FamilyPermission fp = permissionRepo.findById(permissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "İzin isteği bulunamadı"));

        assertOwner(fp.getFamilyId(), ownerId);

        if (fp.getStatus() != PermissionStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sadece PENDING istek onaylanabilir");
        }

        fp.setStatus(PermissionStatus.APPROVED);
        fp.setResolvedByUserId(ownerId);
        fp.setResolvedAt(LocalDateTime.now());
        permissionRepo.save(fp);
    }

    @Transactional
    public void reject(Long ownerId, Long permissionId) {
        FamilyPermission fp = permissionRepo.findById(permissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "İzin isteği bulunamadı"));

        assertOwner(fp.getFamilyId(), ownerId);

        if (fp.getStatus() != PermissionStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sadece PENDING istek reddedilebilir");
        }

        fp.setStatus(PermissionStatus.REJECTED);
        fp.setResolvedByUserId(ownerId);
        fp.setResolvedAt(LocalDateTime.now());
        permissionRepo.save(fp);
    }

    // İstersen cüzdan endpointlerinde kullan:
    @Transactional(readOnly = true)
    public boolean hasWalletAccess(Long userId) {
        User user = mustGetUser(userId);
        if (user.getAileId() == null) return false;

        var last = permissionRepo.findTopByFamilyIdAndRequesterUserIdOrderByCreatedAtDesc(user.getAileId(), userId);
        return last.isPresent() && last.get().getStatus() == PermissionStatus.APPROVED;
    }
}
