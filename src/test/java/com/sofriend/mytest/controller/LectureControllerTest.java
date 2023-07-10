package com.sofriend.mytest.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LectureControllerTest {
    Logger log = LoggerFactory.getLogger(getClass());
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    @BeforeAll
    void setup() throws Exception {
        mvc.perform(put("/person/add").param("account", "90000")).andExpect(status().isOk());
        mvc.perform(put("/person/add").param("account", "90001")).andExpect(status().isOk());
        mvc.perform(put("/person/add").param("account", "90002")).andExpect(status().isOk());
        mvc.perform(put("/person/add").param("account", "90003")).andExpect(status().isOk());

        Instant start = Instant.now().plus(6, ChronoUnit.DAYS);
        Instant end = start.plus(2, ChronoUnit.HOURS);
        mvc.perform(put("/manage/lecture/add")
                    .param("title", "test강연제목")
                    .param("lecturer", "test강연자이름")
                    .param("hall", "test강연장명")
                    .param("limit", "20")
                    .param("startat", start.toString())
                    .param("endat", end.toString())
                    .param("description", "test강연설명")
        ).andExpect(status().isOk());

        start = Instant.now().plus(6, ChronoUnit.DAYS);
        end = start.plus(2, ChronoUnit.HOURS);
        mvc.perform(put("/manage/lecture/add")
                    .param("title", "강연제목2")
                    .param("lecturer", "강연자이름2")
                    .param("hall", "강연장명2")
                    .param("limit", "20")
                    .param("startat", start.toString())
                    .param("endat", end.toString())
                    .param("description", "강연설명2")
        ).andExpect(status().isOk());

        start = Instant.now().plus(8, ChronoUnit.DAYS);
        end = start.plus(2, ChronoUnit.HOURS);
        mvc.perform(put("/manage/lecture/add")
                    .param("title", "강연제목3")
                    .param("lecturer", "강연자이름3")
                    .param("hall", "강연장명3")
                    .param("limit", "20")
                    .param("startat", start.toString())
                    .param("endat", end.toString())
                    .param("description", "강연설명2")
        ).andExpect(status().isOk());

        // 수강신청
        mvc.perform(post("/lecture/1/signup").param("account", "90001")).andExpect(status().isOk());
        mvc.perform(post("/lecture/2/signup").param("account", "90001")).andExpect(status().isOk());
        mvc.perform(post("/lecture/3/signup").param("account", "90001")).andExpect(status().isOk());
        mvc.perform(post("/lecture/1/signup").param("account", "90002")).andExpect(status().isOk());
        mvc.perform(post("/lecture/1/signup").param("account", "90003")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("강연등록 - /manage/lecture/add")
    void test1LectureAdd() throws Exception {
        Instant start = Instant.now().plus(6, ChronoUnit.DAYS);
        Instant end = start.plus(2, ChronoUnit.HOURS);

        MvcResult rtn = mvc.perform(put("/manage/lecture/add")
                    .param("title", "강연제목")
                    .param("lecturer", "강연자이름")
                    .param("hall", "강연장명")
                    .param("limit", "20")
                    .param("startat", start.toString())
                    .param("endat", end.toString())
                    .param("description", "강연설명")
        ).andExpect(status().isOk()).andReturn();

        JsonNode jsonNode = objectMapper.readTree(rtn.getResponse().getContentAsString());
        log.info("return : \n{}", jsonNode.toPrettyString());
        assertTrue(jsonNode.get("id").asInt() > 0);
    }

    @Test
    @DisplayName("수강신청 - /lecture/{lectureId}/signup")
    void test2LectureSignup() throws Exception {
        MvcResult rtn = mvc.perform(post("/lecture/1/signup")
                    .param("account", "90000")
        ).andExpect(status().isOk()).andReturn();

        JsonNode jsonNode = objectMapper.readTree(rtn.getResponse().getContentAsString());
        log.info("return : \n{}", jsonNode.toPrettyString());
        assertEquals(1, jsonNode.get("lecture").get("id").asInt());
    }

    @Test
    @DisplayName("강연목록 - /lecture/list")
    void testLectureList() throws Exception {
        MvcResult rtn = mvc.perform(get("/lecture/list")
        ).andExpect(status().isOk()).andReturn();

        JsonNode jsonNode = objectMapper.readTree(rtn.getResponse().getContentAsString());
        log.info("return : \n{}", jsonNode.toPrettyString());
        assertTrue(jsonNode.size() > 0);
    }

    @Test
    @DisplayName("내가신청한 강연목록 - /lecture/list/signup")
    void testLectureMylist() throws Exception {
        MvcResult rtn = mvc.perform(get("/lecture/list/signup").param("account", "90001")
        ).andExpect(status().isOk()).andReturn();

        JsonNode jsonNode = objectMapper.readTree(rtn.getResponse().getContentAsString());
        log.info("return : \n{}", jsonNode.toPrettyString());
        assertTrue(jsonNode.size() > 0);
    }

    @Test
    @DisplayName("강연신청한 사번목록 - /manage/lecture/{lectureId}/person")
    void testLecturePersonList() throws Exception {
        MvcResult rtn = mvc.perform(get("/manage/lecture/1/person")
        ).andExpect(status().isOk()).andReturn();

        JsonNode jsonNode = objectMapper.readTree(rtn.getResponse().getContentAsString());
        log.info("return : \n{}", jsonNode.toPrettyString());
        assertTrue(jsonNode.size() > 0);
    }

    @Test
    @DisplayName("3일간 인기강연목록 - /lecture/list/favorite")
    void testLectureFavorite() throws Exception {
        MvcResult rtn = mvc.perform(get("/lecture/list/favorite")
        ).andExpect(status().isOk()).andReturn();

        JsonNode jsonNode = objectMapper.readTree(rtn.getResponse().getContentAsString());
        log.info("return : \n{}", jsonNode.toPrettyString());
        assertTrue(jsonNode.size() > 0);
    }

    @Test
    @DisplayName("강연신청 취소 - /lecture/{lectureId}/cancel")
    void testLectureCancel() throws Exception {
        ///lecture/{lectureId}/cancel
        mvc.perform(post("/lecture/3/cancel").param("account", "90001")).andExpect(status().isOk());

        MvcResult rtn = mvc.perform(get("/manage/lecture/3/person")).andExpect(status().isOk()).andReturn();
        JsonNode jsonNode = objectMapper.readTree(rtn.getResponse().getContentAsString());
        log.info("return : \n{}", jsonNode.toPrettyString());
        assertTrue(jsonNode.size() == 0);
    }

    @Test
    @DisplayName("강연 삭제 - /manage/lecture/{lectureId}/delete")
    void testLectureDelete() {
        try {

            mvc.perform(delete("/manage/lecture/3/delete")).andExpect(status().isOk());
            MvcResult rtn = mvc.perform(get("/lecture/3")).andExpect(status().isOk()).andReturn();
            JsonNode jsonNode = objectMapper.readTree(rtn.getResponse().getContentAsString());
            log.info("return : \n{}", jsonNode.toPrettyString());
            assertTrue(jsonNode.size() == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
