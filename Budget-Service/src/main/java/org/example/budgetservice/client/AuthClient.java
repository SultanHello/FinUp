package org.example.budgetservice.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class AuthClient {
    public String getUsername(Long id){
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        System.out.println("1234098 ");

        String response = restTemplate.exchange(
                "http://auth-service:8021/auth/username/"+id,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<String>() {}
        ).getBody();
        System.out.println("1234098 + "+response);
        return response;
    }
}
