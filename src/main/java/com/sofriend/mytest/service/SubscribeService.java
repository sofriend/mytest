package com.sofriend.mytest.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sofriend.mytest.repository.PersonRepository;
import com.sofriend.mytest.repository.SubscribeRepository;
import com.sofriend.mytest.vo.Lecture;
import com.sofriend.mytest.vo.Person;
import com.sofriend.mytest.vo.Subscribe;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final PersonRepository personRepository;
    private static final Lock lock = new ReentrantLock(true); // 요청 순서 보장

    /**
     * 동시성 고려. 요청 순서보장.
     */
    public Subscribe save(Subscribe data) {
        lock.lock();
        try {
            subscribeRepository.save(data);
        } catch(DataIntegrityViolationException cve) {
            throw new RuntimeException("중복강연신청불가", cve);
        } finally {
            lock.unlock();
        }
        return data;
    }

    public Subscribe save(Long lectureId, String account) {
        Optional<Person> p = personRepository.findByAccount(account);
        if (p.isEmpty()) {
            throw new RuntimeException("account was not exists");
        }
        Subscribe s = Subscribe.builder()
                        .lecture(Lecture.builder().id(lectureId).build())
                        .person(Person.builder().id(p.get().getId()).build())
                        .subscribeAt(Instant.now())
                        .build();
        return save(s);
    }

    /**
     * 강연 신청 취소
     * @param lectureId
     * @param account
     */
    public void delete(Long lectureId, String account) {
        Optional<Subscribe> s = subscribeRepository.findByLectureIdAndPersonAccount(lectureId, account);
        if (s.isEmpty()) {
            throw new RuntimeException("Subscribe was not exists");
        }
        subscribeRepository.delete(s.get());
    }

    /**
     * 사번 입력시 신청한 강연목록 조회
     * @param account
     * @return
     */
    public List<Lecture> findLectureByAccount(String account) {
        return subscribeRepository.findLectureByAccount(account);
    }

    /**
     * 강연을 신청한 사번목록 조회
     * @param lectureId
     * @return
     */
    public List<Person> findPersonByLectureId(Long lectureId) {
        return subscribeRepository.findAllPersonByLectureId(lectureId);
    }

    /**
     * 최근 3일간 인기 수강 강연 목록 조회
     * @param time time 기준 
     * @return
     */
    public List<Lecture> findFavoriteLecture() {
        Instant time = Instant.now().minus(3, ChronoUnit.DAYS);
        return subscribeRepository.findFavoriteLecture(time);
    }
}
