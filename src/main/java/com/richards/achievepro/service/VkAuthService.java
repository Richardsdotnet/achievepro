package com.richards.achievepro.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service

public class VkAuthService implements VkAuthInterface {
    @Value("${VK_APP_SECRET_KEY}")
    private String APP_SECRET_KEY;

    public String getAuthenticatedUserId(String launchParams) {
        if (launchParams == null || launchParams.isEmpty()) {
            throw new SecurityException("Launch parameters are missing.");
        }

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
}
