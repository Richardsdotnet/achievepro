package com.richards.achievepro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementResponseDTO {
    private Long id;
    private String userId;
    private String title;
    private String description;
    private int rating;
    private String dateAchieved;
}
