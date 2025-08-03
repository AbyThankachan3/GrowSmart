package com.GrowSmart.GrowSmart.Mapper;

import com.GrowSmart.GrowSmart.DTO.ChildDTO;
import com.GrowSmart.GrowSmart.Entity.Child;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ChildMapper {

    private final ModelMapper modelMapper;

    public ChildMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        // Map Child → ChildDTO (custom logic if needed later)
        this.modelMapper.typeMap(Child.class, ChildDTO.class).addMappings(mapper -> {
            mapper.skip(ChildDTO::setGuardianEmail); // handled manually if needed
        });

        // Map ChildDTO → Child, skip guardian (set manually in service)
        this.modelMapper.typeMap(ChildDTO.class, Child.class).addMappings(mapper -> {
            mapper.skip(Child::setGuardian); // will be resolved using guardianEmail in service
        });
    }

    public ChildDTO toDTO(Child entity) {
        ChildDTO dto = modelMapper.map(entity, ChildDTO.class);
        if (entity.getGuardian() != null) {
            dto.setGuardianEmail(entity.getGuardian().getEmail()); // manually populate email
        }
        return dto;
    }

    public Child toEntity(ChildDTO dto) {
        return modelMapper.map(dto, Child.class);
    }
}
