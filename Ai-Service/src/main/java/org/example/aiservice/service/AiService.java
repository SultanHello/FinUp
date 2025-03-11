package org.example.aiservice.service;

import org.example.aiservice.client.GeminiClient;
import org.example.aiservice.connection.AiConnection;
import org.example.aiservice.connection.AiResponse;
import org.example.aiservice.connection.ContentText;
import org.example.aiservice.repository.ContentTextRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiService {
    private final String apiKey;
    private final GeminiClient geminiClient;
    private final ContentTextRepository contentTextRepository;

    public AiService(@Value("${gemini.apiKey}") String apiKey, ContentTextRepository contentTextRepository, GeminiClient geminiClient) {
        this.apiKey = apiKey;
        this.contentTextRepository = contentTextRepository;
        this.geminiClient = geminiClient;
    }

    public String connectAiCategory(String text) {
        return connectAlgo(text, apiKey, "Classify the expense with one word based on the description. Always use the same category for similar cases. If an appropriate category has already been assigned in previous examples, reuse it. Use the most common or general term if multiple categories are possible.","category");
    }
    public String connectAiAdvice(String text) {
        return connectAlgo(text, apiKey,
                "Analyze the user’s expenses based on today’s and this week’s spending reports. Identify spending patterns, highlight unusual expenses, and offer concise financial advice on optimizing spending habits. The response should be within 150–200 words.",
                "advice");
    }

    public String connectAlgo(String text, String apiKey, String prompt,String type) {
        AiConnection aiConnection = new AiConnection();
        List<AiConnection.Content> contents = new ArrayList<>();
        List<ContentText> previousContents = contentTextRepository.findByType(type);

        // Загружаем предыдущий контекст
        if (previousContents.isEmpty()) {
            // Если контекста нет, добавляем промпт как "user"
            contentTextRepository.save(ContentText.builder()
                    .text(prompt)
                    .role("user")
                    .build());
            AiConnection.Content promptContent = new AiConnection.Content();
            promptContent.setRole("user");
            promptContent.setParts(List.of(new AiConnection.Part(prompt)));
            contents.add(promptContent);
        } else {
            // Добавляем предыдущие запросы и ответы с их ролями
            for (ContentText contentText : previousContents) {
                AiConnection.Content content = new AiConnection.Content();
                content.setRole(contentText.getRole());
                content.setParts(List.of(new AiConnection.Part(contentText.getText())));
                contents.add(content);
            }
        }

        // Добавляем новый запрос как "user"
        contentTextRepository.save(ContentText.builder()
                .text(text)
                .role("user")
                .build());
        AiConnection.Content newContent = new AiConnection.Content();
        newContent.setRole("user");
        newContent.setParts(List.of(new AiConnection.Part(text)));
        contents.add(newContent);

        // Отправляем запрос
        aiConnection.setContents(contents);
        ResponseEntity<AiResponse> response = geminiClient.sendRequest(aiConnection, apiKey);

        // Извлекаем ответ
        String responseCategory = response.getBody().getCandidates().get(
                response.getBody().getCandidates().size() - 1
        ).getContent().getParts().get(
                response.getBody().getCandidates().get(
                        response.getBody().getCandidates().size() - 1
                ).getContent().getParts().size() - 1
        ).getText().trim();

        // Сохраняем ответ как "assistant"
        contentTextRepository.save(ContentText.builder()
                .text(responseCategory)
                .role("assistant")
                .build());

        System.out.println("Категория: " + responseCategory);
        return responseCategory;
    }
}