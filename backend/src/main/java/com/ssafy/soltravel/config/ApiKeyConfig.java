package com.ssafy.soltravel.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApiKeyConfig {

    @Value("${team.private.key}")
    private String privateKey;

    private final Map<String, String> apiKeys = new HashMap<String, String>();

    @Bean
    public Map<String, String> apiKeys() {
        return apiKeys;
    }

    @PostConstruct
    public void init() {
        System.out.println(privateKey);
        try {
            ClassPathResource resource = new ClassPathResource("api-key-encrypted.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            String line;

            while((line = reader.readLine()) != null){
                if(line.isEmpty()) continue;
                line = decrypt(line.trim());

                String[] parts = line.split(":", 2);
                if(parts.length != 2) continue;

                parts[0] = parts[0].trim();
                parts[1] = parts[1].trim();
                apiKeys.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load API keys", e);
        }

        for(String apiKey : apiKeys.keySet()){
            System.out.println(apiKey + ": "+apiKeys.get(apiKey));
        }
    }

    private String decrypt(String encrypted){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt API key", e);
        }
    }
}
