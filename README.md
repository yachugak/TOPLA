# TOPLA
## 1. 프로젝트 개요
>기존 TO-DO앱은 일이 쌓일수록 일의 특성을 고려하여 계획을 짜는것이 어렵다. 이에 기존 방식과는 다르게 작업별 특성을 고려하여 계획을 자동 관리하는 TO-DO앱을 개발하게 되었다.
>해당 서비스는 먼저 사용자에게 스케줄 프리셋을 설정하도록 하여 요일별 작업 가능시간을 받는다. 이후 사용자에게 작업을 입력받을때 작업의 중요도, 마감일, 예상 소요 시간을 입력받아 능동적으로 일정을 계획한다.
>작업의 예상 소요 시간이 긴 작업의경우 서비스에서 자동으로 여러개의 작업으로 쪼개서 여러날에 분산배치 한다.
>새로운 작업이 생기는 것과 같이 기존에 계획된 일정에 변동이 생기면 서비스에서 알아서 계획을 다시 수립해 준다.
>리마인딩 타이밍은 사용자가 따로 설정하지 않아도 자동으로 설정되며 이후 사용자가 원하는 시간대로 조정 가능하다.
>작업에 장소를 지정해 줄 수 있는데 특정 위치를 지정해 줄 수도 있지만, 상호명이나 업종으로 등록하여 사용자 주변에서 사용자가 지정한 상호나 업종과 가장 근접한 장소를 알려준다.

## 2. 일정 자동계산 알고리즘
>작업분배 문제를 작업을 item으로 날짜를 bin으로 하는 Fractional Bin Packing 문제로 모델링 했다.
>기존의 Fractional Bin Packin 알고리즘의 가치계산방식에 마감일을 추가하여 Constraint 있는 FBP기반 일정 분배 휴리스틱 알고리즘을 적용했다.

## 3. 개발내용
+ 일정자동계산
  + 마감일과 중요도를 포함한 FBP기반 일정분배 알고리즘에 따라 일정을 자동계산
  + 유저가 정한 요일별 작업시간을 기준으로 배치
  + 마감일까지 작업을 마치기에 시간이 모자랄 때 경고 및 `손실중요도`에 따라 일부작업 포기
  + 큰 작업은 작은 작업으로 분할 배치
  + `손실중요도` : 작업을 얼마나 못했는지를 나타내는 척도

+ 주간프리셋
  + 유저의 요일별 작업 가능 시간을 설정
  + 상황에 따라 프리셋을 변경, 일정에 반영

+ 푸시알림
  + 작업별로 지정된 시간에 푸시알림을 보냄
  + 아침에 그날 해야 할 작업 리스트를 알림

+ 위치 기반 작업
  + 작업에 필요한 특정 위치를 지정 가능
  + 상호명이나 업종 등으로 지정 가능
  + 장소와의 남은거치, 길찾기 제공

## 4. Front-End
### 개발환경
이 프로젝트는 아래의 환경에서 작성되었습니다.
- `node.js` : v14.5.0
- `npm` : 6.14.5

위 두 프로그램은 프로젝트를 시작하기 위하여 미리 설치하여야 합니다.

### 시작하기
처음 이 프로젝트를 clone 받았다면 node 의존성이 설치되지 않았기에 프로젝트를 실행할 수 없습니다.
먼저 프로젝트 루트에서 아래의 명령어를 입력하여 의존성을 설치해야 합니다.
```
npm install
```
이 명령어는 npm에서 사용된 라이브러리들을 자동으로 받아옵니다.

### 빌드

#### 개발 버전 실행
```
npm run serve
```
이 명령어는 `http://localhost:8080`에 개발 모드로 프로젝트를 빌드합니다.
개발 모드에서는 service worker가 작동하지 않습니다. PWA를 확인하려면 제품 모드로 빌드하십시오.

#### 제품용 빌드
```
npm run build
```
빌드된 정적파일은 `/dist`에 있습니다.

#### 코드 스타일 체크
```
npm run lint
```
코드 스타일을 체크합니다.

### GUI로 작업하기
vue cli를 3 버전 이상으로 설치하면 gui 툴 `vue ui`를 사용할 수 있습니다.
```
npm install -g @vue/cli
```
vue cli는 global로 설치해야 합니다.

vue cli가 설치되었다면 아래의 명령어로 vue ui를 실행할 수 있습니다.
```
vue ui
```
GUI 프로그램은 `http://localhost:8000`에서 실행됩니다.

### 이 프로젝트에서 이용 가능한 플러그인
아래의 플러그인이 설치되어 있습니다.
- vue-router
- vuex
- vuetify
- pwa
- axios

## 5. Back-End
### 개발환경
이 프로젝트는 아래의 환경에서 작성되었습니다.
- `spring` : v5.2.9
- `Spring-tool-suit` : 4.0.0

위 두 프로그램은 프로젝트를 시작하기 위하여 미리 설치하여야 합니다.

### 시작하기
처음 이 프로젝트를 clone 받았다면 의존성이 설치되어있지 않기 때문에 `Spring-tool-suit`에서 `maven install`을 해주어야 합니다.

### 빌드

#### 개발 버전 실행
```
maven build
```
이것은 개발 모드로 프로젝트를 빌드합니다.
개발 모드에서는 서비스가 작동하지 않습니다. 실제로 구동하려면 spring boot app을 이용해주십시오.

#### 테스트 버전 실행
```
maven test
```
테스트 어노테이션이 붙은 메소드들을 동작시킵니다. 서비스가 실제로 작동하는것이 아닌 테스트만 돌아갑니다.
테스트가 몇개가 있고 몇개가 성공했는지 알려줍니다.

#### 제품용 빌드
```
spring boot app
```
실제로 서비스를 작동시킵니다.
back-end의 코드만 돌아가므로 ui는 존재하지 않습니다.

### 이 프로젝트에서 이용 가능한 패키지
아래의 패키지가 설치되어 있습니다.
- hibernate
- mysql
- json parser
- javax email

## 6. 소스파일 설명
### Front-End
+ `src/assets` : app에 사용되는 파일들을 주로 이미지들을 모아 놓았습니다.
+ `src/components` : app에 사용되는 컴포넌트를 모아 놓았습니다.
+ `src/plugins` : back-end와 통신을 할 때 쓰이는 url을 지정하거나 에러창을 띄워주는 등의 역할을 하는 js파일들을 모아두었습니다.
+ `src/router` : 각 페이지의 path와 name을 지정해주는 역할을 합니다.
+ `src/store` : 사용하는 정보 중 로컬 저장소에 정보를 저장하는데 쓰입니다.
+ `src/views` : 사용자에게 보여주는 화면을 나타내는 컴포넌트들이 담겨있습니다.
+ `src/App.vue` : app의 가장 최상위 컴포넌트입니다
+ `src/man.js` : 가장 먼저 실행되는 js 파일입니다. Vue 인스턴스를 생성하는 역할을 합니다.

### Back-End
+ `src/main/java/com/yachugack/topla` : app을 시작하는 main이 있습니다.
+ `src/main/java/com/yachugack/topla/Controller` : front-end와 back-end간 통신을 중계해줍니다.
+ `src/main/java/com/yachugack/topla/dataformat` : back-end에서 정의한 data형식이 들어있습니다.
+ `src/main/java/com/yachugack/topla/entity` : DataBase의 정보를 끌어오기 위해 hibernate로 정의한 entity가 들어있습니다.
+ `src/main/java/com/yachugack/topla/exception` : 서비스에서 발생하는 예외를 처리하기 위해 직접 정의한 처리구문들이 들어있습니다.
+ `src/main/java/com/yachugack/topla/plan` : 서비스에서 plan을 계산하기 위한 알고리즘이 들어가있습니다.
+ `src/main/java/com/yachugack/topla/remind` : 서비스에서 remind기능을 구현하기 위한 코드가 들어있습니다.
+ `src/main/java/com/yachugack/topla/repository` : 데이터베이스에서 정보를 찾기 위한 쿼리문이 들어있습니다.
+ `src/main/java/com/yachugack/topla/request` : front-end에서 데이터를 받기 위한 양식이 들어있습니다.
+ `src/main/java/com/yachugack/topla/response` : front-end로 데이터를 보내기 위한 양식이 들어있습니다.
+ `src/main/java/com/yachugack/topla/service` : back-end에서 실제로 서비스를 위한 코드들이 담겨져있습니다.
+ `src/main/java/com/yachugack/topla/util` : 서비스 전반에 걸쳐 필요한 util들이 들어있습니다.
+ `src/test/java/com/yachugak/topla` : TDD기반의 프로젝트이므로 테스트를 위한 코드들이 있습니다.

## 7. git-hub
```
git-hub 주소 : https://github.com/yachugak/TOPLA
```