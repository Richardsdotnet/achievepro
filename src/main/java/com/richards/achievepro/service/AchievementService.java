package com.richards.achievepro.service;


import com.richards.achievepro.mapper.AchievementMapper;
import com.richards.achievepro.dto.request.AchievementRequestDTO;
import com.richards.achievepro.dto.response.AchievementResponseDTO;
import com.richards.achievepro.achievementModels.Achievement;
import com.richards.achievepro.repository.AchievementRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    // Now the primary method to get achievements, always scoped by userId
    @Transactional(readOnly = true)
    public List<AchievementResponseDTO> getAllAchievementsByUserId(String userId) {
        List<Achievement> achievements = achievementRepository.findByUserId(userId);
        return achievementMapper.toDtoList(achievements);
    }

    // Always fetch by achievement ID and user ID for security
    @Transactional(readOnly = true)
    public Optional<AchievementResponseDTO> getAchievementByIdAndUserId(Long id, String userId) {
        return achievementRepository.findByIdAndUserId(id, userId)
                .map(achievementMapper::toDto);
    }

    @Transactional
    public AchievementResponseDTO createAchievement(AchievementRequestDTO requestDTO, String userId) {
        Achievement achievement = achievementMapper.toEntity(requestDTO);
        achievement.setUserId(userId);

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null when creating an achievement.");
        }

        Achievement savedAchievement = achievementRepository.save(achievement);
        return achievementMapper.toDto(savedAchievement);
    }

    @Transactional
    public AchievementResponseDTO updateAchievement(Long id, AchievementRequestDTO requestDTO, String userId) {
        // Find by ID AND userId to ensure the authenticated user owns this achievement
        Optional<Achievement> optionalAchievement = achievementRepository.findByIdAndUserId(id, userId);
        if (optionalAchievement.isPresent()) {
            Achievement existingAchievement = optionalAchievement.get();
            existingAchievement.setTitle(requestDTO.getTitle());
            existingAchievement.setDescription(requestDTO.getDescription());
            existingAchievement.setRating(requestDTO.getRating());
            existingAchievement.setDateAchieved(requestDTO.getDateAchieved());

            Achievement updatedAchievement = achievementRepository.save(existingAchievement);
            return achievementMapper.toDto(updatedAchievement);
        } else {
            throw new RuntimeException("Achievement not found or not authorized for update.");
        }
    }

    @Transactional
    public boolean deleteAchievement(Long id, String userId) {
        // Find by ID AND userId to ensure the authenticated user owns this achievement
        if (achievementRepository.existsByIdAndUserId(id, userId)) {
            achievementRepository.deleteById(id);
            return true;
        }
        return false;
    }
}