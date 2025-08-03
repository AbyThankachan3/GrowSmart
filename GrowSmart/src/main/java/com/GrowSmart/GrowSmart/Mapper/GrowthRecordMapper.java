package com.GrowSmart.GrowSmart.Mapper;

import com.GrowSmart.GrowSmart.DTO.GrowthRecordDTO;
import com.GrowSmart.GrowSmart.Entity.GrowthRecord;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GrowthRecordMapper {

    private final ModelMapper modelMapper;

    public GrowthRecordMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GrowthRecordDTO toDTO(GrowthRecord entity) {
        return modelMapper.map(entity, GrowthRecordDTO.class);
    }

    public GrowthRecord toEntity(GrowthRecordDTO dto) {
        return modelMapper.map(dto, GrowthRecord.class);
    }
}
