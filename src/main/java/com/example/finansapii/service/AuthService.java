package com.example.finansapii.service;

import com.example.finansapii.dto.AuthResponse;
import com.example.finansapii.dto.LoginRequest;
import com.example.finansapii.dto.RegisterRequest;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.UserRepository;
import com.example.finansapii.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

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
        u.setSifreHash(passwordEncoder.encode(request.getParola())); // parola hash

        User saved = userRepository.save(u);

        String accessToken = jwtService.generateAccessToken(saved.getId(), saved.getEmail());
        String refreshToken = jwtService.generateRefreshToken(saved.getId());

        return new AuthResponse(
                "Kayıt başarılı",
                saved.getId(),
                saved.getEmail(),
                accessToken,
                refreshToken
        );
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.getParola(), user.getSifreHash())) {
            throw new RuntimeException("Hatalı parola");
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
}
