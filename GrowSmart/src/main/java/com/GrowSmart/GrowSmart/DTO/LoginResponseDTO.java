package com.GrowSmart.GrowSmart.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String success;
    private String message;
    private String role;
}
