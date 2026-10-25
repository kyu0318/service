# 대피할지도 | 재난 대피소 지도 서비스

지진·수해·민방공 대피소 정보를 지도에서 탐색하는 1차 팀 프로젝트입니다. 사용자가 지도를 이동하거나 장소를 검색하면 현재 지도 영역에 맞는 대피소를 목록과 마커로 확인할 수 있습니다.

## 프로젝트 구성과 담당 역할

| 구성 요소 | 역할 | 개발·담당 범위 |
| --- | --- | --- |
| Service · 이 저장소 | 사용자 지도 화면, 대피소 조회 API, 재난 유형 탭 API | **김규현: Service 개발 전담** |
| Admin · 별도 저장소 | 대피소 데이터 관리, 회원·게시글 관리 화면 | **다른 팀원: Admin 개발 담당** |
| Docker Host · 인프라 문서 | 데이터 처리 작업을 실행하는 서버 환경 | **김규현: Docker Host 구성과 해당 호스트의 DB 연결 및 컨테이너 실행·Cron 설정 기록** |

Service와 Admin은 같은 팀 프로젝트의 구성 요소입니다. Service 개발 전담은 팀 프로젝트 전체를 혼자 개발했다는 의미가 아닙니다.

[Service 저장소](https://github.com/kyu0318/service) · [Admin 원본 — 다른 팀원 담당](https://github.com/ehdgus4026-tech/admin) · [인프라 구축 기록](https://app.notion.com/p/1-Docker-Host-3c910d3ee8d380e780f7c6db82d112a8)

두 애플리케이션의 현재 DB 연결 설정은 같은 대상을 가리키며, 대피소 데이터를 조회·관리하는 기능이 나뉘어 있습니다. 실제 배포 환경의 연결 상태는 별도 확인이 필요합니다. Service 코드에는 관리자 용도로 표시된 기존 탭 조회·상태 변경 API가 있으나, 현재 Admin 원본에서 이를 호출하는 화면은 확인되지 않았습니다.

## Service에서 구현한 주요 기능

| 기능 | 구현 내용 | 코드 근거 |
| --- | --- | --- |
| 재난 유형별 조회 | 지진·수해·민방공 대피소의 전체·단건·지도 영역 조회 | 유형별 Controller, Service, Repository |
| 지도 영역 연계 | 지도 경계 좌표 4개를 API로 전달하고 목록·마커 갱신 | templates/map.html, static/index.html |
| 지도 이벤트 제어 | 이동·확대/축소·idle 이벤트에 150ms 디바운스 적용 | map.html의 debounceNotifyBounds() |
| 검색·현재 위치 | 주소·장소 검색, 브라우저 위치 권한을 이용한 지도 이동 | searchPlace(), getUserLocation() |
| 목록과 마커 연동 | 지도 중심 기준 직선거리 정렬, 10개 단위 목록, 마커 클릭 시 상세 표시 | filterByBounds(), renderCurrentPage(), selectShelterFromMap() |
| 재난 유형 탭 | DB에서 활성 탭 조회, 전체 탭 조회 및 활성 상태 변경 API | DisasterTabController, DisasterTabRepository |

거리 표시는 지도 중심을 기준으로 계산한 직선거리이며 보행 경로의 거리나 소요 시간이 아닙니다. 목록 페이지 나누기는 프런트엔드에서 수행합니다. 유효한 좌표로 처리하지 못한 항목은 현재 화면용 데이터에서 제외합니다.

### 구현 범위와 고도화 계획

기존 재난 유형의 활성 탭 조회와 상태 변경 API는 코드에 포함되어 있습니다. **새 재난 유형을 등록하고 연결하는 기능은 초기 계획에 있었으나, 시간 제약으로 1차 구현에서 제외하고 고도화 작업으로 이관했습니다.** DB에 항목을 등록하는 것만으로 새로운 조회 API·엔티티까지 자동 생성되는 구조는 아닙니다.

## 동작 흐름

1. 사용자 화면에서 활성화된 재난 탭을 조회합니다.
2. 지도 이벤트가 발생하면 150ms 디바운스 후 map.getBounds()로 남서·북동 좌표를 읽습니다.
3. 선택한 재난 유형 API에 minLat, maxLat, minLng, maxLng를 보냅니다.
4. Controller → Service → Repository 순서로 DB의 좌표 범위 데이터를 조회합니다.
5. 응답 데이터를 화면용 형식으로 맞추고 지도 중심 기준 거리순으로 정렬합니다.
6. 지도 마커와 대피소 목록을 갱신합니다. 마커를 누르면 선택한 대피소 정보를 표시합니다.

네 가지 좌표가 모두 들어올 때 범위 조회를 하며, 일부라도 없으면 현재 Controller는 전체 조회로 처리합니다.

## 사용 기술과 구조

| 구분 | 확인된 사용 범위 |
| --- | --- |
| Java·Spring Boot | POM 선언 Java 17 / Spring Boot 4.1.0, Spring MVC 기반 요청 처리 |
| Spring Data JPA·Hibernate·MySQL | 대피소 및 재난 탭 엔티티, 조회·상태 저장 |
| HTML·CSS·JavaScript·Thymeleaf | 사용자 화면, iframe 지도 연계, 지도 키 전달 |
| Kakao Maps JavaScript API | 지도·마커·주소·장소 검색 |
| Tailwind CSS·Font Awesome | 사용자 화면에서 CDN으로 참조 |
| Maven Wrapper·Lombok | 빌드 구성, 생성자·엔티티 보조 코드 |
| Python | 저장소에 데이터 확인·수집·적재 스크립트 포함 |
| Ubuntu·Docker·Cron | 아래 인프라 문서에 기록된 데이터 처리 환경 |

Apache Commons CSV와 Actuator는 POM 의존성에 선언되어 있습니다. 선언만으로 CSV 처리 기능이나 모니터링 운영 경험을 구현했다고 설명하지 않습니다.

실제 주요 파일:

- service-main/pom.xml
- service-main/src/main/java/com/example/demo/controller/
- service-main/src/main/java/com/example/demo/service/
- service-main/src/main/java/com/example/demo/repository/
- service-main/src/main/java/com/example/demo/domain/
- service-main/src/main/resources/static/index.html
- service-main/src/main/resources/templates/map.html
- service-main/src/main/resources/application.properties
- service-main/scripts/

## 김규현 담당 인프라 기록

[Docker Host 구축 원문](https://app.notion.com/p/1-Docker-Host-3c910d3ee8d380e780f7c6db82d112a8)에 다음 절차와 설정 화면이 있습니다.

- Ubuntu 서버에 Docker 설치 및 Python 3.9-slim 이미지 준비
- Docker Host 측 네트워크 대역과 DB 접속 환경 설정
- Python 데이터 작업용 run_task.sh 구성: 프로젝트 바인드 마운트, 작업 디렉터리 지정, 패키지 설치 후 스크립트 실행
- check_db.py 및 excel_to_mysql.py 실행·점검 절차
- 매일 서버 시간 00:00 데이터 처리 작업을 실행하는 Cron 설정과 로그 출력 예시

김규현의 인프라 담당 범위는 Docker Host입니다. VyOS 라우터 전체 설정·DB 서버 구축·웹 Service 컨테이너 배포·Nginx 구성은 개인 담당 범위로 포함하지 않습니다.

위 내용은 데이터 처리용 Docker Host 기록입니다. 웹 Service 배포용 Docker 구성 전체를 이 저장소가 제공한다는 뜻은 아닙니다. run_task.sh·requirements.txt는 Notion 화면에 있으나 현재 저장소의 추적 파일에는 없습니다. Dockerfile·Compose·Nginx 설정도 현재 확인되지 않았습니다.

## 실행 조건과 실행 명령

이 절차는 현재 파일과 설정을 읽어 정리했습니다. 다른 환경에서의 빌드·실행 성공은 아직 검증하지 않았습니다.

1. POM의 Java 17 선언을 기준으로 JDK와 Maven Wrapper의 실행 환경을 확인합니다. Spring Boot 4.1.0 의존성 해결 및 조합의 실행 가능 여부는 별도 확인이 필요합니다.
2. 테스트용 MySQL과 애플리케이션이 접근 가능한 DB 연결 정보를 준비합니다. 엔티티의 주요 테이블은 earthquake, flood, airstrike, disaster_tab입니다. 데이터와 활성 탭 등록은 별도 준비가 필요합니다.
3. 로컬 실행 설정에서 spring.datasource.url, spring.datasource.username, spring.datasource.password, kakao.maps.api-key 값을 확인합니다. 개인 접속값을 수정한 파일은 공개 저장소에 다시 올리지 않습니다.
4. Kakao Maps 키의 허용 도메인과 CDN 접근 환경을 확인합니다. 현재 위치 기능은 브라우저 권한과 실행 환경의 영향을 받습니다.
5. 저장소 루트에서 service-main 디렉터리로 이동한 뒤 아래 명령을 사용합니다.

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

현재 mvnw는 실행 권한 없이 추적되어 있어 Bash 호출 형태를 제시했습니다. Java 설정에는 DB_URL·DB_USERNAME·DB_PASSWORD·KAKAO_MAPS_API_KEY라는 이름의 사용자 정의 환경변수 치환이 없습니다. config/.env 파일도 Java 설정의 자동 로딩 근거로 사용할 수 없습니다.

Python 적재 작업은 별도 환경 준비가 필요합니다. 일부 스크립트의 로컬 경로, 입력 엑셀 파일, 의존성, DB 연결 방식이 실행 서버와 맞는지 확인해야 합니다. 적재 스크립트에는 기존 데이터를 교체·삭제하는 동작이 있어 일반 웹 실행 절차와 분리합니다.

## 기록으로 확인한 구현·검증과 한계

- 지도 데이터의 위도·경도 순서를 화면에서 보정하며, 민방공 범위 쿼리에는 좌표가 반대로 들어간 경우도 조회하는 조건이 있습니다. 이는 현재 코드의 처리 방식이며 문제 발생·해결 전후 로그까지 확인한 장애 사례는 아닙니다.
- AirRaidShelterControllerTest에는 MockMvc로 목록 API의 HTTP 200 응답을 확인하는 테스트가 있습니다. 서비스는 모의 객체이며 실제 DB 조회나 지도 동작을 검증하지 않습니다.
- DemoApplicationTests에는 contextLoads()가 있습니다. 지진·수해 Controller 테스트 파일은 현재 전체가 주석 처리되어 있습니다.
- Notion에는 ping, DB 확인, 데이터 적재, crontab 등록 확인 절차가 있습니다. 성공 출력·Cron 실행 로그 전체는 확인 필요입니다.
- 이번 문서 검토에서는 애플리케이션 실행·배포·테스트를 수행하지 않았습니다. 성능 향상 수치나 실무 운영 성과를 주장하지 않습니다.

## 남은 개선 과제

- 새 재난 유형 등록·연결 기능 고도화
- 접속정보의 안전한 외부 설정 관리
- 상태 변경 API의 인증·인가, CORS 및 입력값 검증 점검
- 서버 실행 환경과 데이터 처리 스크립트 경로·의존성 정합성 확인
- 실제 실행 화면·ERD와 검증 로그 정리

위 항목은 향후 작업이며, 현재 구현 완료한 보안 대책이나 운영 성과가 아닙니다.
