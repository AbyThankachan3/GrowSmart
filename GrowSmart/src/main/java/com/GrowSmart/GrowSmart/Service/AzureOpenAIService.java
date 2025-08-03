package com.GrowSmart.GrowSmart.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AzureOpenAIService {
    @Value("${azure.openai.endpoint}") private String endpoint;
    @Value("${azure.openai.deploymentName}") private String deployment;
    @Value("${azure.openai.apiKey}") private String apiKey;
    @Value("${azure.openai.apiVersion}") private String apiVersion;

    private final RestTemplate rest;

    public AzureOpenAIService(RestTemplate rest) { this.rest = rest; }

    public String chat(List<Map<String, String>> messages, int maxTokens, boolean stream) {
        String url = String.format("%s/openai/deployments/%s/chat/completions?api-version=%s",
                endpoint, deployment, apiVersion);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = Map.of(
                "messages", messages,
                "max_tokens", maxTokens,
                "stream", stream
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);
        ResponseEntity<String> resp = rest.postForEntity(url, req, String.class);
        return resp.getBody();
    }
}
