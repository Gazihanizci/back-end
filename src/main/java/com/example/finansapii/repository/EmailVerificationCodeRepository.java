package com.example.finansapii.repository;

import com.example.finansapii.entity.EmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, Long> {

    Optional<EmailVerificationCode> findTopByUserIdAndCodeOrderByCreatedAtDesc(Long userId, String code);

    Optional<EmailVerificationCode> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
