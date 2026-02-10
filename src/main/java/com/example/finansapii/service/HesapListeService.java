package com.example.finansapii.service;

import com.example.finansapii.dto.HesapResponse;
import com.example.finansapii.entity.Hesap;
import com.example.finansapii.repository.HesapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HesapListeService {

    private final HesapRepository hesapRepository;

    public HesapListeService(HesapRepository hesapRepository) {
        this.hesapRepository = hesapRepository;
    }

    @Transactional(readOnly = true)
    public List<HesapResponse> getUserAccounts(Long kullaniciId) {
        return hesapRepository.findAllByKullaniciIdOrderByCreatedAtAsc(kullaniciId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private HesapResponse toResponse(Hesap h) {
        return new HesapResponse(
                h.getHesapId(),
                h.getKullaniciId(),
                h.getAileId(),
                h.getHesapAdi(),
                h.getParaBirimi(),
                h.getBakiye(),
                h.getCreatedAt()
        );
    }
}
