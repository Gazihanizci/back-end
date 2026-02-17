package com.example.finansapii.dto;

import java.time.LocalDateTime;

public record PermissionInboxItem(
        Long id,
        Long requesterUserId,
        String adSoyad,
        LocalDateTime createdAt
) { }
