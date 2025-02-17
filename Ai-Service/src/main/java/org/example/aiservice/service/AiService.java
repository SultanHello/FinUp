package org.example.aiservice.service;
import org.example.aiservice.connection.AiResponse;
import org.example.aiservice.connection.ContentText;
import org.example.aiservice.repository.ContentTextRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import lombok.AllArgsConstructor;
import org.example.aiservice.connection.AiConnection;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service

public class AiService {
    private final String apiKey;
    private final ContentTextRepository contentTextRepository;

    public AiService(@Value("${gemini.apiKey}") String apiKey, ContentTextRepository contentTextRepository) {
        this.apiKey = apiKey;
        this.contentTextRepository = contentTextRepository;
    }


    public String connectAiCategory(String text){
        return connectAlgo(text,apiKey,"Now I send you some information from spending, you should answer with only one category, without newline characters,try to make it into one category if it is similar to the previous categories");

    }
    public String connectAiAdvice(String text){
        return connectAlgo(text,apiKey,"Now I send you some information from spending, you should answer with only one category, without newline characters.");

    }





    public String connectAlgo(String text,String apiKey,String prompt) {

        HttpHeaders headers = new HttpHeaders();
        AiConnection aiConnection = new AiConnection();
        List<AiConnection.Content> contents = new ArrayList<>();
        contentTextRepository.save(ContentText.builder().text(text).build());


        if(contentTextRepository.findAll().size()>2){

            for(int i = 0;i<contentTextRepository.findAll().size()-1;i++){
                AiConnection.Content userContent1=new AiConnection.Content();
                userContent1.setRole("user");
                userContent1.setParts(List.of(new AiConnection.Part(contentTextRepository.findAll().get(i).getText())));
                contents.add(userContent1);
            }
        }else{
            contentTextRepository.save(ContentText.builder().text(prompt).build());
            contentTextRepository.save(ContentText.builder().text(text).build());

            AiConnection.Content userContent1=new AiConnection.Content();
            userContent1.setRole("user");
            userContent1.setParts(List.of(new AiConnection.Part(prompt)));

            AiConnection.Content userContent2 = new AiConnection.Content();
            userContent2.setRole("user");
            userContent2.setParts(List.of(new AiConnection.Part(text)));
            aiConnection.setContents(List.of(userContent1, userContent2));

        }


        aiConnection.setContents(contents);

        RestTemplate restTemplate = new RestTemplate();

        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("Authorization", "Bearer " +map.get("token"));
        HttpEntity<AiConnection> entity = new HttpEntity<>(aiConnection, headers);


        ResponseEntity<AiResponse> response= restTemplate.exchange(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="+apiKey,
                HttpMethod.POST,
                entity,
                AiResponse.class
        );
        String responseCategory = response.getBody().getCandidates().get(response.getBody().getCandidates().size()-1).getContent().getParts()
                .get(response.getBody().getCandidates().get(response.getBody().getCandidates().size()-1).getContent().getParts().size()-1).getText();
        return responseCategory;




    }
}
