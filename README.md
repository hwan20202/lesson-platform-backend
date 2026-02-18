# 🎓 Mentoring Platform

### 🎥 시연 영상
(영상 링크 추가)

---

## 📌 프로젝트 개요

멘토링, 강연, 과외와 같은 지식 공유 모임을 위한 플랫폼입니다.  
멘토는 수업을 등록하고, 멘티는 원하는 수업을 검색·신청·결제할 수 있습니다.

플랫폼은 검색, 결제, 스케줄 관리, 리뷰 기능을 제공하며  
멘토와 멘티를 연결하는 중간 다리 역할을 수행합니다.

---

## 🎯 프로젝트 목표

- 수업 등록 → 신청 → 결제 → 스케줄 확정 → 리뷰까지 이어지는 **예약 및 결제 시스템 구현**

--- 

## 🚀 주요 기능 (Key Features)

### 🔐 인증 & 회원 관리
- 회원가입 / 로그인 (JWT 기반 인증)
- 프로필 이미지 / 닉네임 / 자기소개 수정
- 마이 프로필 조회

### 🔎 검색 & 카테고리
- 통합 검색 기능
- 카테고리별 레슨 조회
- 메인 페이지 레슨 탐색

### 📚 레슨 관리
- 레슨 등록
- 레슨 상세 조회
- 레슨 신청
- 내 레슨 조회 및 수정

### 📅 스케줄 관리
- 스케줄 생성
- 스케줄 전체 / 상세 조회
- 상태 변경
- 예정 / 지난 스케줄 구분 조회

### 💳 결제 시스템
- 레슨 신청 후 결제 진행
- Toss Payments 연동
- 결제 결과 처리

### ⭐ 리뷰 시스템
- 작성 가능한 리뷰 조회
- 리뷰 작성
- 작성한 리뷰 조회

---

## 🏗 기술 스택

### 🖥 Frontend
- React 18.3 + TypeScript
- Vite / pnpm
- Tailwind CSS v4 / MUI 7
- Zustand
- TanStack Query & Router
- Axios / MSW

### ⚙ Backend
- Spring Boot 4.0 (Java 21)
- Spring Security + JWT
- JPA / Hibernate / JDBC
- Springdoc OpenAPI
- Transaction 기반 동시성 제어

### 🗄 Infrastructure
- MySQL 8.x
- Toss Payments SDK
- Python (Bulk Data Seeding)

### 🤝 Collaboration
- Git
- Discord
- Figma
- Gemini (AI Code Review)

---

## 🗂 설계

### ERD
[ERD 설계](https://www.erdcloud.com/d/uCjxXfA7wQvDGhqCd)

<img width="1274" height="704" alt="image" src="https://github.com/user-attachments/assets/29c7cc36-25db-4989-9b4d-64785cf7e69b" />
