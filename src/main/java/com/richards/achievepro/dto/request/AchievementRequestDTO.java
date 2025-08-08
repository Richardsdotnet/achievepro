package com.richards.achievepro.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementRequestDTO {
    // Removed userId field for security. The backend will get it from the request context.
    private String title;
    private String description;
    private int rating; // Updated from 'score' to 'rating'
    private LocalDate dateAchieved;
}
