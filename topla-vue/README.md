# topla-vue
TOPLA 프론트엔드 프로젝트입니다.

## 개발환경
이 프로젝트는 아래의 환경에서 작성되었습니다.
- `node.js` : v14.5.0
- `npm` : 6.14.5

위 두 프로그램은 프로젝트를 시작하기 위하여 미리 설치하여야 합니다.

## 시작하기
처음 이 프로젝트를 clone 받았다면 node 의존성이 설치되지 않았기에 프로젝트를 실행할 수 없습니다.
먼저 프로젝트 루트에서 아래의 명령어를 입력하여 의존성을 설치해야 합니다.
```
npm install
```
이 명령어는 npm에서 사용된 라이브러리들을 자동으로 받아옵니다.

## 빌드

### 개발 버전 실행
```
npm run serve
```
이 명령어는 `http://localhost:8080`에 개발 모드로 프로젝트를 빌드합니다.
개발 모드에서는 service worker가 작동하지 않습니다. PWA를 확인하려면 제품 모드로 빌드하십시오.

### 제품용 빌드
```
npm run build
```
빌드된 정적파일은 `/dist`에 있습니다.

### 코드 스타일 체크
```
npm run lint
```
코드 스타일을 체크합니다.

## GUI로 작업하기
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

## 이 프로젝트에서 이용 가능한 플러그인
아래의 플러그인이 설치되어 있습니다.
- vue-router
- vuex
- vuetify
- pwa
- axios