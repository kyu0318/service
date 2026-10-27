# 대피할지도 | 재난 대피소 지도 서비스

지진·수해·민방공 대피소 정보를 지도에서 탐색하는 1차 팀 프로젝트. 지도 이동·장소 검색에 따라 현재 지도 영역의 대피소를 목록과 마커로 확인하는 사용자 서비스

## 프로젝트 전체 구성

| 구성 요소 | 기능·구성 |
| --- | --- |
| Service · 이 저장소 | 사용자 지도 화면, 대피소 조회 API, 기존 재난 유형 탭 API |
| Admin · 별도 저장소 | 회원·대피소·게시판 관리, Python 데이터 수집·적재 연계 |
| Docker Host · 인프라 문서 | 데이터 처리용 호스트, DB 연결, 작업 컨테이너·Cron 설정 |

사용자용 Service와 관리용 Admin으로 구성한 팀 프로젝트. 각 저장소에 애플리케이션별 구현 내용과 실행 조건 정리

[Service 코드](https://github.com/kyu0318/service) · [Admin 코드](https://github.com/kyu0318/admin-main) · [Admin 원본](https://github.com/ehdgus4026-tech/admin) · [Docker Host 구축 기록](https://road-bloom-792.notion.site/1-Docker-Host-3c910d3ee8d380e780f7c6db82d112a8)

- 두 애플리케이션의 현재 DB 연결 설정: 동일 대상 참조, 대피소 데이터 조회·관리 기능 분담
- 실제 배포 환경의 연결 상태: 별도 확인 필요
- Service 코드의 관리자용 기존 탭 조회·상태 변경 API: 구현 확인, 현재 Admin 원본의 호출 화면은 미확인

## Service에서 구현한 주요 기능

| 기능 | 구현 내용 | 코드 근거 |
| --- | --- | --- |
| 재난 유형별 조회 | 지진·수해·민방공 대피소의 전체·단건·지도 영역 조회 | 유형별 Controller, Service, Repository |
| 지도 영역 연계 | 지도 경계 좌표 4개를 API로 전달하고 목록·마커 갱신 | templates/map.html, static/index.html |
| 지도 이벤트 제어 | 이동·확대/축소·idle 이벤트에 150ms 디바운스 적용 | map.html의 debounceNotifyBounds() |
| 검색·현재 위치 | 주소·장소 검색, 브라우저 위치 권한을 이용한 지도 이동 | searchPlace(), getUserLocation() |
| 목록과 마커 연동 | 지도 중심 기준 직선거리 정렬, 10개 단위 목록, 마커 클릭 시 상세 표시 | filterByBounds(), renderCurrentPage(), selectShelterFromMap() |
| 재난 유형 탭 | DB에서 활성 탭 조회, 전체 탭 조회 및 활성 상태 변경 API | DisasterTabController, DisasterTabRepository |

- 거리 표시: 지도 중심 기준 직선거리, 보행 경로 거리·소요 시간 미제공
- 목록 페이지 나누기: 프런트엔드 처리
- 유효 좌표로 처리하지 못한 항목: 현재 화면용 데이터에서 제외

### 구현 범위와 고도화 계획

- 기존 재난 유형: 활성 탭 조회·상태 변경 API 구현
- **새 재난 유형 등록·연결:** 초기 계획에 포함했으나 시간 제약으로 1차 구현에서 제외하고 고도화 작업으로 이관했습니다.
- 신규 유형 지원: DB 항목 추가만으로 조회 API·엔티티가 자동 생성되지 않는 구조

## 동작 흐름

1. 사용자 화면에서 활성화된 재난 탭 조회
2. 지도 이벤트 발생 후 150ms 디바운스 적용, map.getBounds()로 남서·북동 좌표 확인
3. 선택한 재난 유형 API에 minLat, maxLat, minLng, maxLng 전달
4. Controller → Service → Repository 순서로 DB 좌표 범위 데이터 조회
5. 응답 데이터를 화면용 형식으로 변환, 지도 중심 기준 거리순 정렬
6. 지도 마커·대피소 목록 갱신, 마커 선택 시 상세 정보 표시

현재 Controller의 조회 조건: 좌표 4개가 모두 있는 경우 범위 조회, 일부 좌표 누락 시 전체 조회

## 사용 기술과 구조

| 구분 | 확인된 사용 범위 |
| --- | --- |
| Java·Spring Boot | POM 선언 Java 17 / Spring Boot 4.1.0, Spring MVC 기반 요청 처리 |
| Spring Data JPA·Hibernate·MySQL | 대피소 및 재난 탭 엔티티, 조회·상태 저장 |
| HTML·CSS·JavaScript·Thymeleaf | 사용자 화면, iframe 지도 연계, 지도 키 전달 |
| Kakao Maps JavaScript API | 지도·마커·주소·장소 검색 |
| Tailwind CSS·Font Awesome | 사용자 화면에서 CDN으로 참조 |
| Maven Wrapper·Lombok | 빌드 구성, 생성자·엔티티 보조 코드 |
| Python | 저장소의 데이터 확인·수집·적재 스크립트 |
| Ubuntu·Docker·Cron | 아래 인프라 문서의 데이터 처리 환경 |

Apache Commons CSV·Actuator: POM 의존성 선언 확인. 실제 CSV 처리·모니터링 운영 경험은 미확인

주요 파일:

- service-main/pom.xml
- service-main/src/main/java/com/example/demo/controller/
- service-main/src/main/java/com/example/demo/service/
- service-main/src/main/java/com/example/demo/repository/
- service-main/src/main/java/com/example/demo/domain/
- service-main/src/main/resources/static/index.html
- service-main/src/main/resources/templates/map.html
- service-main/src/main/resources/application.properties
- service-main/scripts/

## 데이터 처리 인프라

[Docker Host 구축 원문](https://road-bloom-792.notion.site/1-Docker-Host-3c910d3ee8d380e780f7c6db82d112a8)의 절차·설정 화면 기준

- Ubuntu 서버의 Docker 설치 및 Python 3.9-slim 이미지 준비
- Docker Host 측 네트워크 대역과 DB 접속 환경 설정
- Python 데이터 작업용 run_task.sh 구성: 프로젝트 바인드 마운트, 작업 디렉터리 지정, 패키지 설치 후 스크립트 실행
- check_db.py 및 excel_to_mysql.py 실행·점검 절차
- 매일 서버 시간 00:00 데이터 처리 작업을 실행하는 Cron 설정과 로그 출력 예시

아래 기록은 데이터 처리용 Docker Host 범위. VyOS 라우터·DB 서버·웹 Service 배포·Nginx 등 전체 인프라 구성과 구분

- run_task.sh·requirements.txt: Notion 화면에 존재, 현재 저장소 추적 파일에는 부재
- 웹 Service 배포용 Dockerfile·Compose·Nginx 설정: 현재 저장소에서 미확인

## 실행 조건과 실행 명령

현재 파일·설정을 기준으로 정리한 절차. 다른 환경의 빌드·실행 성공 여부는 미검증

1. POM의 Java 17 선언을 기준으로 JDK·Maven Wrapper 실행 환경 확인. Spring Boot 4.1.0 의존성 해결·조합의 실행 가능 여부는 별도 확인 필요
2. 테스트용 MySQL과 애플리케이션의 DB 접근 환경 준비. 주요 테이블: earthquake, flood, airstrike, disaster_tab. 데이터·활성 탭 등록은 별도 준비 필요
3. 로컬 실행 설정의 spring.datasource.url, spring.datasource.username, spring.datasource.password, kakao.maps.api-key 확인. 개인 접속값을 포함한 설정 파일의 공개 커밋 제외
4. Kakao Maps 키의 허용 도메인·CDN 접근 환경 확인. 현재 위치 기능의 브라우저 권한·실행 환경 확인
5. 저장소 루트에서 service-main으로 이동 후 아래 명령 실행

Windows 명령 프롬프트:

```bat
cd service-main
mvnw.cmd spring-boot:run
```

Linux·macOS의 Bash 환경:

```bash
cd service-main
bash mvnw spring-boot:run
```

기본 확인 경로: http://localhost:8080/ 및 http://localhost:8080/map

- mvnw: 실행 권한 없이 추적 중인 파일로, Bash 호출 형태 제시
- Java 설정: DB_URL·DB_USERNAME·DB_PASSWORD·KAKAO_MAPS_API_KEY 이름의 사용자 정의 환경변수 치환 부재
- config/.env: Java 설정 자동 로딩 근거 미확인
- Python 적재 작업: 별도 환경 준비 필요. 로컬 경로·입력 엑셀·의존성·DB 연결 방식의 서버 환경 정합성 확인 필요
- 적재 스크립트: 기존 데이터 교체·삭제 동작 포함, 일반 웹 실행 절차와 분리

## 기록으로 확인한 구현·검증과 한계

- 좌표 처리: 화면의 위도·경도 순서 보정, 민방공 범위 쿼리의 역순 좌표 조회 조건 확인. 문제 발생·해결 전후 로그를 갖춘 장애 사례는 미확인
- AirRaidShelterControllerTest: MockMvc 목록 API의 HTTP 200 응답 테스트. 서비스 모의 객체 사용, 실제 DB 조회·지도 동작 검증 제외
- DemoApplicationTests: contextLoads() 존재. 지진·수해 Controller 테스트 파일은 전체 주석 처리 상태
- Notion: ping·DB 확인·데이터 적재·crontab 등록 확인 절차 존재. 성공 출력·Cron 실행 로그 전체는 추가 확인 필요

이번 문서 검토에서는 애플리케이션 실행·배포·테스트를 수행하지 않았습니다.

## 남은 개선 과제

- 새 재난 유형 등록·연결 기능 고도화
- 접속정보의 안전한 외부 설정 관리
- 상태 변경 API의 인증·인가, CORS 및 입력값 검증 점검
- 서버 실행 환경과 데이터 처리 스크립트 경로·의존성 정합성 확인
- 실제 실행 화면·ERD와 검증 로그 정리

위 항목은 향후 작업. 현재 구현 완료한 보안 대책·운영 성과와 구분
