# JWT Resource Server

-----
> - 시큐리티에서 기본으로 사용하는 `UsernamePasswordAuthenticationFilter` 가 실행되기 전에 `JwtAuthenticationFilter` 가
    실행되도록 설정합니다.
> - `JwtAuthenticationFilter` 는 헤더로 입력받은 Access Token 의 만료 유무를 확인 합니다.

<br />

### 인증 절차

- 접근 권한이 필요한 리소스에 접근시 사용자는 인증토큰을 헤더에 추가하여 요청합니다.
- 헤더로 입력받은 Access Token 의 만료 유무를 확인 합니다.
- 인증토큰이 만료되지 않았다면 토큰 정보로 인증객채 (`Authentication`) 를 생성하여 인증처리 합니다.
- 사용자가 해당 리소스에 접근 가능한 권한을 가졌다면 리소스를 응답합니다.

<br />