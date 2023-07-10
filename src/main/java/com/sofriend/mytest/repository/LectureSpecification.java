package com.sofriend.mytest.repository;

import java.time.Instant;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sofriend.mytest.vo.Lecture;

public class LectureSpecification {
    public static Specification<Lecture> betweenStartAt(Instant start, Instant end) {
        return new Specification<Lecture>() {
            @Override
            public Predicate toPredicate(Root<Lecture> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get("startat"), start, end);
            }
        };
    }
}
