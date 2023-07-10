# mytest

테스트과제

- 개발언어
  - Java 11
- 프레임워크
  - Spring Boot 2.x
- RDBMS
  - H2 memory
- 데이터설계
  - Lecture
    - 강연정보를 담는 객체. (강연장,강연자,강연명,인원수,시작일시,종료일시)
  - Person
    - 강연을 신청한 사람에 대한 객체. (사번)
  - Subscribe
    - Lecture와 Person 에 대한 FK로 이루어진 객체
    - lecture_id와 person_id 가 unique key로 등록 되어 있어 1강연당 1사번만 신청 가능하도록 제약설정
- 코멘트
  - JPA를 사용해 만들었습니다.
  - 권한 관리 측면에서 관리기능은 /manage prefix로 URL을 구분했습니다.
  - 별도 세션 처리는 하지 않았습니다.
  - 전체 API에 대한 service, controller 테스트 코드를 작성했습니다.
  - 사번은 Person 객체를 만들어 관리했습니다. 그렇기 때문에 사번 등록 후에 사용할 수 있습니다. (/person/add)
  - H2 는 서버 구동시 초기화 되도록 구성되어 있습니다.

제약조건

- [v] 강연신청 목록
  - [v] 강연시작시간 1주일 전에 노출
  - [v] 강연시작시간 1일 후 노출하지 않음
- [v] 강연신청
  - [v] 입장가능한 인원수는 강연마다 다름.
  - [v] 5자리 사번만 입력
  - [v] 아래 조건은 DB제약조건상 한 번에 처리됨.
    - [v] 1인 1좌석만 예약가능
    - [v] 같은 강연 중복신청 불가
    - [v] 타 강연 신청 가능
- [v] 개인화
  - [v] 사번 입력시 신청한 강연목록 조회
  - [v] 조회한 신청강연을 취소할 수 있음
- [v] 관리페이지(백오피스)
  - [v] 강연 신청 사번 목록 조회
  - [v] 실시간 인기 강연 메뉴
    - [v] 3일간 가장 신청이 많은 강연순으로 노출

고려사항

- [v] 동시성
  - subscribe.save 시에 가장 오래기다린 요청이 먼저 처리되도록 함.
- [v] 단위테스트
  - 서비스 단위 테스트

API

- 사번등록
  - PUT /person/add
    - param account
    - return Person
- 강연목록
  - GET /lecture/list
    - @return List<Lecture>
    - 강연시작 1주일 전 부터 강연시작 1일 후 사이의 강연목록
- 최근 3일 인기 수강 목록
  - GET /lecture/list/favorite
- 내가 신청한 강연 목록
  - GET /lecture/list/signup
    - param account
    - return List<Lecture>
- 강연 수강 신청
  - POST /lecture/{lectureId}/signup
    - @param account
    - @return Subscribe(Lecture, Person)
    - 중복 수강시 Exception 발생
- 강연 수강 취소
  - POST /lecture/{lectureId}/cancel
    - @param account
- [관리] 강연등록
  - PUT /manage/lecture/add
    - @param JSON Lecture
    - @return Lecture
- [관리] 강연삭제
  - DELETE /manage/lecture/{lectureId}/delete
- [관리] 강연신청 사번 목록
  - GET /manage/lecture/{lectureId}/person
    - return List<Person>
