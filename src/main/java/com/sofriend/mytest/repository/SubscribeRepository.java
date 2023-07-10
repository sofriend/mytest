package com.sofriend.mytest.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sofriend.mytest.vo.Lecture;
import com.sofriend.mytest.vo.Person;
import com.sofriend.mytest.vo.Subscribe;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    public Optional<Subscribe> findByLectureIdAndPersonAccount(Long lectureId, String account);
    
    @Query(value="SELECT s.lecture FROM Subscribe s WHERE s.person.account = :account")
    public List<Lecture> findLectureByAccount(@Param("account") String account);
    
    @Query(value="SELECT s.person FROM Subscribe s WHERE s.lecture.id = :id")
    public List<Person> findAllPersonByLectureId(@Param("id") Long id);

    @Query(value="SELECT s.lecture FROM Subscribe s WHERE s.subscribeAt > :time GROUP BY s.lecture.id ORDER BY count(s.person) DESC")
    public List<Lecture> findFavoriteLecture(@Param("time") Instant time);

}
