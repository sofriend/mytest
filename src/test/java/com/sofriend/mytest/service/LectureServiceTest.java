package com.sofriend.mytest.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.DisplayName;
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
    @Autowired
    private PersonService personService;
    @Autowired
    private SubscribeService subscribeService;

    @Test
    @DisplayName("강연등록")
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

    @Test
    @DisplayName("강연삭제")
    public void testDelete() {
        Instant start = Instant.now().plus(3,ChronoUnit.DAYS);
        Lecture lecture = Lecture.builder()
                            .hall("대강연장")
                            .lecturer("임꺽정")
                            .title("lt옛이야기1")
                            .description("강연 설명 자세히")
                            .limit(10)
                            .startat(Instant.now().plus(3,ChronoUnit.DAYS))
                            .endat(start.plus(2, ChronoUnit.HOURS))
                            .build();
        lectureService.save(lecture);

        personService.save("91001");
        subscribeService.save(lecture.getId(), "91001");
        assertTrue(lectureService.findById(lecture.getId()) != null);

        lectureService.delete(lecture.getId());
        assertTrue(lectureService.findById(lecture.getId()) == null);
    }
}
