package com.example.finansapii.repository;

import com.example.finansapii.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // ✅ ŞART
    List<User> findAllByAileId(Long aileId);
}
