# SpringSecurityJWT-Local-OAuth2-EmailAuth-Redis

개발환경은 다음과 같습니다.

- Spring Boot : 2.5.6
- Spring Security : 5.6.0
- Gradle : 7.2
- Mysql : 8.0
- Querydsl : 4.4.0



Spring Security를 통해 로컬 로그인과 소셜 로그인을 구현하였습니다. (구글, 네이버, 카카오)

로그인은 JWT 토큰 방식으로 진행됩니다.

추가적으로 로컬 회원가입 진행시 이메일 인증을 추가하여 이메일 인증시 로그인이 되도록 구현하였습니다.

[SangHyunGil/SpringSecurityJWT-Local-OAuth2-EmailAuth (github.com)](https://github.com/SangHyunGil/SpringSecurityJWT-Local-OAuth2-EmailAuth) 구현 부분을 조금 변경하였습니다.

변경점

1. DB Member 엔티티에서 RefreshToken을 저장하던 것을 삭제하고 Redis에 저장하는 것으로 변경
2. DB EmailAuth 엔티티에서 이메일 인증코드를 저장하던 것을 삭제하고 Redis에 저장하는 것으로 변경

해당 부분에 대한 자세한 부분은 아래 블로그 포스트에 설명해두었습니다.

​                 

## 블로그 포스트

### [Security] Spring JWT 인증 With REST API (1)

[[Security\] Spring JWT 인증 With REST API (1) (tistory.com)](https://gilssang97.tistory.com/56)

​                 

### [Security] Spring JWT 인증 With REST API (2)

[[Security\] Spring JWT 인증 With REST API (2) (tistory.com)](https://gilssang97.tistory.com/57)

​                 

### [Security] Spring JWT 인증 With REST API(OAuth2.0 추가) (3)

[[Security\] Spring JWT 인증 With REST API(OAuth2.0 추가) (3) (tistory.com) ](https://gilssang97.tistory.com/58)

​                     

### [Spring] 이메일 인증 구현

[[Spring\] 이메일 인증 구현 (tistory.com)](https://gilssang97.tistory.com/60)
