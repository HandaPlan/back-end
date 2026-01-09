<div align = "center">
<a href = "https://handa-plan.vercel.app/" target="_blank"><img width="2048" height="1546" alt="image" src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FMVluP%2FdJMcadm74Cv%2FAAAAAAAAAAAAAAAAAAAAABbs_8z3qIw-3fqPwJb6jDCyhLFeaylTek17Y7ePnaU2%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1769871599%26allow_ip%3D%26allow_referer%3D%26signature%3DCLvWeynZxrirWhLLT8bIj%252BL1qmE%253D" /></a>
<em>(위 이미지를 클릭하면, 한다라트 사이트로 이동합니다.)</em>
</div>

## 0. 한 줄 소개
**한다라트는 만다라트 구조를 기반으로, 목표를 행동 단위로 분해해 실행과 기록을 관리하는 목표 관리 서비스입니다.**
<br><br><br>

## 1. 서비스 제작 동기
‘갓생’이라는 표현이 일상화될 만큼, 많은 사람들이 더 나은 하루와 성취를 원하고 있습니다.<br>
하지만 목표를 세우는 것에서 그치고, 무엇을 어떻게 실천해야 할지 정리하지 못해 실행으로 이어지지 않는 경우가 많다고 느꼈습니다.<br>

큰 목표를 달성하기 위해서는 막연한 다짐이 아니라, **오늘 당장 수행할 수 있는 구체적인 행동 단위**로의 분해가 필요하다고 생각했습니다.<br>

한다라트는 **만다라트 구조**를 활용해 하나의 목표를 세부 목표와 일일 행동으로 나누고, **실천 과정을 기록하며 목표 달성까지의 흐름을 관리**할 수 있도록 기획되었습니다.
<br><br><br>
## 2. 역할
- 도메인 설계 및 데이터 모델링
- 기능 개발
- 개발 서버 배포 및 운영
<br><br><br>
## 3. 핵심 기능
### 3.1 메인골, 서브골, 데일리 액션 생성
하나의 메인 목표를 중심으로 서브 목표와 데일리 액션을 단계적으로 생성합니다.<br>
**메인골 → 서브골 → 데일리 액션** 구조를 통해 목표를 실행 가능한 단위로 분해할 수 있습니다.
<img width="2048" alt="image" src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2F7HTz4%2FdJMcafrLNsU%2FAAAAAAAAAAAAAAAAAAAAAPZBRZgqaqycZ3U8-srrJbwwV29WxlRIoXoDSU-mmX3h%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1769871599%26allow_ip%3D%26allow_referer%3D%26signature%3D3R%252Fz6GyueA7tEOF0GseSlV98uUE%253D" />


### 3.2 메인골 및 서브골 진행도 수치화
메인골에 속한 서브골들의 데일리 액션 기록을 기반으로 메인골과 서브골의 **주간 진행도**를 계산합니다.<br>
사용자는 목표 달성을 위해 주간 동안 얼마나 노력했는지를 **정량적인 수치로 파악**할 수 있습니다.
<img width="2048" alt="image" src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2F2IBXD%2FdJMcajgAyeN%2FAAAAAAAAAAAAAAAAAAAAAHOFOKahwKWwYS4VJ_4DFgwh69vgMN-1b8qjz-b8va1s%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1769871599%26allow_ip%3D%26allow_referer%3D%26signature%3DZq%252FV1ueoQUOD24R7QaabSG%252B5gxk%253D" />


### 3.3 기존 목표 재사용
이미 생성한 메인골과 서브골을 복사하여 새로운 목표로 재사용할 수 있습니다.<br>
반복적으로 설정하는 목표 구조를 다시 만들 필요 없이, **기존 목표를 기반으로 빠르게 새로운 계획을 시작**할 수 있도록 했습니다.<br>
이를 통해 목표 설정에 드는 피로도를 줄이고, 사용자가 실천과 기록에 더 집중할 수 있도록 돕습니다.<br><br>
<div align = "center"><img width="300" alt="image" src="https://blog.kakaocdn.net/dna/SZdQg/dJMb99ZoPxJ/AAAAAAAAAAAAAAAAAAAAAO3uRRoejR6bLzCZbVbZA88yQAOGDtNbZGXEleP3YPs6/img.gif?credential=yqXZFxpELC7KVnFOS48ylbz2pIh7yKj8&expires=1769871599&allow_ip=&allow_referer=&signature=DHZTqyF5HowyUldkPTSLItd7vts%3D" /></div>
<br><br><br>


## 4. 기술 스택
- Java 17 / Spring Boot 3.4.4
- Spring Security, JWT, Redis
- JPA, QueryDSL, MySQL, Flyway
- AWS EC2, Nginx, S3, CloudFront
<br><br><br>


## 5. 아키텍처
<div align = "center"><img width="1024" alt="image" src="https://github.com/user-attachments/assets/90238e41-c8c0-4586-af7d-fbb512f8a586" /></div>
<br>

- 클라이언트 요청은 Nginx를 통해 Spring Boot 애플리케이션으로 전달됩니다.
- 인증은 JWT 기반으로 처리하며, Refresh Token은 Redis에 저장해 관리합니다.
- 정적 리소스(이미지)는 S3 + CloudFront를 통해 제공해, 애플리케이션 서버는 비즈니스 로직과 API 처리에 집중하도록 역할을 분리했습니다.


<br><br><br>
## 6. 기술 선택 이유
### 6.1 QueryDSL
- 한다라트는 주간/월간 단위로 데일리 액션의 체크 현황을 집계해 서브골 진행률과 캘린더 형태의 화면 데이터를 제공합니다.
- 복잡한 조건의 집계 쿼리와 DTO 반환을 위해 QueryDSL을 사용했습니다.

### 6.2 Flyway
- 개발 도중 `subGoal` 테이블의 `color`를 `slotNum`으로 변경해야 하는 상황이 생겼습니다.
- 배포 DB에 이미 color 필드가 있는 데이터가 있어 안정적인 데이터 변경이 필요했습니다.
- **학습 곡선이 완만**하면서도, 버전 관리 기반의 안정적인 마이그레이션이 가능하다는 점에서 **Flyway를 선택**했습니다.
  
<br><br><br>
## 7. 트러블 슈팅
- `가져오기 API` : 데이터 누적에 따른 응답 크기/조회 비용 증가를 고려해, 단일 조회 API를 목적별로 분리
- `홈 API` : 주간 진행도는 주차 전환 시점에 따른 정합성 이슈를 고려해 DB 저장 대신 조회 시점에 계산하도록 변경
- `엔티티 연관관계` : 연관관계 관리 복잡도를 줄이기 위해 단방향 매핑 중심으로 설계


