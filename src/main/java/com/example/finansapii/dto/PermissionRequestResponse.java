package com.example.finansapii.dto;

public record PermissionRequestResponse(
        String status,          // PENDING / APPROVED
        boolean alreadyApproved // approved ise true
) { }
