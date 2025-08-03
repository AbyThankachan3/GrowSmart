package com.GrowSmart.GrowSmart.Mapper;

import com.GrowSmart.GrowSmart.DTO.RecommendationDTO;
import com.GrowSmart.GrowSmart.Entity.Recommendation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RecommendationMapper {

    private final ModelMapper modelMapper;

    public RecommendationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RecommendationDTO toDTO(Recommendation entity) {
        return modelMapper.map(entity, RecommendationDTO.class);
    }

    public Recommendation toEntity(RecommendationDTO dto) {
        return modelMapper.map(dto, Recommendation.class);
    }
}