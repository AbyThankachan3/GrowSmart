package com.GrowSmart.GrowSmart.DTO;

import com.GrowSmart.GrowSmart.Entity.Child;
import com.GrowSmart.GrowSmart.Entity.User;
import com.GrowSmart.GrowSmart.Enums.Role;
import com.GrowSmart.GrowSmart.Enums.Standard;
import com.GrowSmart.GrowSmart.Mapper.ChildMapper;
import com.GrowSmart.GrowSmart.Repository.ChildRepository;
import com.GrowSmart.GrowSmart.Repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildDTO {
    @JsonSetter(nulls = Nulls.SKIP)
    private UUID id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String guardianEmail; // optional
    private String studentCode;
    @Enumerated(EnumType.STRING)
    private Standard standard;

}

