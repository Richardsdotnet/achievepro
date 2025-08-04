package com.richards.achievepro.service;

import com.richards.achievepro.achievementModels.Achievement;
import com.richards.achievepro.dto.request.AchievementRequestDTO;
import com.richards.achievepro.dto.response.AchievementResponseDTO;
import com.richards.achievepro.mapper.AchievementMapper;
import com.richards.achievepro.repository.AchievementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AchievementServiceImpl implements AchievementServiceInterface {

    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    public AchievementServiceImpl(AchievementRepository achievementRepository, AchievementMapper achievementMapper) {
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AchievementResponseDTO> getAllAchievementsByUserId(String userId) {
        List<Achievement> achievements = achievementRepository.findByUserId(userId);
        return achievementMapper.toDtoList(achievements);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AchievementResponseDTO> getAchievementByIdAndUserId(Long id, String userId) {
        return achievementRepository.findByIdAndUserId(id, userId)
                .map(achievementMapper::toDto);
    }

    @Override
    @Transactional
    public AchievementResponseDTO createAchievement(AchievementRequestDTO requestDTO, String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID must not be null when creating an achievement.");
        }
        Achievement achievement = achievementMapper.toEntity(requestDTO);
        achievement.setUserId(userId);
        Achievement savedAchievement = achievementRepository.save(achievement);
        return achievementMapper.toDto(savedAchievement);
    }

    @Override
    @Transactional
    public AchievementResponseDTO updateAchievement(Long id, AchievementRequestDTO requestDTO, String userId) {
        return achievementRepository.findByIdAndUserId(id, userId)
                .map(existingAchievement -> {
                    existingAchievement.setTitle(requestDTO.getTitle());
                    existingAchievement.setDescription(requestDTO.getDescription());
                    existingAchievement.setRating(requestDTO.getRating());
                    existingAchievement.setDateAchieved(requestDTO.getDateAchieved());
                    Achievement updatedAchievement = achievementRepository.save(existingAchievement);
                    return achievementMapper.toDto(updatedAchievement);
                })
                .orElseThrow(() -> new RuntimeException("Achievement not found or not authorized for update."));
    }

    @Override
    @Transactional
    public boolean deleteAchievement(Long id, String userId) {
        if (achievementRepository.existsByIdAndUserId(id, userId)) {
            achievementRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
