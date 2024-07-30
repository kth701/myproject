package com.spring.myproject.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;

@Configuration
public class RootConfig {
  @Bean
  public ModelMapper getMapperMapper(){
    ModelMapper modelMapper = new ModelMapper();

    modelMapper.getConfiguration()
              .setFieldMatchingEnabled(true)
              .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
              .setMatchingStrategy(MatchingStrategies.STRICT);

    return modelMapper;
  }
}
