# 🗺️ 대피할지도 | 재난 대피소 지도 서비스

> 재난 유형별 대피소 정보를 지도에서 확인하고, 현재 위치와 지도 영역을 기준으로 필요한 정보를 탐색할 수 있는 웹 서비스입니다.

<!-- 실제 화면 캡처를 준비한 뒤 아래 이미지 경로를 저장소에 맞게 수정하세요. -->
<!-- 예: docs/images/main.png -->
<!-- ![서비스 메인 화면](docs/images/main.png) -->

## 📌 프로젝트 소개

**대피할지도**는 지진 대피소, 수해 관련 시설, 민방공 대피소 정보를 한 곳에서 확인할 수 있도록 만든 지도 기반 웹 프로젝트입니다. 카카오 지도 API와 Spring Boot 기반 조회 API를 연동해 사용자가 지도를 이동하거나 검색할 때 위치에 맞는 대피소 정보를 탐색할 수 있도록 구현했습니다.

- **프로젝트 유형:** 웹 애플리케이션
- **핵심 주제:** 지도 기반 위치 정보 탐색 및 재난 대피소 조회
- **주요 기술:** Java, Spring Boot, Spring Data JPA, MySQL, JavaScript, Kakao Maps API

## ✨ 주요 기능

### 1. 재난 유형별 대피소 조회

- 지진 대피소, 수해 관련 시설, 민방공 대피소를 유형별 API로 분리해 조회합니다.
- 재난 유형별 데이터 모델과 조회 로직을 각각 구성했습니다.
- 대피소의 좌표 정보를 이용해 지도 마커와 목록 정보를 연계합니다.

### 2. 지도 영역 기반 데이터 조회

- 지도 화면의 남서쪽·북동쪽 좌표를 이용해 조회 범위를 계산합니다.
- `minLat`, `maxLat`, `minLng`, `maxLng` 파라미터를 API로 전달해 현재 지도 영역에 해당하는 데이터를 조회합니다.
- 좌표 파라미터가 없으면 전체 목록을 조회할 수 있도록 구성했습니다.
- 지도 이동·확대/축소가 연속해서 발생할 때 디바운스(debounce)를 적용해 짧은 시간 동안 반복되는 요청을 줄입니다.

### 3. 장소 및 주소 검색

- 카카오 지도 검색 서비스를 활용해 장소와 주소를 검색합니다.
- 검색한 위치로 지도를 이동하고 별도의 마커로 검색 위치를 구분합니다.

### 4. 현재 위치 확인

- 브라우저 위치 기능을 활용해 사용자의 현재 위치를 지도에 표시하도록 구성했습니다.
- 현재 위치와 대피소 위치 정보를 함께 확인할 수 있습니다. 위치 기능은 브라우저 권한과 실행 환경의 영향을 받을 수 있습니다.

### 5. 대피소 목록 및 페이지 이동

- 대피소 정보를 목록 형태로 확인할 수 있습니다.
- 페이지네이션으로 목록을 나누어 탐색할 수 있도록 구성했습니다.
- 좌표가 등록되지 않은 데이터는 위치 정보가 없음을 구분해 표시합니다.

### 6. 재난 유형 탭 관리 API

- 사용자 화면에서 활성화된 재난 유형 탭을 조회합니다.
- 전체 탭 목록을 조회하고 각 탭의 활성화 상태를 변경하는 API를 제공합니다.

## 🧰 기술 스택

| 구분 | 사용 기술 |
|---|---|
| Backend | Java 17, Spring Boot |
| API | Spring MVC, REST API |
| ORM / Persistence | Spring Data JPA, Hibernate |
| Database | MySQL |
| Frontend | HTML, CSS, JavaScript, Thymeleaf |
| Map | Kakao Maps JavaScript API |
| Build | Maven Wrapper |
| Libraries | Lombok, Apache Commons CSV |
| Test | Spring Boot Test, JUnit 기반 테스트 구성 |

## 🏗️ 시스템 구조

```text
사용자 브라우저
  ├─ HTML / CSS / JavaScript
  ├─ 카카오 지도 및 위치·검색 기능
  └─ REST API 요청
          │
          ▼
      Controller
  HTTP 요청 및 응답 처리
          │
          ▼
       Service
  조회 및 범위 검색 로직
          │
          ▼
      Repository
  Spring Data JPA 데이터 접근
          │
          ▼
        MySQL
  재난 유형별 대피소 데이터
```

### 백엔드 계층별 역할

| 계층 | 역할 |
|---|---|
| `controller` | 화면 경로와 REST API 요청을 처리하고 응답을 반환합니다. |
| `service` | 대피소 전체 조회, 단건 조회, 지도 영역 조회 로직을 담당합니다. |
| `repository` | Spring Data JPA를 통해 데이터베이스를 조회합니다. |
| `domain` | 지진 대피소, 수해 시설, 민방공 대피소, 재난 탭을 엔티티로 표현합니다. |

## 🔍 주요 구현 포인트

### 지도 경계 좌표를 이용한 조회

지도 전체의 데이터를 매번 화면에 표시하는 대신, 현재 보이는 지도 영역의 경계 좌표를 조회 API에 전달하도록 구현했습니다.

```http
GET /api/shelters/earthquake?minLat=37.4&maxLat=37.5&minLng=126.9&maxLng=127.0
```

서버는 네 가지 좌표가 모두 전달되면 해당 범위의 데이터를 조회합니다. 좌표 파라미터가 전달되지 않은 경우에는 전체 목록을 반환합니다.

**기술적 의의**

- 지도에서 보이는 영역에 맞춰 데이터를 조회할 수 있습니다.
- 지도 이벤트가 연속해서 발생할 때 디바운스로 반복 요청을 줄입니다.
- 대량 데이터 환경에서는 지도 영역 기반 조회가 전체 데이터의 불필요한 전달을 줄이는 데 도움이 됩니다.

> 참고: 현재 구현은 지도 영역 기반 조회와 프런트엔드 목록 페이지네이션을 사용합니다. 서버 페이지네이션이나 공간 인덱스까지 적용했다고 표현하지는 않습니다.

### 재난 유형별 API 분리

재난 유형별 Controller, Service, Repository, Domain을 분리해 각 데이터의 조회 책임을 구분했습니다. 이를 통해 유형별 조회 로직을 관리하기 쉽고, 기능별 수정 범위를 명확하게 할 수 있도록 구성했습니다.

### 데이터 적재 및 확인 스크립트

`scripts/` 디렉터리에 엑셀 데이터 확인 및 MySQL 적재와 관련된 Python 스크립트를 두었습니다.

- `excel_to_mysql.py`: 엑셀 데이터의 MySQL 적재 작업
- `check_db.py`: 데이터베이스 연결 및 상태 확인
- `check_excel.py`: 엑셀 데이터 확인
- `earthquake.py`, `airstrike.py`: 재난 유형별 데이터 처리 관련 스크립트

실행 전에는 스크립트의 DB 접속 정보와 입력 파일 경로를 실행 환경에 맞게 설정해야 합니다.

## 🔌 API 명세

기본 로컬 주소는 `http://localhost:8080`입니다.

| Method | Endpoint | 설명 |
|---|---|---|
| `GET` | `/api/shelters/earthquake` | 지진 대피소 전체 조회 |
| `GET` | `/api/shelters/earthquake?minLat={min}&maxLat={max}&minLng={min}&maxLng={max}` | 지도 영역 내 지진 대피소 조회 |
| `GET` | `/api/shelters/earthquake/{id}` | 지진 대피소 단건 조회 |
| `GET` | `/api/shelters/flood` | 수해 시설 전체 조회 |
| `GET` | `/api/shelters/flood?minLat={min}&maxLat={max}&minLng={min}&maxLng={max}` | 지도 영역 내 수해 시설 조회 |
| `GET` | `/api/shelters/flood/{id}` | 수해 시설 단건 조회 |
| `GET` | `/api/shelters/air` | 민방공 대피소 전체 조회 |
| `GET` | `/api/shelters/air?minLat={min}&maxLat={max}&minLng={min}&maxLng={max}` | 지도 영역 내 민방공 대피소 조회 |
| `GET` | `/api/shelters/air/{id}` | 민방공 대피소 단건 조회 |
| `GET` | `/api/disaster-tabs/active` | 활성화된 재난 유형 탭 조회 |
| `GET` | `/api/disaster-tabs/all` | 전체 재난 유형 탭 조회 |
| `PATCH` | `/api/disaster-tabs/{id}/status?active={true\|false}` | 재난 유형 탭 활성 상태 변경 |

## 📂 프로젝트 구조

```text
service-main/
├── scripts/
│   ├── airstrike.py
│   ├── earthquake.py
│   ├── check_db.py
│   ├── check_excel.py
│   └── excel_to_mysql.py
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/
│   │   │   ├── domain/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   ├── style.css
│   │       │   └── js/kakao.js
│   │       ├── templates/map.html
│   │       └── application.properties
│   └── test/
├── pom.xml
└── mvnw / mvnw.cmd
```

## 🚀 실행 방법

### 1. 사전 준비

- JDK 17 이상
- MySQL
- Kakao Developers에서 발급한 카카오 지도 JavaScript 키

### 2. 데이터베이스 설정

MySQL에 프로젝트용 데이터베이스를 준비하고, DB 주소·사용자명·비밀번호를 로컬 환경에 맞게 설정합니다. 데이터 적재 스크립트를 사용하는 경우 필요한 엑셀 파일과 Python 패키지도 준비합니다.

### 3. 환경 변수 설정

DB 비밀번호와 카카오 지도 키는 코드나 Git 저장소에 직접 작성하지 말고 환경 변수 또는 저장소 외부 설정으로 관리하세요. `application.properties`가 해당 환경 변수를 읽도록 설정한 뒤 실행해야 합니다.

환경 변수 이름 예시:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
KAKAO_MAPS_API_KEY
```

> 프로젝트를 처음 실행하기 전에 `src/main/resources/application.properties`와 `config/.env`, Python 스크립트에 민감한 값이 남아 있지 않은지 확인하세요. 현재 제공된 프로젝트 파일에는 DB 접속 정보와 카카오 지도 키가 직접 포함되어 있으므로, 공개 저장소에 이미 올렸다면 키와 비밀번호를 교체하고 Git 이력에서도 제거하는 것을 권장합니다.

### 4. 애플리케이션 실행

프로젝트의 `service-main` 디렉터리에서 실행합니다.

**Windows**

```bash
mvnw.cmd spring-boot:run
```

**macOS / Linux**

```bash
./mvnw spring-boot:run
```

서버 실행 후 브라우저에서 확인합니다.

- 메인 화면: `http://localhost:8080/`
- 지도 화면: `http://localhost:8080/map`

카카오 지도 기능은 API 키와 허용 도메인 설정, 브라우저 네트워크 환경에 따라 동작이 달라질 수 있습니다.

## 🧪 테스트

프로젝트에 포함된 테스트를 실행하려면 다음 명령어를 사용합니다.

**Windows**

```bash
mvnw.cmd test
```

**macOS / Linux**

```bash
./mvnw test
```

## 📸 화면 및 설계 자료

포트폴리오의 전달력을 높이려면 실제 화면 캡처와 설계 자료를 추가하는 것을 권장합니다.

```text
docs/
├── images/
│   ├── main.png          # 메인 화면
│   ├── map-search.png    # 지도 및 장소 검색
│   └── shelter-list.png  # 대피소 목록
└── erd.png               # ERD 이미지
```

이미지를 추가한 뒤 README에 다음과 같이 연결할 수 있습니다.

```markdown
![메인 화면](docs/images/main.png)
![지도 검색](docs/images/map-search.png)
![ERD](docs/erd.png)
```

위 파일들은 README 구성을 위한 예시 경로이며, 실제 이미지 파일을 추가해야 표시됩니다.

## 💡 프로젝트를 통해 다룬 내용

- Spring Boot의 Controller–Service–Repository 계층 구조를 활용한 API 구성
- Spring Data JPA를 이용한 MySQL 데이터 조회
- 카카오 지도 API와 웹 프런트엔드 연동
- 위도·경도 경계값을 이용한 지도 영역 기반 검색
- 디바운스를 활용한 지도 이벤트 요청 제어
- 재난 유형별 데이터 모델 및 API 분리
- 엑셀 기반 데이터를 DB에 적재하고 확인하는 스크립트 구성

## 🛠️ 향후 개선 방향

- 환경 변수 및 비밀정보 관리 체계 강화
- CORS 허용 출처 제한 및 API 입력값 검증 보강
- 서버 페이지네이션과 공간 인덱스 적용 가능성 검토
- 예외 상황에 대한 일관된 API 오류 응답 설계
- 배포 환경별 설정 분리 및 배포 자동화
- 지도·검색·위치 기능의 통합 테스트 보강

---

**대피할지도**는 재난 유형별 대피소 데이터를 지도 기반으로 탐색할 수 있도록 구현한 프로젝트입니다. 위치 기반 조회와 계층형 백엔드 구조를 통해 사용자가 필요한 정보를 보다 직관적으로 확인하는 것을 목표로 합니다.


---

## 📸 프로젝트 화면 및 ERD

> <span style="color:#E67E22"><strong>🟠 [추가 예정] 실제 실행 화면</strong></span>
>
> 아래 위치에 프로젝트 실행 화면 캡처를 추가하세요.
>
> <!-- TODO: 실제 실행 화면 이미지를 업로드한 뒤 아래 예시를 수정하세요. -->
> <!-- 예시: ![메인 화면](docs/images/main-screen.png) -->
>
> <span style="color:#E67E22"><strong>🟠 [추가 예정] 지도 기반 대피소 조회 화면</strong></span>
>
> <!-- TODO: 지도에서 대피소를 조회하는 화면 캡처를 추가하세요. -->
> <!-- 예시: ![대피소 조회 화면](docs/images/shelter-map.png) -->
>
> <span style="color:#E67E22"><strong>🟠 [추가 예정] ERD (데이터베이스 설계도)</strong></span>
>
> <!-- TODO: ERD 이미지 파일을 docs/images/erd.png 경로에 추가한 뒤 아래 예시를 수정하세요. -->
> <!-- 예시: ![ERD](docs/images/erd.png) -->

