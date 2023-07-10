package com.sofriend.mytest.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sofriend.mytest.repository.LectureRepository;
import com.sofriend.mytest.repository.LectureSpecification;
import com.sofriend.mytest.vo.Lecture;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LectureService {
    private final LectureRepository lectureRepository;

    /**
     * 새 강연 생성
     * @param lecture
     * @return
     */
    public Lecture save(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    public Lecture findById(Long id) {
        return lectureRepository.findById(id).get();
    }

    public void delete(Long id) {
        lectureRepository.deleteById(id);
    }

    /**
     * 현재기준 1일전 부터 7일 후 까지의 startat 목록을 반환한다.
     * @return
     */
    public List<Lecture> list() {
        Instant start = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant end = Instant.now().plus(7, ChronoUnit.DAYS);
        Specification<Lecture> spec = Specification.where(LectureSpecification.betweenStartAt(start, end));
        return lectureRepository.findAll(spec);
    }
}
