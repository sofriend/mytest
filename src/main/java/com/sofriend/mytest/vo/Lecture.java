package com.sofriend.mytest.vo;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="lecture")
public class Lecture {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="lecture_id")
    private Long id;

    // 강연명
    @Column(name="title", nullable=false)
    private String title;

    // 강연자
    @Column(name="lecturer", nullable=false)
    private String lecturer;
    
    // 강연장
    @Column(name="hall", nullable=false)
    private String hall;
    
    // 신청인원
    @Column(name="person_limit", nullable=false)
    private Integer limit;
    
    /** 강연시작시간 */
    @Column(name="startat", nullable=false)
    private Instant startat;
    
    /** 강연종료시간 */
    @Column(name="endat", nullable=false)
    private Instant endat;
    
    /** 강연내용 */
    @Column(name="description", nullable=false)
    private String description;

}
