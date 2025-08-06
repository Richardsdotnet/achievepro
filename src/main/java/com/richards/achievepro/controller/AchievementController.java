package com.richards.achievepro.controller;

import com.richards.achievepro.dto.request.AchievementRequestDTO;
import com.richards.achievepro.dto.response.AchievementResponseDTO;
import com.richards.achievepro.service.AchievementServiceInterface;
import com.richards.achievepro.service.VkAuthServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*")
public class AchievementController {

    private final AchievementServiceInterface achievementService;
    private final VkAuthServiceImpl vkAuthService;

    public AchievementController(AchievementServiceInterface achievementService, VkAuthServiceImpl vkAuthService) {
        this.achievementService = achievementService;
        this.vkAuthService = vkAuthService;
    }

    @PostMapping("/create")
    public ResponseEntity<AchievementResponseDTO> createAchievement(
            @RequestBody AchievementRequestDTO requestDTO,
            @RequestHeader("X-VK-Launch-Params") String launchParams) {
        String userId = vkAuthService.getAuthenticatedUserId(launchParams);
        AchievementResponseDTO createdAchievement = achievementService.createAchievement(requestDTO, userId);
        return new ResponseEntity<>(createdAchievement, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AchievementResponseDTO>> getAllAchievementsForCurrentUser(
            @RequestHeader("X-VK-Launch-Params") String launchParams) {
        String userId = vkAuthService.getAuthenticatedUserId(launchParams);
        List<AchievementResponseDTO> achievements = achievementService.getAllAchievementsByUserId(userId);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<AchievementResponseDTO> getAchievementByIdForCurrentUser(
            @PathVariable Long id,
            @RequestHeader("X-VK-Launch-Params") String launchParams) {
        String userId = vkAuthService.getAuthenticatedUserId(launchParams);
        return achievementService.getAchievementByIdAndUserId(id, userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AchievementResponseDTO> updateAchievement(
            @PathVariable Long id,
            @RequestBody AchievementRequestDTO requestDTO,
            @RequestHeader("X-VK-Launch-Params") String launchParams) {
        String userId = vkAuthService.getAuthenticatedUserId(launchParams);
        AchievementResponseDTO updatedAchievement = achievementService.updateAchievement(id, requestDTO, userId);
        return ResponseEntity.ok(updatedAchievement);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAchievement(
            @PathVariable Long id,
            @RequestHeader("X-VK-Launch-Params") String launchParams) {
        String userId = vkAuthService.getAuthenticatedUserId(launchParams);
        boolean deleted = achievementService.deleteAchievement(id, userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
