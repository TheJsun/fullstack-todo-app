package com.jason.todoapp.fullstack_todo_app.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();
    mapper
      .getConfiguration()
      .setSkipNullEnabled(true)
      .setPreferNestedProperties(false)
      .setMatchingStrategy(MatchingStrategies.STRICT);

    mapper.addConverter(
      ctx -> {
        String source = ctx.getSource();
        return source == null ? null : source.trim().replaceAll("//s+", " ");
      },
      String.class,
      String.class
    );

    return mapper;
  }
}
