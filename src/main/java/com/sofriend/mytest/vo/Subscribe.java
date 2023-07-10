package com.sofriend.mytest.vo;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 강의를 수강한 수강자 정보 관계 테이블
 */
@Table(name = "subscribe", uniqueConstraints = @UniqueConstraint(columnNames = { "lecture_id", "person_id"}))
@Entity
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    
    @Column(name = "subscribeat")
    private Instant subscribeAt;
}
