package com.richards.achievepro.mapper;

import com.richards.achievepro.models.Achievement;
import com.richards.achievepro.dto.request.AchievementRequestDTO;
import com.richards.achievepro.dto.response.AchievementResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between Achievement entities and DTOs.
 */
@Component
public class AchievementMapper {

    /**
     * Converts an AchievementRequestDTO to an Achievement entity.
     * @param dto The AchievementRequestDTO to convert.
     * @return The corresponding Achievement entity.
     */
    public Achievement toEntity(AchievementRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Achievement entity = new Achievement();
        entity.setUserId(dto.getUserId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setRating(dto.getRating());
        entity.setDateAchieved(dto.getDateAchieved());
        return entity;
    }

    /**
     * Converts an Achievement entity to an AchievementResponseDTO.
     * @param entity The Achievement entity to convert.
     * @return The corresponding AchievementResponseDTO.
     */
    public AchievementResponseDTO toDto(Achievement entity) {
        if (entity == null) {
            return null;
        }
        AchievementResponseDTO dto = new AchievementResponseDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setRating(entity.getRating());
        dto.setDateAchieved(entity.getDateAchieved());
        return dto;
    }

    /**
     * Converts a list of Achievement entities to a list of AchievementResponseDTOs.
     * @param entities The list of Achievement entities.
     * @return A list of AchievementResponseDTOs.
     */
    public List<AchievementResponseDTO> toDtoList(List<Achievement> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
