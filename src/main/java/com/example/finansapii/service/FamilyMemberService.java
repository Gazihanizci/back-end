package com.example.finansapii.service;

import com.example.finansapii.entity.Aile;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.AileRepository;
import com.example.finansapii.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamilyMemberService {

    private final UserRepository userRepository;
    private final AileRepository aileRepository;

    public FamilyMemberService(UserRepository userRepository, AileRepository aileRepository) {
        this.userRepository = userRepository;
        this.aileRepository = aileRepository;
    }
    @Transactional
    public void leaveFamily(Long userId, Long aileId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + userId));

        Aile aile = aileRepository.findById(aileId)
                .orElseThrow(() -> new RuntimeException("Aile bulunamadı: " + aileId));

        // ✅ ekstra garanti
        if (user.getAileId() == null || !user.getAileId().equals(aileId)) {
            throw new RuntimeException("Bu aileye üye değilsin.");
        }

        boolean isOwner = aile.getAileSahibiKullaniciId().equals(userId);

        // ✅ Aile sahibi DEĞİLSE: direkt ayrıl
        if (!isOwner) {
            user.setAileId(null);
            userRepository.save(user);
            return;
        }

        // ✅ Aile sahibi İSE:
        long memberCount = userRepository.countByAileId(aileId);

        // Sadece kendisi varsa: aileyi kapat
        if (memberCount <= 1) {
            // önce kullanıcıyı aileden çıkar
            user.setAileId(null);
            userRepository.save(user);

            // (güvenli) aileye bağlı başka kullanıcı kalmasın diye temizlik
            userRepository.clearFamilyIdByAileId(aileId);

            // aile kaydını sil / kapat
            aileRepository.delete(aile);
            return;
        }

        // Başka üyeler varsa: önce sahiplik devri şart
        throw new RuntimeException("Aile sahibi ayrılmadan önce sahipliği başka bir üyeye devretmelidir.");
    }



    @Transactional
    public void removeMember(Long requestUserId, Long aileId, Long targetUserId) {

        // 1) Oturum sahibi var mı?
        User requester = userRepository.findById(requestUserId)
                .orElseThrow(() -> new RuntimeException("İstek sahibi kullanıcı bulunamadı: " + requestUserId));

        // 2) Aile var mı?
        Aile aile = aileRepository.findById(aileId)
                .orElseThrow(() -> new RuntimeException("Aile bulunamadı: " + aileId));

        // 3) Yetki: sadece aile sahibi çıkarabilir
        if (!aile.getAileSahibiKullaniciId().equals(requestUserId)) {
            throw new RuntimeException("Bu işlem için yetkin yok (aile sahibi değilsin).");
        }

        // 4) Hedef kullanıcı var mı?
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Çıkarılacak kullanıcı bulunamadı: " + targetUserId));

        // 5) Hedef aynı ailede mi?
        if (target.getAileId() == null || !target.getAileId().equals(aileId)) {
            throw new RuntimeException("Bu kullanıcı bu ailenin üyesi değil.");
        }

        // 6) Aile sahibini çıkarma engeli
        if (aile.getAileSahibiKullaniciId().equals(targetUserId)) {
            throw new RuntimeException("Aile sahibi çıkarılamaz.");
        }

        // ✅ 7) Çıkarma: kullanicilar.aile_id -> NULL
        target.setAileId(null);
        userRepository.save(target);

        // (Opsiyonel) üye sayısını güncellemek istersen:
        // Integer count = Math.max(1, aile.getAileUyeSayisi() - 1);
        // aile.setAileUyeSayisi(count);
        // aileRepository.save(aile);
    }
    @Transactional
    public void transferOwnership(Long requestUserId, Long aileId, Long newOwnerUserId) {

        Aile aile = aileRepository.findById(aileId)
                .orElseThrow(() -> new RuntimeException("Aile bulunamadı: " + aileId));

        // ✅ sadece mevcut aile sahibi devredebilir
        if (!aile.getAileSahibiKullaniciId().equals(requestUserId)) {
            throw new RuntimeException("Sahipliği sadece mevcut aile sahibi devredebilir.");
        }

        // ✅ kendine devredemez
        if (requestUserId.equals(newOwnerUserId)) {
            throw new RuntimeException("Sahiplik kendine devredilemez.");
        }

        // ✅ yeni sahip gerçekten bu ailenin üyesi mi?
        boolean member = userRepository.existsByIdAndAileId(newOwnerUserId, aileId);
        if (!member) {
            throw new RuntimeException("Yeni sahip bu ailenin üyesi değil.");
        }

        aile.setAileSahibiKullaniciId(newOwnerUserId);
        aileRepository.save(aile);
    }

}
