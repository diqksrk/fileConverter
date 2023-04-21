# Naver Cloud Platform data-teleporter

| Building | Branch | Status |
|---|---|---|
| Pull Request CommitBuilding | All |  |
| DailyBuilding | master |  |

## Specification

## Build Setup

**Requires Node.js 6+**<br>
**node 14 추천**

``` bash
# install dependencies
yarn install # or npm install (yarn 이 추천 됨.)

# serve in dev mode, with hot reload at localhost:8080
npm run dev

# build for production
npm run build

# serve in production mode
npm start

# package
npm run package
```

## Upgrade NCP UI Kit
```

## 설치 가이드
1. 해당 프로젝트는 14.* 버전의 노드와 호환하므로 해당 버전대의 nodejs를 설치합니다.
2. 설치 후, 환경 세팅이 되어 있는지 확인해야 하므로 Command창으로 들어가 node -v 를 통해 nodejs버전을 확인합니다.
3. 이후 node_modules(프로젝트가 참조하는 라이브러리가 위치하는 폴더)를 설치하기 위해, yarn install 명령어를 사용합니다.
4. 설치 도중 sha512관련 에러가 발생합니다. 해당 에러는 yarn.lock에 정의된 패키지 파일을 설치하는 과정중 발생하는 에러입니다.
5. yarn.lock을 참조해서 동일한 패키지 버전을 받기 위해, yarn.lock이 수정되었는지 여부를 판별하는데 그 과정중에서 유효성 검증(integrity-check)을 수행하게 됩니다.
6. 위의 과정속에서 Mismatch가 발생하게 되면, 에러를 발생시키는데 checksum을 통해 검증하므로 해당값을 일치시켜줘야 합니다.
7. yarn install --update-checksums --ignore-platform 해당 명령어를 사용합니다.
8. 이후, 패키지가 설치되고 node_modules 폴더가 제대로 생성되었는지 확인합니다.
9. FrontEnd - BackEnd로 구성되어 있으므로, BackEnd인 internal-cost_detail_dashboard_BE Spring Boot 프로젝트를 실행시킵니다.
10. 이후, 현재 프로젝트로 돌아와 Terminal을 열고 npm run dev 명령어를 실행합니다.
11. 1 ~ 2 분 정도후에 해당 프로젝트가 빌드되고 Browser창을 통해 localhost:8000 로 접속합니다.
