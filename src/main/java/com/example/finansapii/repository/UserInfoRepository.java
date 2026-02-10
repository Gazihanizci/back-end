package com.example.finansapii.repository;

import com.example.finansapii.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<User, Long> {

    // Gerekirse ileride özel sorgular ekleyebilirsin
    // Optional<User> findByEmail(String email);
}
