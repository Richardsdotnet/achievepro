package com.richards.achievepro.achievementModels;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user achievement, mapped to the 'achievements' table in the database.
 */
@Entity
@Table(name = "achievements")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Achievement {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "user_id", nullable = false, length = 50)
        private String userId;

        @Column(name = "title", nullable = false, length = 255)
        private String title;

        @Column(name = "description", columnDefinition = "TEXT")
        private String description;

        @Column(name = "rating", nullable = true)
        private int rating;

        @Column(name = "date_achieved", nullable = false, length = 10)
        private String dateAchieved;
}
