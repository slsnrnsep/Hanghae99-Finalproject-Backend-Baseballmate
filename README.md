# Hanghae99-Finalproject-Backend-Baseballmate [![Build Status](https://app.travis-ci.com/slsnrnsep/Habit-Tracker.svg?branch=main)](https://app.travis-ci.com/slsnrnsep/Habit-Tracker)

프로 야구 직관러들을 위한 야구 모임 생성 서비스
# ⚾Meetball(미트볼)🥎

![11조 썸네일](https://user-images.githubusercontent.com/90598889/145743459-023a8322-f7d7-40c8-900e-2da9ea14fca3.png)

Meetball은 **야구를 좋아하고 직관을 즐기는 사람들을 위한 모임 생성 서비스**입니다.

야구 경기 직관과 스크린 야구를 위한 모임 형성 서비스, 커뮤니티, 굿즈 게시판 등 야구를 좋아하는 사람들이 다양하게 이용할 수 있는 기능들을 제공합니다.

사이트 URL <p style='font-size:22px;'>🌐<a href='https://meetball.shop' target='_blank'>https://meetball.shop</a></p>

---

## 📝 프로젝트 개요

- 프로젝트 이름 : 미트볼(meetball)
- 개발 기간 : 2021.10.24 ~ 2021.12.04
- 팀원
    - Front-end(React) : <a href='https://github.com/lipton-web' style="text-decoration:none" target='_blank'>최진식</a>, <a href='https://github.com/jaeilnet' style="text-decoration:none" target='_blank'>주재일</a>, <a href='https://github.com/sinyubin' style="text-decoration:none" target='_blank'>신유빈</a>
    - Back-end(Spring) : <a href='https://github.com/slsnrnsep' style="text-decoration:none" target='_blank'>정영호</a>, <a href='https://github.com/codenamehee' style="text-decoration:none" target='_blank'>정희윤</a>, <a href='https://github.com/Woong5026' style="text-decoration:none" target='_blank'>김태웅</a>
- <a href='' target='_blank' >Notion</a>
- <a href='https://youtu.be/cviKHk5Aqi8'>시연 영상</a>
- <a href='https://github.com/slsnrnsep/Hanghae99-Finalproject-Backend-Baseballmate' target='_blank'>Github</a>

---

## 🧩 프로젝트 구조(아키텍쳐)

![프로젝트 아키텍쳐](https://user-images.githubusercontent.com/90598889/145743533-56d5d596-9566-48fb-be2f-73be6843a0c5.png)

---

## 🧰 기술 스택

Backend

- java 8
- Gradle
- Spring Boot 2.5.2
- Spring Security
- JPA
- QueryDSL
- MySQL
- Web Socket
- Redis
- Nginx
- Jenkins

---

## ⚙ 주요 기능

<details>
<summary>로그인</summary>

- Spring Security + JWT 기반 일반 로그인
- 카카오 소셜 로그인이 가능합니다.
</details>

<details>
<summary>회원가입</summary>

- 아이디 및 닉네임의 중복확인을 자동으로 체크합니다.
- 문자 인증을 통해 휴대폰 인증을 할 수 있습니다.
</details>

<details>
<summary>메인(경기모임) 페이지</summary>

- 전체 모임 게시글들을 조회할 수 있습니다.
- 모임 게시글들을 구단/경기 일정/참여도가 높은 순으로 조회할 수 있습니다.
- 플로팅 버튼을 누르면 모임 게시글을 작성할 수 있습니다.
- 무한 스크롤을 통해 모임 게시글들을 계속해서 불러올 수 있습니다.
- 특정 게시글을 누르면 해당 게시글의 상세 페이지로 넘어갈 수 있습니다.
- 로그인 페이지, 마이페이지(로그인이 완료되었을 경우)로 넘어갈 수 있습니다.
</details>

<details>
<summary>스야 모임 페이지</summary>

- 전체 스크린 야구 모임 게시글들을 조회할 수 있습니다.
- 스크린 야구 모임 게시글들을 지역별로 조회할 수 있습니다.
- 플로팅 버튼을 누르면 스크린 야구 모임 게시글을 작성할 수 있습니다.
- 무한 스크롤을 통해 스크린 야구 모임 게시글들을 계속해서 불러올 수 있습니다.
- 특정 게시글을 누르면 해당 게시글의 상세 페이지로 넘어갈 수 있습니다.
</details>

<details>
<summary>경기 모임/스크린 야구 모임 게시글 작성 페이지</summary>

- 모임 게시글의 제목과 소개글을 작성할 수 있습니다.
- [경기 직관 모임] 구단을 선택하면 해당 구단이 치르는 경기 일정들을 조회하고 그 중 직관 갈 경기를 선택 할 수 있습니다.
- [스크린 야구 모임] 지도에서 스크린 야구장을 선택할 수 있습니다.
- 모임의 인원 수를 최대 8명까지 설정할 수 있습니다.
- 모임 게시글에 함께 등록할 사진을 업로드할 수 있습니다.
- 모임 게시글을 생성하면 해당 모임에 대한 채팅방이 함께 생성됩니다.
</details>

<details>
<summary>경기 모임/스크린 야구 모임 상세 페이지</summary>

- 모임 게시글들의 자세한 정보를 조회할 수 있습니다.
- 모임 게시글을 찜하여 '찜한 모임' 페이지에서 모아서 조회할 수 있습니다.
- 방명록에 댓글을 남기거나 수정, 삭제할 수 있습니다.
- 방명록에 남겨진 댓글에 좋아요를 할 수 있습니다.
- [모임 생성자의 경우] 모임 게시글을 수정, 삭제할 수 있습니다.
- [모임 생성자의 경우] '모임 확정하기'를 통해 더이상 해당 모임의 참여신청을 받지 않도록 할 수 있습니다.
- [모임 생성자의 경우] '모임 확정 취소하기'를 통해 모임 확정을 취소할 수 있습니다.
- [모임 참여자의 경우] 모임에 참여 신청을 할 수 있습니다.
- [모임 참여자의 경우] 모임에 대한 참여 신청을 취소할 수 있습니다.
</details>

<details>
<summary>커뮤니티 페이지</summary>

- 서비스 내의 다양한 유저들이 올린 게시글들을 조회할 수 있습니다.
- 특정 게시글을 선택하면 해당 게시글의 상세 페이지로 넘어갈 수 있습니다.
- 플로팅 버튼을 누르면 커뮤니티 게시글을 작성할 수 있습니다.
</details>

<details>
<summary>커뮤니티 상세 페이지</summary>

- 해당 커뮤니티 게시글에 댓글을 작성할 수 있습니다.
- 타인이 등록한 댓글에 좋아요를 할 수 있습니다.
- [게시글 작성자] 커뮤니티 게시글을 수정, 삭제할 수 있습니다.
- [댓글 작성자] 해당 댓글을 수정, 삭제할 수 있습니다.
</details>

<details>
<summary>굿즈자랑 페이지</summary>

- 굿즈 자랑 페이지에 올라온 게시글들을 모두 조회할 수 있습니다.
- '더보기' 버튼을 통해 감춰진 게시글 내용을 조회할 수 있습니다.
- '댓글 더보기' 버튼을 통해 게시글에 등록된 댓글들을 조회할 수 있습니다.
- 무한 스크롤을 통해 게시글들을 계속해서 불러올 수 있습니다.
- 플로팅 버튼을 누르면 굿즈자랑 게시글을 작성할 수 있습니다.
</details>

<details>
<summary>굿즈자랑 게시글 작성 페이지</summary>

- 굿즈 게시글에 올릴 사진을 업로드할 수 있습니다.
- 굿즈 게시글의 이름, 내용을 작성할 수 있습니다.
</details>

<details>
<summary>내 모임 페이지</summary>

- 내가 참여한 모임 게시글들을 조회할 수 있습니다.
- 내가 작성한 모임 게시글들을 조회할 수 있습니다.
- 내가 찜한 모임 게시글들을 조회할 수 있습니다.
- 필터 기능을 통해 전체/경기 직관/스크린 야구 모임들을 나누어 조회할 수 있습니다.
</details>

<details>
<summary>채팅 페이지</summary>

- 참여하거나 직접 개설한 경기 직관 모임/스크린 야구 모임 게시글의 채팅방들을 모두 조회할 수 있습니다.
- 특정 채팅방으로 넘어가서 초대되어있는 유저들과 채팅을 나눌 수 있습니다.
</details>

<details>
<summary>마이페이지</summary>

- 초기에 선택했던 내 구단을 변경할 수 있습니다.
- 거주지역을 설정할 수 있습니다.
- 100자 이하의 자기소개를 등록할 수 있습니다.
- 로그아웃을 할 수 있습니다.
</details>

<details>
<summary>알람페이지</summary>

- 작성한 게시글에 댓글, 좋아요가 추가될 경우 해당 이벤트에 대한 알람을 조회할 수 있습니다.
- 경기 직관 모임, 스크린 야구 모임에 참가신청이 들어올 경우 참가신청 탭에서 해당 신청에 대한 승락/거절 여부를 결정할 수 있습니다.
- 특정 유저의 모임 참가신청을 승락할 경우 해당 유저가 모임의 단체 채팅방에 초대됩니다.
</details>

---

## 🛠 개선사항

<a href='' target='_blank'>사용자 피드백 바로가기 </a>
