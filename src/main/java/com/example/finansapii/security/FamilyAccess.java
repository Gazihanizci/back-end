package com.example.finansapii.security;

import com.example.finansapii.repository.UserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public class FamilyAccess {

    private final UserRepository userRepository;

    public FamilyAccess(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void assertUserInFamily(Long userId, Long aileId) {
        Long userFamilyId = userRepository.findAileIdByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (userFamilyId == null || !userFamilyId.equals(aileId)) {
            throw new SecurityException("Bu aileye erişim yok");
        }
    }
}
