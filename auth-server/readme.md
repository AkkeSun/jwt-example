# JWT Auth Server

----

## 1. 토큰 발급 절차

- 올바른 사용자 정보를 입력한 경우 Access Token 과 Refresh Token 발급 합니다.
- Refresh Token 캐시를 초기화 합니다.
    - 캐시는 Redis 와 DB 를 사용합니다.
    - Redis : `{username-userAgent::token}` 키로 refresh token 과 사용자 정보를 저장합니다.
    - DB : Redis 이슈 발생시 서킷브레이커가 오픈될 때 사용되는 임시 캐시 입니다.
- AccessToken 과 Refresh Token 을 응답합니다.

<br />

## 2. 인증토큰 갱신

- 입력받은 Refresh Token 이 만료된 토큰인지, Redis 에 저장된 최신화 RefreshToken 토큰과 동일한지 확인합니다.
- 문제가 없다면 AccessToken, RefreshToken 을 재발급 합니다.
- Refresh Token 캐시를 초기화 합니다.

<br />

## 3. 안정적인 서비스를 위한 장치

- Redis 서버에 이슈가 생긴경우 서킷브레이커가 오픈되고 DB 캐시 서버를 이용해서 사용자를 검증합니다.
- RefreshToken 은 User-Agent, username 을 Unique Key 로 하여 저장됩니다.
    - 즉 사용자가 이용하는 디바이스 마다 다르게 인증 토큰을 관리합니다.
    - 이는 RefreshToken 이 탈취 당했을 때 탈취당한 토큰으로 인증토큰을 발급받지 못하도록 보호합니다.