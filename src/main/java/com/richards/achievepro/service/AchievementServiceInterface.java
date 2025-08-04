package com.richards.achievepro.service;

import com.richards.achievepro.dto.request.AchievementRequestDTO;
import com.richards.achievepro.dto.response.AchievementResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface defining the business logic for managing achievements.
 */
public interface AchievementServiceInterface {

    /**
     * Retrieves all achievements for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of achievement data transfer objects.
     */
    List<AchievementResponseDTO> getAllAchievementsByUserId(String userId);

    /**
     * Retrieves a single achievement by its ID, ensuring it belongs to the specified user.
     *
     * @param id The ID of the achievement.
     * @param userId The ID of the user.
     * @return An Optional containing the achievement DTO if found, otherwise empty.
     */
    Optional<AchievementResponseDTO> getAchievementByIdAndUserId(Long id, String userId);

    /**
     * Creates a new achievement for a user.
     *
     * @param requestDTO The DTO containing the new achievement's data.
     * @param userId The ID of the user creating the achievement.
     * @return The created achievement as a DTO.
     */
    AchievementResponseDTO createAchievement(AchievementRequestDTO requestDTO, String userId);

    /**
     * Updates an existing achievement.
     *
     * @param id The ID of the achievement to update.
     * @param requestDTO The DTO containing the updated data.
     * @param userId The ID of the user owning the achievement.
     * @return The updated achievement as a DTO.
     */
    AchievementResponseDTO updateAchievement(Long id, AchievementRequestDTO requestDTO, String userId);

    /**
     * Deletes an achievement.
     *
     * @param id The ID of the achievement to delete.
     * @param userId The ID of the user owning the achievement.
     * @return true if the achievement was deleted, false otherwise.
     */
    boolean deleteAchievement(Long id, String userId);
}
