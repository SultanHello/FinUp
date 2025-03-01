package org.example.userservice.client;


import org.example.userservice.dto.ReportDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service

public class BudgetClient {
    public List<ReportDTO> getReports(Long id){
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<ReportDTO> response = restTemplate.exchange(
                "http://budget-service:3333/api/budget/report/user/"+id,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ReportDTO>>() {}
        ).getBody();
        return response;
    }

    public ReportDTO getReportLast(Long id){
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ReportDTO response = restTemplate.exchange(
                "http://budget-service:3333/api/budget/report/last/user/"+id,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<ReportDTO>() {}
        ).getBody();
        return response;
    }
}
