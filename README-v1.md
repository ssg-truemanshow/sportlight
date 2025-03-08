# Sportlight
신세계아이앤씨 JAVA 기반 백엔드 개발자 과정 파이널 프로젝트

## 프로젝트 소개
**Sportlight**는 원데이 스포츠 클래스 예약 플랫폼 개발 프로젝트 입니다.
- ``Sportlight`` : 원데이 클래스를 통해 스포츠를('Sports') 가벼운 마음으로('Light') 즐길 수 있도록 하여 각광받는 서비스를('Spotlight') 목표로 한다는 의미

## 프로젝트 일정
개발 기간: 2024.10.28 - 2024.12.10 (6주)
- **1주차** : 기획 및 기획안 작성, 시장 분석 및 벤치마킹 보고서 작성, 수요조사 & SWOT 분석, WBS 작성
- **2주차** : 기능 분담 및 상세 정의, API 명세 & ERD 설계, UI 디자인
- **3주차** : 사용자 백엔드, 관리자 프론트엔드 구현
- **4주차** : 사용자 프론트엔드, 관리자 백엔드 구현
- **5주차** : 백엔드, 프론트엔드 연결, NCP 서버 세팅
- **6주차** : 버그 해결, NCP 서버 테스트, 최종 산출물 작성, 프로젝트 발표
  
## 개발 팀
### 트루만쇼 (True만Show)
체계적인 테스트를 통해 사용자에게 신뢰를 주는 서비스를 개발하겠다는 포부를 담았다.
<table>
  <thead>
    <tr align=center >
      <td>
      <b>@kinggora</b>
      </td>
      <td>
        <b>@HongYong-Woo</b>
      </td>
      <td>
        <b>@PARK-TH</b>
      </td>
      <td>
        <b>@bottomsUp-99</b>
      </td>
      <td>
        <b>@Jeongu01</b>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr valign=top>
      <td>
        <div>팀장</div>
        <ul>
          <li>강사 메뉴</li>
        </ul>
      </td>
      <td>
        <div>팀원</div>
        <ul>
          <li>고객 센터, 챗봇</li>
          <li>서버 관리</li>
        </ul>
      </td>
      <td>
        <div>팀원</div>
        <ul>
          <li>로그인/회원 관리</li>
        </ul>
      </td>
      <td>
        <div>팀원</div>
        <ul>
          <li>관리자 페이지</li>
        </ul>
      </td>
      <td>
        <div>팀원</div>
        <ul>
          <li>클래스 관리</li>
        </ul>
      </td>
    </tr>
  </tbody>
</table>

## 시장 분석
### 1. 2024 트렌드
- 분초 사회: 시간이 중요한 자원으로 변모하며 경험의 가치를 중시하는 현대 사회는 더 높은 시간 밀도와 빠른 속도로 나아가고 있다.
- 리퀴드 폴리탄: 유목적 라이프스타일 확산으로 지역이 유연해지며, 관계 인구 중심의 '리퀴드 폴리탄'이 지역 소멸의 대안으로 주목받고 있다.
### 2. LMS 시장 규모 및 성장 가능성
**국내**
- 현재(2024년): 시장 규모가 13조 5,000억원으로 추정
- 미래(2032년): 약 44조에 도달 (연평균 성장률 15.7%)

**글로벌**
- 현재(2021년): 약 2,000조로 추정
- 미래(2032년): 약 4,800조에 도달 (연평균 성장률 8.2%)

### 3. 현직자 인터뷰
온라인 강의 플랫폼에서 강의를 업로드하고 있는 A 요가의 송 대표님
- 높은 수수료: 적게는 30%, 많게는 80%
- 인플루언서에게 유리: 조회수를 통한 수익구조이므로 개인 홍보에서는 상당히 어려움
- 경쟁사 촬영의 책임: 경쟁사에 촬영한다는 도의적인 부분의 책임
- 저비용 운동: 현재는 금전적으로 부담이 적은 운동 선호

### 4. 설문 조사
- **대상** : 다양한 직업군의 10-50대 남여 150명
- **조사 내용** : 스포츠 온/오프라인 클래스에 대한 경험, 스포츠 원데이 클래스에 대한 선호도, 클래스로 제공되었으면 하는 스포츠 종목, 클래스 제공 빈도, 수강 금액 등

![Image](https://github.com/user-attachments/assets/5288be67-5835-4711-ab0c-cf07dfc719bc)

## 벤치마킹
**클래스 101 & 모카클래스**

두 플랫폼의 이용자층, 접속 환경, 주요 구성, 디자인 레이아웃, 차별화 서비스, 고객 문의, 마케팅을 종합적으로 분석하여 벤치마킹 할 공통점과 차별점을 도출하였다.
- 전연령층 포괄
- 오프라인 클래스
- 문의 시스템(챗봇&고객센터)
- 개인 회원과 크리에이터 회원 서비스 구별 
- 배너와 클래스 카드 형태로 배치
- 직관적이고 깔끔한 UI
- 리뷰 시스템

## SWOT 분석
![Image](https://github.com/user-attachments/assets/4184fad3-b129-4a63-be00-c13fdd6a1459)

## 개발 환경 및 기술 스택

![Image](https://github.com/user-attachments/assets/752d1e47-3525-44f1-b39c-219626ac3d0f)
- **IDE**: IntelliJ IDEA Ultimate
- **Backend**: Java 17, Spring Boot 3.0.1, Spring Security 6, JPA, 
- **Frontend**: JavaScript, HTML5, CSS3, Vue.js 3, Bootstrap
- **API**: Kakao Map, Kakao Login, Toss Pay, OpenID, Daum Postcode, CLOVA Chatbot
- **Test**: Postman, JUnit5
- **Data**: MySQL 8.0.21, MyBatis, Redis
- **Library**: Thymeleaf, Bootstrap
- **Design**: Figma, ERD Cloud, Draw.io, ChatGpt
- **Communication**: GitHub, Notion, Slack, Google Workspace, Discord
- **Infrastructure**: Naver Cloud Platform, Nginx

## WBS
![Image](https://github.com/user-attachments/assets/53cb91d1-1e25-4f27-8f61-78a25efe03ec)

## ERD
![Image](https://github.com/user-attachments/assets/f3dca603-f52e-41b6-8886-1142e489694a)

## GitHub 구조
- **Organization**: ssg-truemanshow
- **Repository**
- sportlight: Backend
- sportlight-front: User Frontend
- sportlight-admin-front: Admin Frontend

## 프로젝트 구조
![Image](https://github.com/user-attachments/assets/1795abda-790f-4e16-8370-fa9991c2b8ea)

## 주요 기능


## 커밋 메세지 컨벤션
**Commit Type Gitmoji**
- 🐛 bug: 버그 수정
- ✨ sparkles: 새로운 기능 도입
- 🔥 fire: 코드나 파일 삭제
- 📝 memo: 문서 추가 또는 업데이트
- 🎨 art: 코드의 구조/형식 개선
- 🚧 construction: 작업 진행 중
- 🎉 tada: 프로젝트 시작
- ✅ white_check_mark: 테스트 추가, 업데이트 또는 통과
- 🔧 wrench: 설정 파일 추가 또는 업데이트
- 📦 package: 컴파일된 파일이나 패키지 추가 또는 업데이트
- ♻ recycle: 코드 리팩토링
