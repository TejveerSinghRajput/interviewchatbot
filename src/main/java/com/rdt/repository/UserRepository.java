package com.rdt.repository;

import com.rdt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findById(Long userId);

    @Query("SELECT COUNT(s) FROM InterviewSession s WHERE s.user.id = :userId AND s.startedAt >= :startOfDay")
    long countInterviewsToday(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay);
}
