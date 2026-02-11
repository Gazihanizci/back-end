package com.example.finansapii.security;

import org.springframework.security.core.context.SecurityContextHolder;

public final class CurrentUser {
    private CurrentUser() {}

    public static Long id() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("Token yok veya geçersiz");
        }
        Object principal = auth.getPrincipal();

        if (principal instanceof Long l) return l;
        if (principal instanceof String s) return Long.parseLong(s);

        throw new RuntimeException("UserId okunamadı");
    }
}
