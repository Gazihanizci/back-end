package com.example.finansapii.service;

import com.example.finansapii.dto.FamilyInfoResponse;
import com.example.finansapii.entity.Aile;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.AileRepository;
import com.example.finansapii.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyInfoService {

    private final UserRepository userRepository;
    private final AileRepository aileRepository;

    public FamilyInfoService(UserRepository userRepository, AileRepository aileRepository) {
        this.userRepository = userRepository;
        this.aileRepository = aileRepository;
    }

    public FamilyInfoResponse getFamilyInfo(Long requestUserId) {
        // 1) istek atan kullanıcı
        User requester = userRepository.findById(requestUserId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + requestUserId));

        // 2) kullanıcı aileye bağlı mı?
        Long aileId = requester.getAileId();
        if (aileId == null) {
            // Front tarafında hasFamily false olsun diye null döndür
            return new FamilyInfoResponse(null, null, List.of());
        }

        // 3) aile bilgisi
        Aile aile = aileRepository.findById(aileId)
                .orElseThrow(() -> new RuntimeException("Aile bulunamadı: " + aileId));

        // 4) üyeler: aynı aile_id’ye sahip tüm kullanıcılar
        List<User> users = userRepository.findAllByAileId(aileId);

        List<FamilyInfoResponse.MemberDto> members = users.stream()
                .map(u -> new FamilyInfoResponse.MemberDto(
                        u.getId(),        // sende getKullaniciId() olabilir
                        u.getAd(),
                        u.getSoyad(),
                        u.getEmail()
                ))
                .toList();

        return new FamilyInfoResponse(
                aileId,
                aile.getAileSahibiKullaniciId(),
                members
        );
    }
}
