package com.sofriend.mytest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sofriend.mytest.service.LectureService;
import com.sofriend.mytest.service.SubscribeService;
import com.sofriend.mytest.vo.Lecture;
import com.sofriend.mytest.vo.Person;
import com.sofriend.mytest.vo.Subscribe;

import lombok.NoArgsConstructor;

@RestController
@NoArgsConstructor
public class LectureController {
    private LectureService lectureService;
    private SubscribeService subscribeService;
    
    /**
     * 강연목록
     * 현재 시각 기준 시작일이 1주일 이내 및 1일 이후 인 목록만 추출
     * @return
     */
    @GetMapping("/lecture/list")
    public ResponseEntity<List<Lecture>> lectureList() {
        return new ResponseEntity<>(lectureService.list(), HttpStatus.OK);
    }

    /** 강연신청 */
    @PostMapping("/lecture/{lectureId}/signup")
    public ResponseEntity<Subscribe> lectureSignup(@PathVariable Long lectureId, String account) {
        return new ResponseEntity<>(subscribeService.save(lectureId, account), HttpStatus.OK);
    }

    /** 강연신청 취소 */
    @PostMapping("/lecture/{lectureId}/cancel")
    public void lectureCancel(@PathVariable Long lectureId, String account) {
        subscribeService.delete(lectureId, account);
    }

    /** 신규 강연 등록 */
    @PutMapping("/manage/lecture/add")
    public ResponseEntity<Lecture> lectureAdd(Lecture lecture) {
        return new ResponseEntity<>(lectureService.save(lecture), HttpStatus.OK);
    }
    
    /** 강연 삭제 */
    @DeleteMapping("/manage/lecture/{lectureId}/delete")
    public void lectureDelete(@PathVariable Long lectureId) {
        lectureService.delete(lectureId);
    }

    /** 강연 신청 사번 목록 조회 */
    @GetMapping("/manage/lecture/{lectureId}/person")
    public ResponseEntity<List<Person>> lecturePersonList(@PathVariable Long lectureId) {
        return new ResponseEntity<>(subscribeService.findPersonByLectureId(lectureId), HttpStatus.OK);
    }

    /** 최근 3일간 인기 강연 목록 */
    @GetMapping("/lecture/list/favorite")
    public ResponseEntity<List<Lecture>> lectureFavorite() {
        return new ResponseEntity<>(subscribeService.findFavoriteLecture(), HttpStatus.OK);
    }

    public ResponseEntity<List<Lecture>> lectureMylist(String account) {
        return new ResponseEntity<>(subscribeService.findLectureByAccount(account), HttpStatus.OK);
    }
}
