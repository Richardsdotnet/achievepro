package com.richards.achievepro.repository;

import com.richards.achievepro.models.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository for Achievement entities.
 * Provides standard CRUD (Create, Read, Update, Delete) operations.
 */
@Repository // Marks this interface as a Spring Data repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    // JpaRepository provides methods like save(), findById(), findAll(), deleteById(), etc.
    // You can add custom query methods here if needed, e.g.:
    // List<Achievement> findByStudentId(String studentId);
}