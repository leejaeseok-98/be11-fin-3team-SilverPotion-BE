# 📁 프로젝트명

<div align="center">
  <img src=https://github.com/beyond-sw-camp/be11-fin-3team-SilverPotion-BE/blob/main/img/%EA%B0%9C%EC%9A%94.png >
</div>

>실버세대의 건강관리와 사회적 교류를 위한 플랫폼

---

## 📌 목차

1. [프로젝트 개요](#프로젝트-개요)
2. [문서 목록](#문서-목록)
3. [프로젝트 기획서 (프로젝트기획)](#프로젝트-기획서-프로젝트기획)
4. [요구사항 정의서 (프로젝트기획)](#요구사항-정의서-프로젝트기획)
5. [시스템 아키텍처 설계서 (프로젝트기획)](#시스템-아키텍처-설계서-프로젝트기획)
6. [WBS (프로젝트기획)](#wbs-프로젝트기획)
7. [ERD (프로젝트기획)](#erd-프로젝트기획)
8. [화면설계서 (프로젝트기획)](#화면설계서-프로젝트기획)
9. [프로그램사양서 (백엔드 설계 및 구축)](#프로그램사양서-백엔드-설계-및-구축)
10. [단위 테스트 결과서 (백엔드 설계 및 구축)](#단위-테스트-결과서-백엔드-설계-및-구축)
11. [UI/UX 단위 테스트 결과서 (프론트엔드 설계 및 구축)](#uiux-단위-테스트-결과서-프론트엔드-설계-및-구축)
12. [배포 후 통합 테스트 결과서 (시스템 통합)](#배포-후-통합-테스트-결과서-시스템-통합)
13. [CI/CD 계획서 (시스템 통합)](#cicd-계획서-시스템-통합)
14. [배포 및 운영](#배포-및-운영)
15. [팀원 정보](#팀원-정보)

---

## 📖 프로젝트 개요

- **프로젝트명** : 실버포션
- **프로젝트 배경** : <br>
<div align="center">
  <img src=https://github.com/beyond-sw-camp/be11-fin-3team-SilverPotion-BE/blob/main/img/%ED%97%AC%EC%8A%A4%EC%BC%80%EC%96%B4%20%EC%88%98%EC%9A%941.png >
</div>
<div align="center">
  <img src=https://github.com/beyond-sw-camp/be11-fin-3team-SilverPotion-BE/blob/main/img/%EB%85%B8%EC%9D%B8%20%EC%BB%A4%EB%AE%A4%EB%8B%88%ED%8B%B0%EC%97%90%20%EB%8C%80%ED%95%9C%20%EC%88%98%EC%9A%94.png >
</div>

1️⃣초고령 사회 진입
대한민국은 작년 12월, 전체 인구 중 65세 이상 노인이 차지하는 비율이 20%를 넘어가며 **초고령 사회에 진입**했습니다.<br>
이에 따라, 노인 인구의 의료 서비스, 돌봄 수요가 급증하고 있습니다.<br><br>
2️⃣노인인구 중 노인 1인 가구 혹은 노인 부부가구가 90%에 도달했습니다.<br>
노인인구의 대부분이 자식과 떨어져 산다는 것으로, 노인의 가족 및 보호자에게 있어 <br>
**원격으로 부모의 건강을 모니터링하고 돌봄에 참여할 수 있는 시스템에 대한 수요**가 커지고 있습니다.<br><br>
3️⃣한국 노인의 사회적 고립도는 OECD 평균에 비해 3배 이상 높습니다.<br>
이러한 사회적 고립도는 치매나 고독사 증가를 가중시켜 사회의 부담을 높이고 있습니다.<br><br>

**목표 및 목적** :
**1️⃣ 노년층의 건강 관리 효율성 강화**
**2️⃣ 가족 및 보호자의 안심 돌봄 환경 구축**
**3️⃣ 사회적 고립 해소 및 정서적 안정 지원**

- **주요 기능 요약** :  
  **1️⃣ 실버 헬스케어**
    - 갤럭시 워치, 애플워치 등 웨어러블 기기 연동을 통한 실시간 데이터 수집(걸음수,심박수,체온,혈압,소모칼로리 등)
    - 수집된 데이터를 통해 일/주/월 단위로 건강 리포트 제공
    - AI가 개인 건강 데이터를 분석해 약/영양제, 병원 위치, 운동 방법 추천
    - 보호자가 노인의 건강 데이터를 조회할 수 있음

  **2️⃣ 소모임(커뮤니티)**
    - 사용자가 자신의 관심사(운동,요리,독서,음악)등을 기반으로 손쉽게 모임 생성 및 가입 가능
    - 모임별로 독립된 커뮤니티 공간을 제공하여 각 모임마다 홈,게시판,사진첩,채팅방 기능을 제공

---

## 📄 문서 목록

| 문서명 | 분류 | 설명 | 다운로드 |
|:----------------------------------|:--------------------------|:------------------------------------------|:---------------------------------------------|
| 프로젝트 기획서 (프로젝트기획) | 프로젝트기획 | 프로젝트 배경, 목적, 추진 전략 등 | [📎 프로젝트 기획서 다운로드](./docs/프로젝트_기획서.pdf) |
| 요구사항 정의서 (프로젝트기획) | 프로젝트기획 | 기능 및 비기능 요구사항 정의 | [📎 요구사항 정의서 바로가기](https://docs.google.com/spreadsheets/d/1xPL9fzuUFguVioFHqQyqesHNe3U2_l63kEcYtexFRvk/edit?gid=1960802497#gid=1960802497) |
| 시스템 아키텍처 설계서 (프로젝트기획) | 프로젝트기획 | 시스템 구성 및 흐름도 | [📎 시스템 아키텍처 설계서 다운로드](./docs/시스템_아키텍처_설계서.pdf) |
| WBS (프로젝트기획) | 프로젝트기획 | 업무 분장 및 세부 일정 | [📎 WBS 바로가기](https://docs.google.com/spreadsheets/d/1xPL9fzuUFguVioFHqQyqesHNe3U2_l63kEcYtexFRvk/edit?gid=1809279197#gid=1809279197) |
| ERD (프로젝트기획) | 프로젝트기획 | 데이터베이스 구조 및 관계 | [📎 ERD 바로가기](https://www.erdcloud.com/d/5zTkSLQ7qB9hg6b4G) |
| 화면설계서 (프로젝트기획) | 프로젝트기획 | UI/UX 화면 흐름 및 구조 | [📎 화면설계서(피그마) 바로가기 ](https://www.figma.com/design/lieOgqHknZxpzSkXT6IsX7/silverpotion?node-id=0-1&t=f7PFNSWT2n8etaXt-1) |
| API명세서 (백엔드 설계 및 구축) | 백엔드 설계 및 구축 | 각 기능별 상세 사양 기술 | [📎 API명세서 바로보기](https://docs.google.com/spreadsheets/d/1xPL9fzuUFguVioFHqQyqesHNe3U2_l63kEcYtexFRvk/edit?gid=1214004648#gid=1214004648) |
| 단위 테스트 결과서 (백엔드 설계 및 구축) | 백엔드 설계 및 구축 | 기능별 테스트 수행 결과 | [📎 단위 테스트 결과서 다운로드](./docs/단위_테스트_결과서.pdf) |
| UI/UX 단위 테스트 결과서 (프론트엔드 설계 및 구축) | 프론트엔드 설계 및 구축 | 화면 기반 사용자 테스트 결과 | [📎 UI/UX 단위 테스트 결과서 다운로드](./docs/UIUX_단위_테스트_결과서.pdf) |
| 배포 후 통합 테스트 결과서 (시스템 통합) | 시스템 통합 | 배포 후 통합 테스트 결과 정리 | [📎 배포 후 통합 테스트 결과서 다운로드](./docs/배포후_통합_테스트_결과서.pdf) |
| CI/CD 계획서 (시스템 통합) | 시스템 통합 | 배포 및 자동화 파이프라인 계획 | [📎 CI/CD 계획서 다운로드](./docs/CICD_계획서.pdf) |

---

## 📑 프로젝트 기획서 (프로젝트기획)
- 프로젝트 추진 배경, 목적, 전략 정리
  👉 [📎 문서 보기](./docs/프로젝트_기획서.pdf)

---

## 📑 요구사항 정의서 (프로젝트기획)
- 기능적 / 비기능 요구사항, 유즈케이스
  👉 [📎 문서 보기](https://docs.google.com/spreadsheets/d/1xPL9fzuUFguVioFHqQyqesHNe3U2_l63kEcYtexFRvk/edit?gid=1960802497#gid=1960802497)

---

## 📑 시스템 아키텍처 설계서 (프로젝트기획)
> 시스템 구성도 이미지 삽입  
![시스템 아키텍처](./images/architecture.png)  
👉 [📎 문서 보기](./docs/시스템_아키텍처_설계서.pdf)

---

## 📑 WBS (프로젝트기획)
- 업무 분류 체계, 일정계획
  👉 [📎 문서 보기](https://docs.google.com/spreadsheets/d/1xPL9fzuUFguVioFHqQyqesHNe3U2_l63kEcYtexFRvk/edit?gid=1809279197#gid=1809279197)

---

## 📑 ERD (프로젝트기획)
<details>
<summary>데이터베이스 구조 보기</summary>

### 전체 ERD
![스크린샷 2025-03-28 112415](https://github.com/user-attachments/assets/f55b36ad-2a82-4126-b2a4-f14a5e7abe99)
### 유저
![스크린샷 2025-03-28 112538](https://github.com/user-attachments/assets/26e79aeb-b5fb-4156-a274-276320a0b99c)
### 소모임
![스크린샷 2025-03-28 112512](https://github.com/user-attachments/assets/d908d100-0905-441b-845e-92b8b700e6c3)
### 채팅
![스크린샷 2025-03-28 112434](https://github.com/user-attachments/assets/2cc2c98f-37cc-42e7-baed-c3318dbcd8f9)
</details>
👉 [📎 문서 보기](https://www.erdcloud.com/d/5zTkSLQ7qB9hg6b4G)

---

## 📑 화면설계서 (프로젝트기획)
> 주요 UI/UX 흐름  
👉 [📎 문서 보기](https://www.figma.com/design/lieOgqHknZxpzSkXT6IsX7/silverpotion?node-id=0-1&t=f7PFNSWT2n8etaXt-1)

---

## 📑 API명세서
- 상세 기능별 사양, 입력/출력 정의
  👉 [📎 문서 보기](https://docs.google.com/spreadsheets/d/1xPL9fzuUFguVioFHqQyqesHNe3U2_l63kEcYtexFRvk/edit?gid=1214004648#gid=1214004648)

---

## 📑 단위 테스트 결과서 (백엔드 설계 및 구축)
| 테스트 항목 | 테스트 내용 | 결과 | 비고 |
|:-------------|:------------------|:----:|:------|
| 로그인 기능 | 올바른 ID/Password | ✅ 성공 | 정상 작동 |
| 회원가입 기능 | 필수 입력값 누락 테스트 | ✅ 성공 | 예외처리 확인 |

👉 [📎 문서 보기](https://documenter.getpostman.com/view/41079416/2sB2cYbzyb#6ba25797-9674-4873-9b11-99b092b2b0cb)

---

## 📑 UI/UX 단위 테스트 결과서 (프론트엔드 설계 및 구축)
- 사용성 테스트, 인터페이스 반응
<details>
  
  #### - 로그인 및 회원가입-
  <summary> 메인페이지</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441147171-84ce158e-6992-4e6f-bf99-ef0c23879da5.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYwMjUsIm5iZiI6MTc0NjYxNTcyNSwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTQ3MTcxLTg0Y2UxNThlLTY5OTItNGU2Zi1iZjk5LWVmMGMyMzg3OWRhNS5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTAyMDVaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1mY2UxMzMxZjM1NjViYjUwMTIzMWM5MDVjNjdlNjljYzUwNjliMjZiYmY2Yjc4ODJlNDM0MTI3ZDRkYzAxZTVmJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.-MlU28hA492ndFwwWLro7HY6dSFywZcODFf0Pmb-YyI">
</details>
<details>
  <summary> 로그인</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441185109-981cc060-5c46-4fef-87d6-7f8f6ab2568c.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTY0NjUsIm5iZiI6MTc0NjYxNjE2NSwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTg1MTA5LTk4MWNjMDYwLTVjNDYtNGZlZi04N2Q2LTdmOGY2YWIyNTY4Yy5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA5MjVaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0wZThlNDY0ZjJmZWNjMjBiOGYxZGYzNjIyYTYwMTVhYzFhZWM1MTgxYzZiZjYxMmZkZGZlN2QzYWExZmU4ZTBlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.GTVfHHC_rjzcyIwzUtfZKRhBtajWaduU7TanzvvwjWM">
</details>
<details>
  <summary> 회원가입</summary>
  <img src="https://github.com/user-attachments/assets/9508ba22-b7f4-4c24-8a6f-a92672c15033">
</details>
<details>
  <summary> google회원가입</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441185281-335c6a78-a31e-4201-9278-bc85c9a63cd5.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTY0OTQsIm5iZiI6MTc0NjYxNjE5NCwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTg1MjgxLTMzNWM2YTc4LWEzMWUtNDIwMS05Mjc4LWJjODVjOWE2M2NkNS5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA5NTRaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0xNTE1ZmNmY2Y1YzIwMmYxYjVmMjM1NThlMDVlZmU4MmM5MDYzMGUzNjIxNTQzZDc3M2MyNThmYTk4NjdkMjRiJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.bYXATl0dVDoJMkg7OhipEGpBmnTVwG_stoUB5pyUoLU">
</details>
<details>
  <summary> google로그인</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441188121-8da84780-741b-4486-a1d3-6fcbbcb46c85.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTY1MzksIm5iZiI6MTc0NjYxNjIzOSwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTg4MTIxLThkYTg0NzgwLTc0MWItNDQ4Ni1hMWQzLTZmY2JiY2I0NmM4NS5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTEwMzlaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1jNmU5MGFhNDU0ZDliOGZhNGFlMzY1YzRmZDZlODhkYTU0NDA2Y2JkYTQzN2VhZGZlYmIzODdmMjgyNTFkODhjJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.vRCtSvtnIs0LF9tEzYyUx1QRXLioTPlDBMZXY8B8cG4">
</details>
<details>
  <summary> kakao회원가입</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441188396-d0186848-be4c-41f8-a617-3afe8fe09db8.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTY1NTIsIm5iZiI6MTc0NjYxNjI1MiwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTg4Mzk2LWQwMTg2ODQ4LWJlNGMtNDFmOC1hNjE3LTNhZmU4ZmUwOWRiOC5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTEwNTJaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0zMzljNDk2YTY0ZDVkMWYzMzllYzE2MGMzNTRlOTQ1MGM2ZGYzODFlNWM5ODA1NmNlODlkM2FjZGMxNzNkNzI0JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.j498950U_AAdZyHdNI1yWEqcaKGhxJkH8PCCpjRV_O8">
</details>
<details>
  <summary> kakao로그인</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441189919-3f352d8a-fa4b-4c52-b425-462185a08c50.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTY1NjEsIm5iZiI6MTc0NjYxNjI2MSwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTg5OTE5LTNmMzUyZDhhLWZhNGItNGM1Mi1iNDI1LTQ2MjE4NWEwOGM1MC5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTExMDFaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0wMDg2MTcwNDJiZmQzZTQ1OTU2MWIyM2UwNGRmODNhNGViM2VlMzgwOGVhZmNhOGQxMzEzMTRkZWRiMmYxODMxJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.149jeHbwRNuReNpTUipLKTjAphpov2LWny3qDa4ZC_k">
</details>


#### - 건강데이터-
<details>
<summary> 본인 건강데이터 조회</summary>
  <img src="https://private-user-images.githubusercontent.com/176744569/441160856-4d2d4825-bbcc-4c6d-bc5e-a33b7d8f52a8.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYyNTQsIm5iZiI6MTc0NjYxNTk1NCwicGF0aCI6Ii8xNzY3NDQ1NjkvNDQxMTYwODU2LTRkMmQ0ODI1LWJiY2MtNGM2ZC1iYzVlLWEzM2I3ZDhmNTJhOC5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA1NTRaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1mNWQ4ZWIzZWYyY2JjZWMxYTM0OGRhMWY3ZTQwODlkZTFkOGI3ZDgwNGE4N2YzMmI1Y2Q3YWY2MmRmODU4Y2IxJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.TxgGhAgQGbnFtku7dPnBO6TTOt1b7Krd7BvmcOw7HI0">
</details>
<details>
<summary> 본인 AI리포트 조회</summary>
  <img src="https://private-user-images.githubusercontent.com/176744569/441161119-b873fd13-2216-44c1-b261-be22454be71d.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYyODIsIm5iZiI6MTc0NjYxNTk4MiwicGF0aCI6Ii8xNzY3NDQ1NjkvNDQxMTYxMTE5LWI4NzNmZDEzLTIyMTYtNDRjMS1iMjYxLWJlMjI0NTRiZTcxZC5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA2MjJaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1lMDZhNzMyOTU4Mzk3MDM2ZDViNWI2YzFlNTU3YzQwNjc2YThmOWQ3MWExZjM5YzU1NDY0ZGMwMzEzNGE3MjVlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.jRl0am5Y_-oLUNe3CnY0xGtbLaUWbqdb4kstsPv0sD0">
</details>
<details>
<summary> 피보호자 데이터 조회</summary>
  <img src="https://private-user-images.githubusercontent.com/176744569/441161904-d20a3eeb-4097-436a-84e0-a9d3f6d89df5.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYzMDUsIm5iZiI6MTc0NjYxNjAwNSwicGF0aCI6Ii8xNzY3NDQ1NjkvNDQxMTYxOTA0LWQyMGEzZWViLTQwOTctNDM2YS04NGUwLWE5ZDNmNmQ4OWRmNS5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA2NDVaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1hNjY4ZWFlOTE2ZWJlMzI2NTg3MjlmOGM1YzM3MDJlZjYxNTU3ZjhhOTAxOTU5Y2Q3OTgyMzMzYzY5ODYwYWFlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.biZtS9EDSaa_FuiOY7_oatpYRLltFeqGSugHSUKUI3s">
</details>
<details>
<summary> 피보호자 리포트 조회</summary>
  <img src="https://private-user-images.githubusercontent.com/176744569/441162124-b5b91708-a8b7-4445-a90d-4cfd2b984de6.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYzMjAsIm5iZiI6MTc0NjYxNjAyMCwicGF0aCI6Ii8xNzY3NDQ1NjkvNDQxMTYyMTI0LWI1YjkxNzA4LWE4YjctNDQ0NS1hOTBkLTRjZmQyYjk4NGRlNi5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA3MDBaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1hODUyMjVkMTE4MDg4MDJlZTk4OGFhZDQ3ZTJhNzJiZjhhNWQ5OGIxNzE5Y2U0YWQ4YjkwYTk3NmQwZTVlYTY5JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.FbSngGbpw8FpqvyqOiBfJkLdmdCS8F0zMbwN9WwibUI">
</details>
<details>
<summary> 헬스지수 생성 및 조회</summary>
  <img src="https://private-user-images.githubusercontent.com/176744569/441162586-c808c8d3-86b9-4204-b656-fd693bc41792.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTY0MjksIm5iZiI6MTc0NjYxNjEyOSwicGF0aCI6Ii8xNzY3NDQ1NjkvNDQxMTYyNTg2LWM4MDhjOGQzLTg2YjktNDIwNC1iNjU2LWZkNjkzYmM0MTc5Mi5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA4NDlaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0zNDdhOTM3NTgzODM2NGNmMjNiMmI5M2IzNjE4YjFkZWVlMTMyOThkZTMzZWE5OTk2OGYzZjdmMzNjMWFiODc5JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.Grxn1CghlGVvDgvnNefZ8YCjB5o7itHpv4rVkBRCw_M">
</details>
<details>
<summary> 건강데이터기반 모임 추천</summary>
  <img src="https://private-user-images.githubusercontent.com/176744569/441162780-2f42a962-f86c-46df-bf41-1719040c6b77.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTY0NDUsIm5iZiI6MTc0NjYxNjE0NSwicGF0aCI6Ii8xNzY3NDQ1NjkvNDQxMTYyNzgwLTJmNDJhOTYyLWY4NmMtNDZkZi1iZjQxLTE3MTkwNDBjNmI3Ny5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA5MDVaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1lY2JiNmM1NzM2YjE1MzI3YmRhYTgxNDg0ODA4ZDQyMTEwNTllYTEzNThiYjc3Yzk4YjQ1YzA0NDgxOTU3ZWE4JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.NpWFVMDAhQk7ZFvaiAn_pHr13e_6R6qQgzUTNGYMCVI">
</details>

#### - 모임-
<details>
<summary>소모임 생성</summary>
  <img src="https://private-user-images.githubusercontent.com/176744569/441162999-a2f80d17-514c-47e7-bfc5-520dddc41767.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYyMDEsIm5iZiI6MTc0NjYxNTkwMSwicGF0aCI6Ii8xNzY3NDQ1NjkvNDQxMTYyOTk5LWEyZjgwZDE3LTUxNGMtNDdlNy1iZmM1LTUyMGRkZGM0MTc2Ny5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA1MDFaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1jMzIzYTJhMWU4OTNiZWQwYThiNzNkNzcwMGVhNjczMzNlYmQxM2ZjYzI5MmMwNzdmY2ViMWMzZTUwMTQyZWU1JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.nzuarJyOiQmzhQWq3xLt3F08_p1rvmG0E-unrFQxgKQ">
</details>
<details>
<summary>모임 가입</summary>
  <img src="https://private-user-images.githubusercontent.com/176744569/441163221-6d07924a-a5f9-4962-b748-65939a0b947d.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYyMTgsIm5iZiI6MTc0NjYxNTkxOCwicGF0aCI6Ii8xNzY3NDQ1NjkvNDQxMTYzMjIxLTZkMDc5MjRhLWE1ZjktNDk2Mi1iNzQ4LTY1OTM5YTBiOTQ3ZC5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA1MThaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT05Zjg2YzcwNmE3Zjk1NzIwM2FmMDYzNmExNzExMDliOWVlYzQwYjQ3YjAyNzg5MWM3ZWYyN2ZkMDI2YTc2MmU3JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.Y1QNgeDLDh2IoOOG7nau39uq_BxMqO-LGriqC3MbDO4">
</details>
<details>
<summary>정모 만들기</summary>
  <img src="https://private-user-images.githubusercontent.com/176744569/441163409-0c7f2a8a-0d39-4e6e-b5e0-d5cff0914341.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYyMzQsIm5iZiI6MTc0NjYxNTkzNCwicGF0aCI6Ii8xNzY3NDQ1NjkvNDQxMTYzNDA5LTBjN2YyYThhLTBkMzktNGU2ZS1iNWUwLWQ1Y2ZmMDkxNDM0MS5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA1MzRaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1iZTRkOWQwZTY5ZjE3OGE0NjhhYTA5ZjZhZTI2NDc4ODNmOWJiZGVhY2E1ODQ3YmRlZGUwYWE4NDQwNTc4YTMyJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.b5nbMscnqYUpKy_9Knd2-HMi_8jCjlU8xgWiCL3HHHs">
</details>
<details>
<summary>게시판 전체 조회</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441148282-3a4707e4-2dc9-4b07-947b-3453344d5873.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYwNzAsIm5iZiI6MTc0NjYxNTc3MCwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTQ4MjgyLTNhNDcwN2U0LTJkYzktNGIwNy05NDdiLTM0NTMzNDRkNTg3My5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTAyNTBaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT03ZTRkNzBiN2IzZmRiYmY0ZTY2Njk4ZTVjM2M4ZGMxNzczNjQ1M2JlYjI2YmNjODA2OTk1NTFkMjMzNjc0Nzk5JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.5VxW67TetO8qimPO8BKw-ROVA7C3ECWP_uqz0FQyk1I">
</details>
<details>
<summary>게시판 좋아요</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441149928-55ef2b5b-4e54-4e41-a751-8160aa4facc0.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYwOTIsIm5iZiI6MTc0NjYxNTc5MiwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTQ5OTI4LTU1ZWYyYjViLTRlNTQtNGU0MS1hNzUxLTgxNjBhYTRmYWNjMC5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTAzMTJaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT05YmEyYTNjMDVlZjVkOWQzMTYzNWY3M2ExYTk1ZWRhZDMxNDEzODRmODEyMmU0MGZiZWJlOTc1MzQxMWUxYmI1JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.VVk-ANly-AjiBa6xvJqIKcWqg3tPNRJ4dtGGWwfCcNI">
</details>
<details>
<summary>투표 상세 조회</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441152087-78b53846-e624-4a95-9171-21cc6fffb377.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYxMDksIm5iZiI6MTc0NjYxNTgwOSwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTUyMDg3LTc4YjUzODQ2LWU2MjQtNGE5NS05MTcxLTIxY2M2ZmZmYjM3Ny5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTAzMjlaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0zNDE1ZTM4YzYxZTY2YzZkOTQxY2E0MmRjY2QwMGQ0ZmJlMmRmZGY1ODU4MTVlZDQ1NGRiNTRkMmExZTRkNzk1JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.zMTgFpaAUCxKyi-KScC0BjVBxKzXV85SEYElX3c4ppU">
</details>
<details>
<summary>투표 항목 유저 조회</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441152959-4b29d826-b96c-4574-a533-b346a35b4b1d.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYxMzMsIm5iZiI6MTc0NjYxNTgzMywicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTUyOTU5LTRiMjlkODI2LWI5NmMtNDU3NC1hNTMzLWIzNDZhMzViNGIxZC5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTAzNTNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1iN2Y0ZDRmYzYxYTE0OTZiOGYzZWYxYzk5Y2JjODY3OTRkM2QxZjdlMzJiZTZlZTNiODNlOTVjNjJlNzgwZDMxJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.2cbhUQbAHJbbMZu_aEt5plq-pvtWhR2YJOKYvsdC5jc">
</details>
<details>
<summary>자유글 생성</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441154854-66be7ecf-8f6b-4a26-9568-7f64d6caa783.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYxNTEsIm5iZiI6MTc0NjYxNTg1MSwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTU0ODU0LTY2YmU3ZWNmLThmNmItNGEyNi05NTY4LTdmNjRkNmNhYTc4My5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA0MTFaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0xOWQxMWMwZmE0ZDYwZTEzMTczNWQ4Nzg0YmJiMWQ4OTk2YmQxMGI4YTFjN2MzOWNjMzJmODhhZjg4OTRhNDZjJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.QRpICLebc8TnOQT0LimgI0IXBwDKRYmSNsAIkqyZmz0">
</details>
<details>
<summary>공지글 생성</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441155014-e329558f-b521-42ed-b39a-69fe52e61e18.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYxNjgsIm5iZiI6MTc0NjYxNTg2OCwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTU1MDE0LWUzMjk1NThmLWI1MjEtNDJlZC1iMzlhLTY5ZmU1MmU2MWUxOC5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA0MjhaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0wMTQxNDc1ZTIwMjljODcyZWQ2MWIxZTZkZDFmMGQxNWE3MTJjNDIxYWZiYzU4Zjc1N2I1Y2E0Y2IxNWUxY2ZlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.l3zlIn30Qt5TWh5sCCS42nQBX2F0r_YnTLr1Pv2sftw">
</details>
<details>
<summary>투표글 생성</summary>
  <img src="https://private-user-images.githubusercontent.com/185016962/441155139-37c8223c-5074-458b-81ad-5bea7c924cfb.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTYxODIsIm5iZiI6MTc0NjYxNTg4MiwicGF0aCI6Ii8xODUwMTY5NjIvNDQxMTU1MTM5LTM3YzgyMjNjLTUwNzQtNDU4Yi04MWFkLTViZWE3YzkyNGNmYi5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMTA0NDJaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0wMzc3N2NiNDY2Njg1NjEzOWJlNGFkYzMxZTFhYzc5MGU2MzA2MzA1YjcwNDY3NjgyYzc0NzE0NjUwYTY0ODJhJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.ixJZxh86g4CmzPVuyStZE9yU0j2aln36RXG8L5z0C00">
</details>

#### - 채팅-
<details>
<summary>채팅</summary>
  <img src="https://private-user-images.githubusercontent.com/188140712/441139123-57df70ef-64bf-4972-bf11-3de55a4e3ee6.gif?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDY2MTU1NDUsIm5iZiI6MTc0NjYxNTI0NSwicGF0aCI6Ii8xODgxNDA3MTIvNDQxMTM5MTIzLTU3ZGY3MGVmLTY0YmYtNDk3Mi1iZjExLTNkZTU1YTRlM2VlNi5naWY_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTA3JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUwN1QxMDU0MDVaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1kNTE4ZTBmNTliMmY4Y2I2YTk1ZTExNzhjMDQzNGIyYzNiMjI2NWQ3NWY0YTNlOGRmMDFjZGUwM2I5ODlkN2JmJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.IzxNhs41FHSiOdj0cjy1zGPfMF6GAGo-Z9B4-v6zzuc">
</details>

#### - 마이캘린더-








---

## 📑 배포 후 통합 테스트 결과서 (시스템 통합)
- 통합 테스트 항목 및 결과 요약
  👉 [📎 문서 보기](./docs/배포후_통합_테스트_결과서.pdf)

---

## 📑 CI/CD 계획서 (시스템 통합)
| 항목 | 내용 |
|:------------------|:-------------------------------|
| 자동화 도구 | GitHub Actions, Docker |
| 배포 환경 | AWS EC2 / ECS |
| 테스트 방식 | 단위/통합 자동 테스트 포함 |

👉 [📎 문서 보기](./docs/CICD_계획서.pdf)

---

## 🚀 배포 및 운영

- 운영 URL : [https://yourproject.com](https://yourproject.com)
- 배포 환경 : AWS / Vercel / 기타
- 컨테이너 이미지 : `yourproject:latest`

---

## 👨‍👩‍👧‍👦 팀원 정보

| 이름 | 역할 | GitHub |
|:------|:------------------------|:-----------------------------|
| 경수혁 | 팀장 | [github.com/hong](https://github.com/hong) |
| 이재석 | 팀원 | [github.com/lee](https://github.com/lee) |
| 최영일 | 팀원 | [github.com/sung](https://github.com/sung) |
| 김진영 | 팀원 | [github.com/sung](https://github.com/sung) |




### Git Commit Convention
- 커밋 메시지 형식 <br>
  태그 종류 <br>
  Feat : 새로운 기능 추가 <br>
  Fix : 버그 수정 <br>
  Docs : 문서 수정 <br>
  Style : 세미콜론 누락 등 코드 변경이 없는 경우 <br>
  Refactor : 코드 리팩토링 <br>
  Test : 테스트 코드 및 리팩토링 테스트 코드 추가 <br>
  커밋 메시지 작성 규칙 <br>
  제목은 간결하게 작성. <br>
  본문에는 무엇을 변경했는지 또는 왜 변경했는지를 상세히 기록.
