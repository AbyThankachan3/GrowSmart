package com.GrowSmart.GrowSmart.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptRequest {
    private List<GptMessage> messages;
    private int max_tokens = 50;
    private double temperature = 0.2;
}
