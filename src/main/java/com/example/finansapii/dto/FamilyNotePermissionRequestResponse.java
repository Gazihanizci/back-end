package com.example.finansapii.dto;

import com.example.finansapii.permissions.PermissionStatus;

import java.time.LocalDateTime;

public record FamilyNotePermissionRequestResponse(
        Long id,
        Long familyId,
        Long requesterUserId,
        PermissionStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long resolvedByUserId,
        LocalDateTime resolvedAt
) {}