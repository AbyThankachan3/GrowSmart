package com.GrowSmart.GrowSmart.Config;

import com.GrowSmart.GrowSmart.DTO.BehaviorLogDTO;
import com.GrowSmart.GrowSmart.DTO.ChildDTO;
import com.GrowSmart.GrowSmart.DTO.ConsentDTO;
import com.GrowSmart.GrowSmart.Entity.BehaviorLog;
import com.GrowSmart.GrowSmart.Entity.Child;
import com.GrowSmart.GrowSmart.Entity.Consent;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}



