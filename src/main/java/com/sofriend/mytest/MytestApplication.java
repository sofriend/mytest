package com.sofriend.mytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
@EnableAutoConfiguration
public class MytestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MytestApplication.class, args);
	}

	// @Bean
	// @Primary
	// public ObjectMapper objectMapper() {
	// 	// JavaTimeModule module = new JavaTimeModule();
	// 	// module.addSerializer();
	// 	// return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).registerModule(module);
	// 	ObjectMapper objectMapper = new ObjectMapper();
	// 	objectMapper.registerModule(new JavaTimeModule());
	// 	// objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	// 	return objectMapper;
	// }

	@Bean
	@Primary
	public ObjectMapper primaryObjectMapper() {
		return JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.addModule(new JavaTimeModule()).build();
	}
}
