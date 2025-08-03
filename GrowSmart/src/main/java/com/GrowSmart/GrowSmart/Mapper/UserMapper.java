package com.GrowSmart.GrowSmart.Mapper;

import com.GrowSmart.GrowSmart.DTO.UserDTO;
import com.GrowSmart.GrowSmart.Entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

// 2. UserMapper.java
@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO toDTO(User entity) {
        return modelMapper.map(entity, UserDTO.class);
    }

    public User toEntity(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }
}