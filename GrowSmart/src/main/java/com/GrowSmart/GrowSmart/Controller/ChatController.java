package com.GrowSmart.GrowSmart.Controller;

import com.GrowSmart.GrowSmart.DTO.ChatRequest;
import com.GrowSmart.GrowSmart.Service.AzureOpenAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/azure-chat")
public class ChatController {
    private final AzureOpenAIService openAI;

    public ChatController(AzureOpenAIService openAI) { this.openAI = openAI; }

    @PostMapping("/reply")
    public ResponseEntity<String> reply(@RequestBody ChatRequest req) {
        String resp = openAI.chat(req.getMessages(), req.getMaxTokens(), false);
        return ResponseEntity.ok(resp);
    }
}

