package com.example.finansapii.repository;

import com.example.finansapii.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByAileId(Long aileId);
    @org.springframework.data.jpa.repository.Query("select u.aileId from User u where u.id = :userId")
    java.util.Optional<Long> findAileIdByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

}
