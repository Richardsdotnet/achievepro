package com.richards.achievepro.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementRequestDTO {

    private String userId;
    private String title;
    private String description;
    private int rating;
    private String dateAchieved;
}
