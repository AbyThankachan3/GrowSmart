package com.GrowSmart.GrowSmart.DTO;

import com.GrowSmart.GrowSmart.Enums.Standard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyStandardAssignmentDTO {
    @JsonSetter(nulls = Nulls.SKIP)
    private UUID id;
    private String facultyEmail;
    private Standard standard;
}
