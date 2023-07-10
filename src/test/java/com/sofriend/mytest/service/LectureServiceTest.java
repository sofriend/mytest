package com.sofriend.mytest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.sofriend.mytest.vo.Lecture;

@SpringBootTest
@Rollback(true)
public class LectureServiceTest {
    @Autowired
    private LectureService lectureService;

    @Test
    public void testSave() {
        Instant start = Instant.now().plus(3,ChronoUnit.DAYS);
        Lecture lecture = Lecture.builder()
                            .hall("대강연장")
                            .lecturer("임꺽정")
                            .title("lt옛이야기")
                            .description("강연 설명 자세히")
                            .limit(10)
                            .startat(Instant.now().plus(3,ChronoUnit.DAYS))
                            .endat(start.plus(2, ChronoUnit.HOURS))
                            .build();
        lectureService.save(lecture);

        assertTrue(lectureService.list().size() > 0);
    }
}
