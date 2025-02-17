package org.example.aiservice.controller;

import lombok.AllArgsConstructor;
import org.example.aiservice.config.RestConfiguration;
import org.example.aiservice.service.AiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/ai")
@AllArgsConstructor
public class AiController {
    private final AiService aiService;
    @PostMapping("/category")
    public String DoCategory(@RequestParam String text){
        return aiService.connectAiCategory(text);
    }


}
