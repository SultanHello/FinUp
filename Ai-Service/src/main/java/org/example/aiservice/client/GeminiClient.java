package org.example.aiservice.client;

import org.example.aiservice.connection.AiConnection;
import org.example.aiservice.connection.AiResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class GeminiClient {
    public ResponseEntity<AiResponse> sendRequest(AiConnection aiConnection,String apiKey){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiConnection> entity = new HttpEntity<>(aiConnection, headers);


        ResponseEntity<AiResponse> response = restTemplate.exchange(
                "https://generativelanguage.googleapis.com/v1beta/models/text-bison-001:generateContent?key=" + apiKey,
                HttpMethod.POST,
                entity,
                AiResponse.class
        );

        return response;

    }
}
