package com.sofriend.mytest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.sofriend.mytest.util.JsonUtil;
import com.sofriend.mytest.vo.Lecture;
import com.sofriend.mytest.vo.Person;

@SpringBootTest
@Rollback(true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubscribeServiceTest {
    Logger log = LoggerFactory.getLogger(getClass());

    Long lectureId;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private PersonService personService;

    @Autowired
    private LectureService lectureService;

    @BeforeAll
    public void setup() {
        // person add
        for (int base=10; base<20; base++) {
            for (int i=0; i<20; i++) {
                personService.save(Person.builder().account(String.valueOf((base*1000)+i)).build());
            }
        }
        
        Lecture lecture = Lecture.builder()
                            .hall("대강연장")
                            .lecturer("임꺽정")
                            .title("옛이야기")
                            .description("강연 설명 자세히")
                            .limit(1000)
                            .startat(Instant.now().plus(3,ChronoUnit.DAYS))
                            .endat(Instant.now().plus(2, ChronoUnit.HOURS))
                            .build();
        lectureService.save(lecture);
        lectureId = lecture.getId();
    }

    @Test
    @DisplayName("강연신청")
    public void testOne() {
        personService.save(Person.builder().account("90000").build());
        subscribeService.save(lectureId, "90000");
    }

    @Test
    @DisplayName("동시강연신청")
    public void testSave() throws Exception {
        for (int base=10; base<20; base++) {
            TestRunnable t = new TestRunnable(lectureId, base, subscribeService);
            Thread th = new Thread(t);
            th.start();
        }
        Thread.sleep(3000);
    }

    @Test
    @DisplayName("같은강연중복신청불가")
    public void testUniqueTest() {
        personService.save(Person.builder().account("80000").build());
        subscribeService.save(lectureId, "80000");
        Throwable exception = assertThrowsExactly(RuntimeException.class,() -> {
            subscribeService.save(lectureId, "80000");
        });
        assertEquals("중복강연신청불가", exception.getMessage());
    }

    @Test
    @DisplayName("내가신청한강연목록조회")
    public void testMySubscribe() throws Exception {
        String account = "12345";
        personService.save(Person.builder().account(account).build());
        // 강연신청
        subscribeService.save(lectureId, account);
        List<Lecture> list = subscribeService.findLectureByAccount(account);
        assertEquals(1, list.size());

        Lecture lecture = Lecture.builder()
                            .hall("소강연장")
                            .lecturer("강사1")
                            .title("주렁주렁")
                            .description("강연 설명 자세히")
                            .limit(20)
                            .startat(Instant.now().plus(5,ChronoUnit.DAYS))
                            .endat(Instant.now().plus(3, ChronoUnit.HOURS))
                            .build();
        lectureService.save(lecture);
        // 강연신청
        subscribeService.save(lecture.getId(), account);
        list = subscribeService.findLectureByAccount(account);
        assertEquals(2, list.size());

        for(Lecture s : list) {
            log.info(JsonUtil.objectToString(s));
        }
    }

    @Test
    @DisplayName("강연신청취소")
    public void testCancel() {
        String account = "34567";
        // 사번 생성
        personService.save(Person.builder().account(account).build());
        // 강연 신청
        subscribeService.save(lectureId, account);
        // 하나만 등록되어 있는 것을 확인
        assertEquals(1, subscribeService.findLectureByAccount(account).size());
        // 강연 신청 취소
        subscribeService.delete(lectureId, account);
        // 내 계정으로 신청한 강연이 없는 것을 확인
        assertEquals(0, subscribeService.findLectureByAccount(account).size());
    }

    @Test
    @DisplayName("강연인원초과 테스트")
    public void testLimit() {
        Lecture lecture = Lecture.builder()
                            .hall("대강연장9")
                            .lecturer("임꺽정9")
                            .title("옛이야기9")
                            .description("강연 설명 자세히9")
                            .limit(2)
                            .startat(Instant.now().plus(3,ChronoUnit.DAYS))
                            .endat(Instant.now().plus(2, ChronoUnit.HOURS))
                            .build();
        lectureService.save(lecture);
        Long id = lecture.getId();
        personService.save(Person.builder().account("87000").build());
        personService.save(Person.builder().account("87001").build());
        personService.save(Person.builder().account("87002").build());
        subscribeService.save(id, "87000");
        subscribeService.save(id, "87001");
        Throwable t = assertThrowsExactly(RuntimeException.class, () -> {
            subscribeService.save(id, "87002");
        });
        assertEquals(t.getMessage(), "강연인원초과로 신청불가");

    }

    @Test
    @DisplayName("강연신청한 사번목록 조회")
    public void testLecturePerson() throws Exception {
        String account = "34568";
        // 사번 생성
        personService.save(Person.builder().account(account).build());
        personService.save(Person.builder().account("34569").build());
        // 강연 신청
        subscribeService.save(lectureId, account);
        subscribeService.save(lectureId, "34569");

        List<Person> list = subscribeService.findPersonByLectureId(lectureId);
        log.info("size : {}", list.size());
        assertTrue(list.size() > 0);

        for(Person p : list) {
            log.info(JsonUtil.objectToString(p));
        }
    }

    @Test
    @DisplayName("최근3일간 인기강연")
    public void testFavoriteLecture() {
        Lecture lecture1 = Lecture.builder().hall("hall1").lecturer("강사").title("title1")
                            .description("강연 설명 자세히").limit(20)
                            .startat(Instant.now().plus(5,ChronoUnit.DAYS))
                            .endat(Instant.now().plus(3, ChronoUnit.HOURS))
                            .build();
        Lecture lecture2 = Lecture.builder().hall("hall2").lecturer("강사").title("title2")
                            .description("강연 설명 자세히").limit(20)
                            .startat(Instant.now().plus(4,ChronoUnit.DAYS))
                            .endat(Instant.now().plus(2, ChronoUnit.HOURS))
                            .build();

        Lecture lecture3 = Lecture.builder().hall("hall3").lecturer("강사").title("title3")
                            .description("강연 설명 자세히").limit(20)
                            .startat(Instant.now().plus(6,ChronoUnit.DAYS))
                            .endat(Instant.now().plus(2, ChronoUnit.HOURS))
                            .build();
                            
        lectureService.save(lecture1);
        lectureService.save(lecture2);
        lectureService.save(lecture3);
        // 순위 title2 > title1 > title 3
        subscribeService.save(lecture1.getId(), "10000");
        subscribeService.save(lecture1.getId(), "10001");
        subscribeService.save(lecture1.getId(), "10002");

        subscribeService.save(lecture2.getId(), "10003");
        subscribeService.save(lecture2.getId(), "10004");
        subscribeService.save(lecture2.getId(), "10005");
        subscribeService.save(lecture2.getId(), "10006");

        subscribeService.save(lecture3.getId(), "10007");
    
        List<Lecture> list = subscribeService.findFavoriteLecture();
        assertEquals(lecture2.getId(), list.get(0).getId());
    }
}

class TestRunnable implements Runnable {
    Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private SubscribeService subscribeService;
    private int index;
    private Long lectureId;
    public TestRunnable(Long id, int index, SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
        this.index = index;
        this.lectureId = id;
    }

    @Override
    public void run() {
        for (int i=0; i<20; i++) {
            log.info("idx: {}, inidx: {}", index, i);
            subscribeService.save(lectureId, String.valueOf((index*1000)+i));
        }
    }
}