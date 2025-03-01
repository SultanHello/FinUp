package org.example.aiservice.service;
import org.example.aiservice.client.GeminiClient;
import org.example.aiservice.connection.AiResponse;
import org.example.aiservice.connection.ContentText;
import org.example.aiservice.repository.ContentTextRepository;
import org.hibernate.internal.log.SubSystemLogging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import lombok.AllArgsConstructor;
import org.example.aiservice.connection.AiConnection;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service

public class AiService {
    private final String apiKey;
    private final GeminiClient geminiClient;
    private final ContentTextRepository contentTextRepository;

    public AiService(@Value("${gemini.apiKey}") String apiKey, ContentTextRepository contentTextRepository,GeminiClient geminiClient) {
        this.apiKey = apiKey;
        this.contentTextRepository = contentTextRepository;
        this.geminiClient= geminiClient;
    }


    public String connectAiCategory(String text){
        return connectAlgo(text,apiKey,"Classify the expense with one word based on the description. Always use the same category for similar cases. If an appropriate category has already been assigned in previous examples, reuse it. Use the most common or general term if multiple categories are possible.");
    }
    public String connectAiAdvice(String text){
        return connectAlgo(text,apiKey,"Now I send you some information from spending, you should answer with only one category, without newline characters.");

    }





    public String connectAlgo(String text, String apiKey, String prompt) {
        AiConnection aiConnection = new AiConnection();
        List<AiConnection.Content> contents = new ArrayList<>();

        // Добавляем предыдущий контент из репозитория
        List<ContentText> previousContents = contentTextRepository.findAll();

        if (!previousContents.isEmpty()) {
            // Загружаем все предыдущие категории и описания
            for (ContentText contentText : previousContents) {
                AiConnection.Content previousContent = new AiConnection.Content();
                previousContent.setRole("user");
                previousContent.setParts(List.of(new AiConnection.Part(contentText.getText())));
                contents.add(previousContent);
            }
        } else {
            // Если контента ещё нет, добавляем начальный промпт
            contentTextRepository.save(ContentText.builder().text(prompt).build());

            AiConnection.Content promptContent = new AiConnection.Content();
            promptContent.setRole("user");
            promptContent.setParts(List.of(new AiConnection.Part(prompt)));
            contents.add(promptContent);
        }

        // Добавляем новый запрос
        contentTextRepository.save(ContentText.builder().text(text).build());

        AiConnection.Content newContent = new AiConnection.Content();
        newContent.setRole("user");
        newContent.setParts(List.of(new AiConnection.Part(text)));
        contents.add(newContent);

        aiConnection.setContents(contents);
        ResponseEntity<AiResponse> response =geminiClient.sendRequest(aiConnection,apiKey);


        // Обрабатываем ответ и сохраняем категорию
        String responseCategory = response.getBody().getCandidates().get(
                response.getBody().getCandidates().size() - 1
        ).getContent().getParts().get(
                response.getBody().getCandidates().get(
                        response.getBody().getCandidates().size() - 1
                ).getContent().getParts().size() - 1
        ).getText().trim();

        // Сохраняем категорию в репозиторий для последующих запросов
        contentTextRepository.save(ContentText.builder().text(responseCategory).build());

        System.out.println("Категория: " + responseCategory);
        return responseCategory;
    }
}
