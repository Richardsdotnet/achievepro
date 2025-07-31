package com.richards.achievepro.service;

import com.richards.achievepro.mapper.AchievementMapper;
import com.richards.achievepro.dto.request.AchievementRequestDTO;
import com.richards.achievepro.dto.response.AchievementResponseDTO;
import com.richards.achievepro.models.Achievement;
import com.richards.achievepro.repository.AchievementRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing user achievements.
 * Handles business logic and interacts with the repository and mapper.
 */
@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    @Autowired
    public AchievementService(AchievementRepository achievementRepository,
                              AchievementMapper achievementMapper) {
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
    }

    /**
     * Retrieves all user achievements.
     * @return A list of AchievementResponseDTOs.
     */
    @Transactional(readOnly = true)
    public List<AchievementResponseDTO> getAllAchievements() {
        List<Achievement> achievements = achievementRepository.findAll();
        return achievementMapper.toDtoList(achievements);
    }

    /**
     * Retrieves a single achievement by its ID.
     * @param id The ID of the achievement.
     * @return An Optional containing the AchievementResponseDTO if found, or empty if not.
     */
    @Transactional(readOnly = true)
    public Optional<AchievementResponseDTO> getAchievementById(Long id) {
        return achievementRepository.findById(id)
                .map(achievementMapper::toDto);
    }

    /**
     * Creates a new achievement.
     * @param requestDTO The AchievementRequestDTO containing the data for the new achievement.
     * @return The created AchievementResponseDTO with its generated ID.
     */
    @Transactional
    public AchievementResponseDTO createAchievement(AchievementRequestDTO requestDTO) {
        Achievement achievement = achievementMapper.toEntity(requestDTO);
        Achievement savedAchievement = achievementRepository.save(achievement);
        return achievementMapper.toDto(savedAchievement);
    }

    /**
     * Updates an existing achievement.
     * @param id The ID of the achievement to update.
     * @param requestDTO An AchievementRequestDTO containing the updated details.
     * @return The updated AchievementResponseDTO, or null if the achievement with the given ID was not found.
     */
    @Transactional
    public AchievementResponseDTO updateAchievement(Long id, AchievementRequestDTO requestDTO) {
        Optional<Achievement> optionalAchievement = achievementRepository.findById(id);
        if (optionalAchievement.isPresent()) {
            Achievement existingAchievement = optionalAchievement.get();
            existingAchievement.setUserId(requestDTO.getUserId());
            existingAchievement.setTitle(requestDTO.getTitle());
            existingAchievement.setDescription(requestDTO.getDescription());
            existingAchievement.setRating(requestDTO.getRating());
            existingAchievement.setDateAchieved(requestDTO.getDateAchieved());

            Achievement updatedAchievement = achievementRepository.save(existingAchievement);
            return achievementMapper.toDto(updatedAchievement);
        } else {
            return null;
        }
    }

    /**
     * Deletes an achievement by its ID.
     * @param id The ID of the achievement to delete.
     * @return true if the achievement was deleted, false otherwise.
     */
    @Transactional
    public boolean deleteAchievement(Long id) {
        if (achievementRepository.existsById(id)) {
            achievementRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
