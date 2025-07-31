package com.richards.achievepro.controller;

import com.richards.achievepro.dto.request.AchievementRequestDTO;
import com.richards.achievepro.dto.response.AchievementResponseDTO;
import com.richards.achievepro.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AchievementController {

    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    // Endpoint to get all achievements
    @GetMapping("/all")
    public List<AchievementResponseDTO> getAllAchievements() {
        return achievementService.getAllAchievements();
    }

    // Endpoint to get a single achievement by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<AchievementResponseDTO> getAchievementById(@PathVariable Long id) {
        return achievementService.getAchievementById(id)
                .map(achievement -> new ResponseEntity<>(achievement, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to create a new achievement (consumes JSON)
    @PostMapping("/create")
    public ResponseEntity<AchievementResponseDTO> createAchievement(
            @RequestBody AchievementRequestDTO requestDTO) {
        AchievementResponseDTO createdAchievement = achievementService.createAchievement(requestDTO);
        return new ResponseEntity<>(createdAchievement, HttpStatus.CREATED);
    }

    // Endpoint to update an existing achievement (consumes JSON)
    @PutMapping("/update/{id}")
    public ResponseEntity<AchievementResponseDTO> updateAchievement(
            @PathVariable Long id,
            @RequestBody AchievementRequestDTO requestDTO) {
        AchievementResponseDTO updatedAchievement = achievementService.updateAchievement(id, requestDTO);
        if (updatedAchievement != null) {
            return new ResponseEntity<>(updatedAchievement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete an achievement
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        if (achievementService.deleteAchievement(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
