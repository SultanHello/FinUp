package org.example.budgetservice.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class UserClient {
    public List<Long> userIds(){
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<Long> response = restTemplate.exchange(
                "http://user-service:8080/users/id",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Long>>() {}
        ).getBody();
        return response;
    }
}
