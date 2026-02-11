package com.example.finansapii.service;

import com.example.finansapii.dto.*;
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

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
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
        u.setSifreHash(passwordEncoder.encode(request.getParola())); // ✅ parola hash

        // Aile yoksa null bırak (senin tablona göre)
        // u.setAileId(null);

        User saved = userRepository.save(u);

        String token = jwtService.generateToken(saved.getId(), saved.getEmail());
        return new AuthResponse("Kayıt başarılı", saved.getId(), saved.getEmail(), token);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // ✅ hash kontrol
        if (!passwordEncoder.matches(request.getParola(), user.getSifreHash())) {
            throw new RuntimeException("Hatalı parola");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse("Giriş başarılı", user.getId(), user.getEmail(), token);
    }
}
