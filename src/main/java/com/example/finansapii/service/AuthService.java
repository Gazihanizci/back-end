package com.example.finansapii.service;

import com.example.finansapii.dto.AuthResponse;
import com.example.finansapii.dto.LoginRequest;
import com.example.finansapii.dto.RegisterRequest;
import com.example.finansapii.dto.VerifyEmailRequest;
import com.example.finansapii.entity.EmailVerificationCode;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.EmailVerificationCodeRepository;
import com.example.finansapii.repository.UserRepository;
import com.example.finansapii.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailVerificationCodeRepository verificationRepo;
    private final EmailService emailService;

    private static final SecureRandom RNG = new SecureRandom();

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       EmailVerificationCodeRepository verificationRepo,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.verificationRepo = verificationRepo;
        this.emailService = emailService;
    }

    // 🔥 REGISTER
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Bu email zaten kayıtlı");
        }

        User u = new User();
        u.setAd(request.getAd().trim());
        u.setSoyad(request.getSoyad().trim());
        u.setEmail(email);
        u.setTelefon(request.getTelefon());
        u.setSifreHash(passwordEncoder.encode(request.getParola()));

        u.setEmailVerified(false);
        u.setEmailVerifiedAt(null);

        User saved = userRepository.save(u);

        // ✅ Kod üret
        String code = generate6DigitCode();

        EmailVerificationCode evc = new EmailVerificationCode();
        evc.setUserId(saved.getId());
        evc.setCode(code);
        evc.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        verificationRepo.save(evc);

        // ✅ Mail gönder
        emailService.sendVerificationCode(saved.getEmail(), code);

        // ❌ Token verme
        return new AuthResponse(
                "Kayıt başarılı. Emailinize doğrulama kodu gönderildi.",
                saved.getId(),
                saved.getEmail(),
                null,
                null
        );
    }

    // 🔥 EMAIL DOĞRULAMA
    @Transactional
    public AuthResponse verifyEmail(VerifyEmailRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        EmailVerificationCode evc = verificationRepo
                .findTopByUserIdAndCodeOrderByCreatedAtDesc(user.getId(), request.getCode())
                .orElseThrow(() -> new RuntimeException("Kod geçersiz"));

        if (evc.getUsedAt() != null)
            throw new RuntimeException("Kod zaten kullanılmış");

        if (evc.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Kodun süresi dolmuş");

        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        evc.setUsedAt(LocalDateTime.now());
        verificationRepo.save(evc);

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return new AuthResponse(
                "Email doğrulandı. Giriş başarılı",
                user.getId(),
                user.getEmail(),
                accessToken,
                refreshToken
        );
    }

    // 🔥 LOGIN
    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.getParola(), user.getSifreHash())) {
            throw new RuntimeException("Hatalı parola");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Email doğrulanmamış.");
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return new AuthResponse(
                "Giriş başarılı",
                user.getId(),
                user.getEmail(),
                accessToken,
                refreshToken
        );
    }

    private String generate6DigitCode() {
        int number = RNG.nextInt(900000) + 100000;
        return String.valueOf(number);
    }
}
