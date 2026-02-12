package com.example.finansapii.repository;

import com.example.finansapii.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findAllByAileId(Long aileId);

    @Query("select u.aileId from User u where u.id = :userId")
    Optional<Long> findAileIdByUserId(@Param("userId") Long userId);

    // ✅ EK: ailede kaç kişi var?
    long countByAileId(Long aileId);

    // ✅ EK: belirli user bu ailede mi?
    boolean existsByIdAndAileId(Long id, Long aileId);

    // ✅ (Opsiyonel) EK: aile kapatırken temizlik
    @Modifying
    @Query("update User u set u.aileId = null where u.aileId = :aileId")
    int clearFamilyIdByAileId(@Param("aileId") Long aileId);
}
