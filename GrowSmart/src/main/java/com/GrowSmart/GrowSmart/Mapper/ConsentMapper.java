package com.GrowSmart.GrowSmart.Mapper;

import com.GrowSmart.GrowSmart.DTO.ConsentDTO;
import com.GrowSmart.GrowSmart.DTO.UserDTO;
import com.GrowSmart.GrowSmart.Entity.Consent;
import com.GrowSmart.GrowSmart.Entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConsentMapper {

    private final ModelMapper modelMapper;

    public ConsentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        this.modelMapper.typeMap(Consent.class, ConsentDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getChild().getId(), ConsentDTO::setChildId);
            mapper.map(src -> src.getUser().getId(), ConsentDTO::setUserId);
        });

        this.modelMapper.typeMap(ConsentDTO.class, Consent.class).addMappings(mapper -> {
            mapper.skip(Consent::setChild);
            mapper.skip(Consent::setUser);
        });
    }

    public ConsentDTO toDTO(Consent entity) {
        return modelMapper.map(entity, ConsentDTO.class);
    }

    public Consent toEntity(ConsentDTO dto) {
        return modelMapper.map(dto, Consent.class);
    }
}
