package org.example.transactionservice.client;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AiClient {
    public String getCategory(String description){
        RestTemplate restTemplate= new RestTemplate();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://ai-service:2222/ai/category?text="+ URLEncoder.encode(description, StandardCharsets.UTF_8),
                HttpMethod.POST,
                entity,
                String.class
        );
        return response.getBody();
    }
}
