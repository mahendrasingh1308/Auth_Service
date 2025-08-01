package com.auth.service.service;

import com.auth.service.dto.OtplessUser;
import com.auth.service.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OtplessService {

    @Value("${otpless.client-id}")
    private String clientId;

    @Value("${otpless.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

//    public OtplessUser verifyOtplessToken(String token) {
//        try {
//            // Step 1: Encode credentials
//            String auth = clientId + ":" + clientSecret;
//            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
//
//            // Step 2: Set headers
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            headers.set("Authorization", "Basic " + encodedAuth);
//
//            // Step 3: Set body
//            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//            body.add("token", token);
//
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//
//            // Step 4: Call OTPless introspect API
//            ResponseEntity<Map> response = restTemplate.postForEntity(
//                    "https://auth.otpless.com/oauth/token/introspect", request, Map.class);
//
//            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
//                throw new CustomException("Failed to verify OTPless token.");
//            }
//
//            Map<String, Object> data = response.getBody();
//
//            // Step 5: Validate essential fields
//            String email = (String) data.get("email");
//            String phone = (String) data.get("phone_number");
//            String channel = (String) data.get("channel");
//
//            if (phone == null || channel == null) {
//                throw new CustomException("Invalid OTPless data received.");
//            }
//
//            // Step 6: Map to DTO
//            OtplessUser user = new OtplessUser();
//            user.setEmail(email);
//            user.setPhone(phone);
//            user.setChannel(channel);
//
//            return user;
//        } catch (Exception e) {
//            throw new CustomException("OTPless verification failed: " + e.getMessage());
//        }
//    }

    public OtplessUser verifyOtplessToken(String token) {
        if (token.equals("demo123")) {
            OtplessUser user = new OtplessUser();
            user.setEmail("demo@otpless.com");
            user.setPhone("9999999999");
            user.setChannel("whatsapp");
            return user;
        }

        throw new CustomException("Invalid demo token.");
    }



}
