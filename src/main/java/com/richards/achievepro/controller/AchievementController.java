package com.richards.achievepro.controller;

import com.richards.achievepro.dto.request.AchievementRequestDTO;
import com.richards.achievepro.dto.response.AchievementResponseDTO;
import com.richards.achievepro.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*") // FIX: Allows requests from any origin (e.g., your frontend)
public class AchievementController {


    private final AchievementService achievementService;

    @Value("${vk.app.secret-key}")
    private String APP_SECRET_KEY;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    private String getAuthenticatedUserId(String launchParams) {
        if (launchParams == null || launchParams.isEmpty()) {
            throw new SecurityException("Launch parameters are missing.");
        }

        // Handle mock data for preview environment
        if ("?vk_user_id=12345&sign=mock_sign".equals(launchParams)) {
            return "12345";
        }

        Map<String, String> params = Stream.of(launchParams.replace("?", "").split("&"))
                .filter(s -> s.contains("="))
                .map(s -> s.split("=", 2))
                .collect(Collectors.toMap(
                        a -> urlDecode(a[0]),
                        a -> urlDecode(a[1])
                ));

        String sign = params.remove("sign");
        if (sign == null || sign.isEmpty()) {
            throw new SecurityException("Signature is missing from launch parameters.");
        }

        String checkString = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String expectedSign = calculateSign(checkString, APP_SECRET_KEY);

        if (!expectedSign.equals(sign)) {
            throw new SecurityException("Signature validation failed. The request is not authentic.");
        }

        String userId = params.get("vk_user_id");
        if (userId == null || userId.isEmpty()) {
            throw new SecurityException("User ID (vk_user_id) not found in launch parameters.");
        }
        return userId;
    }

    private String calculateSign(String str, String secret) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] hmacBytes = mac.doFinal(str.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate signature", e);
        }
    }

    private String urlDecode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to URL decode value: " + value, e);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createAchievement(
            @RequestBody AchievementRequestDTO requestDTO,
            @RequestHeader("X-VK-Launch-Params") String launchParams) {
        try {
            String userId = getAuthenticatedUserId(launchParams);
            AchievementResponseDTO createdAchievement = achievementService.createAchievement(requestDTO, userId);
            return new ResponseEntity<>(createdAchievement, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAchievementsForCurrentUser(
            @RequestHeader("X-VK-Launch-Params") String launchParams) {
        try {
            String userId = getAuthenticatedUserId(launchParams);
            List<AchievementResponseDTO> achievements = achievementService.getAllAchievementsByUserId(userId);
            return new ResponseEntity<>(achievements, HttpStatus.OK);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAchievementByIdForCurrentUser(
            @PathVariable Long id,
            @RequestHeader("X-VK-Launch-Params") String launchParams) {
        try {
            String userId = getAuthenticatedUserId(launchParams);
            Optional<AchievementResponseDTO> achievement = achievementService.getAchievementByIdAndUserId(id, userId);
            return achievement.<ResponseEntity<?>>map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAchievement(
            @PathVariable Long id,
            @RequestBody AchievementRequestDTO requestDTO,
            @RequestHeader("X-VK-Launch-Params") String launchParams) {
        try {
            String userId = getAuthenticatedUserId(launchParams);
            AchievementResponseDTO updatedAchievement = achievementService.updateAchievement(id, requestDTO, userId);
            return new ResponseEntity<>(updatedAchievement, HttpStatus.OK);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAchievement(
            @PathVariable Long id,
            @RequestHeader("X-VK-Launch-Params") String launchParams) {
        try {
            String userId = getAuthenticatedUserId(launchParams);
            boolean deleted = achievementService.deleteAchievement(id, userId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
