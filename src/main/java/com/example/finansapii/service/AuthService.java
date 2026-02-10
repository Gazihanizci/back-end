package com.example.finansapii.service;

import com.example.finansapii.dto.*;
import com.example.finansapii.entity.AylikAnaliz;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.AylikAnalizRepository;
import com.example.finansapii.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AylikAnalizRepository aylikAnalizRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       AylikAnalizRepository aylikAnalizRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.aylikAnalizRepository = aylikAnalizRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ KAYIT
    @Transactional
    public AuthResponse register(RegisterRequest req) {

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Bu email zaten kayıtlı");
        }

        User user = new User();
        user.setAd(req.getAd());
        user.setSoyad(req.getSoyad());
        user.setEmail(req.getEmail().toLowerCase());
        user.setTelefon(req.getTelefon());
        user.setSifreHash(passwordEncoder.encode(req.getParola()));

        User savedUser = userRepository.save(user);

        // ✅ kullanıcı oluşunca otomatik aylık analiz satırı oluştur
        AylikAnaliz analiz = new AylikAnaliz();
        analiz.setKullaniciId(savedUser.getId());
        analiz.setYilAy(LocalDate.now().withDayOfMonth(1));
        analiz.setAylikGelir(BigDecimal.ZERO);
        analiz.setAylikGider(BigDecimal.ZERO);

        aylikAnalizRepository.save(analiz);

        return new AuthResponse(
                "Kayıt başarılı",
                savedUser.getId(),
                savedUser.getEmail()
        );
    }

    // GİRİŞ
    public AuthResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Email veya parola hatalı"));

        if (!passwordEncoder.matches(req.getParola(), user.getSifreHash())) {
            throw new RuntimeException("Email veya parola hatalı");
        }

        return new AuthResponse(
                "Giriş başarılı",
                user.getId(),
                user.getEmail()
        );
    }
}
