package com.example.finansapii.service;
import com.example.finansapii.dto.HesapBakiyeUpdateRequest;
import com.example.finansapii.dto.HesapCreateRequest;
import com.example.finansapii.dto.HesapResponse;
import com.example.finansapii.entity.Hesap;
import com.example.finansapii.repository.HesapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.example.finansapii.dto.HesapBakiyeUpdateRequest;

@Service
public class HesapService {

    private final HesapRepository hesapRepository;

    public HesapService(HesapRepository hesapRepository) {
        this.hesapRepository = hesapRepository;
    }
     @Transactional
    public HesapResponse create(Long kullaniciId, HesapCreateRequest req) {
        Hesap h = new Hesap();
        h.setKullaniciId(kullaniciId);
        h.setAileId(req.aileId());
        h.setHesapAdi(req.hesapAdi().trim());
        h.setParaBirimi(req.paraBirimi().trim().toUpperCase());
        h.setBakiye(req.bakiye());

        Hesap saved = hesapRepository.save(h);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public HesapResponse getCurrent(Long kullaniciId) {
        Hesap h = hesapRepository.findFirstByKullaniciIdOrderByCreatedAtAsc(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı"));
        return toResponse(h);
    }

    // ✅ BURASI EKSİKTİ
    @Transactional(readOnly = true)
    public List<HesapResponse> getAll(Long kullaniciId) {
        return hesapRepository.findAllByKullaniciIdOrderByCreatedAtAsc(kullaniciId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    @Transactional
    public HesapResponse updateBakiye(Long kullaniciId, Long hesapId, HesapBakiyeUpdateRequest req) {

        Hesap h = hesapRepository.findByHesapIdAndKullaniciId(hesapId, kullaniciId)
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı veya size ait değil"));

        h.setBakiye(req.bakiye());

        Hesap saved = hesapRepository.save(h);
        return toResponse(saved);
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
