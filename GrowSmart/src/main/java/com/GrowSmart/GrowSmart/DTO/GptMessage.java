package com.GrowSmart.GrowSmart.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptMessage {
    private String role;
    private String content;
}

