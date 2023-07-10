package com.sofriend.mytest.util;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class JsonUtil {
    static ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
        
    public static String objectToString(Object t) throws Exception {
        return mapper.writeValueAsString(t);
    }
}
