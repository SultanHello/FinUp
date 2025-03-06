package org.example.budgetservice.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class TransactionClient {
    public Map<String,Double> getReportWeekly(Long id){
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String,Double> response = restTemplate.exchange(
                "http://transaction-service:1111/transactions/reportWeekly/"+id,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String,Double>>() {}
        ).getBody();
        return response;
    }

    public Map<String,Double> getReportDaily(Long id){
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String,Double> response = restTemplate.exchange(
                "http://transaction-service:1111/transactions/reportDaily/"+id,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String,Double>>() {}
        ).getBody();
        return response;
    }


}
