package project.SangHyun.config.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import project.SangHyun.advice.exception.MemberNotFoundException;
import project.SangHyun.domain.entity.Member;
import project.SangHyun.domain.repository.MemberRepository;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Tokens token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        try {
            String authToken = token.getAuthToken();
            if (token != null && jwtTokenProvider.validateTokenExceptExpiration(authToken)) {
                Authentication auth = jwtTokenProvider.getAuthentication(authToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Used jwtToken - {}", LocalDateTime.now());
            }
        } catch(ExpiredJwtException ex1) {
            try {
                String authToken = token.getAuthToken();
                String refreshToken = token.getRefreshToken();
                Authentication auth = jwtTokenProvider.getAuthentication(authToken);
                String findRefreshToken = findRefreshToken(auth);
                if (isCorrectRefreshToken(refreshToken, findRefreshToken)) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Used refreshToken - {}", LocalDateTime.now());
                }
            } catch (ExpiredJwtException ex2) {
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isCorrectRefreshToken(String refreshToken, String findRefreshToken) {
        return refreshToken != null && refreshToken.equals(findRefreshToken) && jwtTokenProvider.validateTokenExceptExpiration(refreshToken);
    }

    private String findRefreshToken(Authentication auth) {
        UserDetails principal = (UserDetails) auth.getPrincipal();
        String email = principal.getUsername();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        return member.getRefreshToken();
    }
}
