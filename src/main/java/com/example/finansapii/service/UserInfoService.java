package com.example.finansapii.service;

import com.example.finansapii.dto.UserInfoResponse;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    private final UserRepository userRepository;

    public UserInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfoResponse getUserInfo(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + userId));

        return new UserInfoResponse(
                u.getId(),
                u.getAd(),
                u.getSoyad(),
                u.getEmail(),
                u.getAileId()
        );
    }
}
