package com.example.finansapii.dto;

import java.time.LocalDateTime;

public record NotResponse(
        Long notId,
        String notMetini,
        LocalDateTime createdAt
) { }
