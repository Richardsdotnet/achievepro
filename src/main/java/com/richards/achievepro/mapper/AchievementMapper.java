package com.richards.achievepro.mapper;

import com.richards.achievepro.achievementModels.Achievement;
import com.richards.achievepro.dto.request.AchievementRequestDTO;
import com.richards.achievepro.dto.response.AchievementResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AchievementMapper {

    public Achievement toEntity(AchievementRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Achievement entity = new Achievement();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setRating(dto.getRating());
        entity.setDateAchieved(dto.getDateAchieved());
        return entity;
    }

    public AchievementResponseDTO toDto(Achievement entity) {
        if (entity == null) {
            return null;
        }
        AchievementResponseDTO dto = new AchievementResponseDTO();
        dto.setId(entity.getId());
//        dto.setUserId(entity.getUserId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setRating(entity.getRating());
        dto.setDateAchieved(entity.getDateAchieved());
        return dto;
    }

    public List<AchievementResponseDTO> toDtoList(List<Achievement> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
