package com.GrowSmart.GrowSmart.Mapper;

import com.GrowSmart.GrowSmart.DTO.BehaviorLogDTO;
import com.GrowSmart.GrowSmart.Entity.BehaviorLog;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BehaviorLogMapper {

    private final ModelMapper modelMapper;

    public BehaviorLogMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        this.modelMapper.typeMap(BehaviorLog.class, BehaviorLogDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getChild().getId(), BehaviorLogDTO::setChildId);
            mapper.map(src -> src.getLoggedBy().getId(), BehaviorLogDTO::setLoggedByUserId);
        });

        this.modelMapper.typeMap(BehaviorLogDTO.class, BehaviorLog.class).addMappings(mapper -> {
            mapper.skip(BehaviorLog::setChild);
            mapper.skip(BehaviorLog::setLoggedBy);
        });
    }

    public BehaviorLogDTO toDTO(BehaviorLog entity) {
        return modelMapper.map(entity, BehaviorLogDTO.class);
    }

    public BehaviorLog toEntity(BehaviorLogDTO dto) {
        return modelMapper.map(dto, BehaviorLog.class);
    }
}
