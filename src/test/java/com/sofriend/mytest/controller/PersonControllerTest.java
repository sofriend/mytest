package com.sofriend.mytest.controller;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
    Logger log = LoggerFactory.getLogger(getClass());
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    @Test
    void testPersonAdd() throws Exception {
        MvcResult rtn = mvc.perform(put("/person/add")
                    .param("account", "91001")
        ).andExpect(status().isOk()).andReturn();
        
        JsonNode jsonNode = objectMapper.readTree(rtn.getResponse().getContentAsString());
        log.info("return : \n{}", jsonNode.toPrettyString());
    }
}
