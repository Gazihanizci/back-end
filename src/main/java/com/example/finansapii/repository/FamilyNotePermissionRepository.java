package com.example.finansapii.repository;

import com.example.finansapii.entity.FamilyNotePermission;
import com.example.finansapii.permissions.PermissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FamilyNotePermissionRepository extends JpaRepository<FamilyNotePermission, Long> {

    Optional<FamilyNotePermission> findByFamilyIdAndRequesterUserId(Long familyId, Long requesterUserId);

    boolean existsByFamilyIdAndRequesterUserIdAndStatus(Long familyId, Long requesterUserId, PermissionStatus status);

    List<FamilyNotePermission> findByFamilyIdAndStatusOrderByIdDesc(Long familyId, PermissionStatus status);

    List<FamilyNotePermission> findByRequesterUserIdOrderByIdDesc(Long requesterUserId);
}