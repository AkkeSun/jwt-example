# JWT Token Auth

-----

## 1. 토큰 발급 절차

- 올바른 사용자 정보를 입력한 경우 Access Token 과 Refresh Token 발급 합니다.
- Refresh Token 캐시를 초기화 합니다.
    - 캐시는 Redis 와 DB 를 사용합니다.
    - Redis : `{username-userAgent::token}` 키로 refresh token 과 사용자 정보를 저장합니다.
    - DB : Redis 이슈 발생시 서킷브레이커가 오픈될 때 사용되는 임시 캐시 입니다.
- AccessToken 과 Refresh Token 을 응답합니다.

<br />

## 2. 인증 절차

- 시큐리티에서 기본으로 사용하는 `UsernamePasswordAuthenticationFilter` 가 실행되기 전에 `JwtAuthenticationFilter` 가
  실행되도록 설정합니다.
- `JwtAuthenticationFilter` 는 헤더로 입력받은 Access Token 의 만료 유무를 확인 합니다.
- 인증토큰이 만료되지 않았다면 토큰 정보로 인증객채 (`Authentication`) 를 생성하여 인증처리 합니다.

<br />

## 3. 인증토큰 갱신

- 권한이 필요한 리소스에 접근할 때 헤더로 AccessToken 을 전송합니다
- 인증토큰이 만료되었다면 클라이언트는 RefreshToken 으로 AccessToken 을 갱신하는 엔드포인트를 요청합니다.
- 입력받은 Refresh Token 이 만료된 토큰인지, Redis 에 저장된 최신화 RefreshToken 토큰과 동일한지 확인합니다.
- 문제가 없다면 AccessToken, RefreshToken 을 재발급 합니다.
- Refresh Token 캐시를 초기화 합니다.

<br />

## 4. 안정적인 서비스를 위한 장치

- Redis 서버에 이슈가 생긴경우 서킷브레이커가 오픈되고 DB 캐시 서버를 이용해서 사용자를 검증합니다.
- RefreshToken 은 User-Agent, username 을 Unique Key 로 하여 저장됩니다.
    - 즉 사용자가 이용하는 디바이스마다 다르게 인증 토큰을 관리합니다
    - 이는 리프레시 토큰이 탈취 당했을 때 탈취당한 토큰으로 인증토큰을 발급받지 못하도록 보호합니다.

<br />

## 5. 참고사항

- 클라이언트 사용 시나리오는 ClientTest 를 참고해주세요