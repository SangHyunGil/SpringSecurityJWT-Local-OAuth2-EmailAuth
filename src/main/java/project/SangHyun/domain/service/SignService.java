package project.SangHyun.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.SangHyun.advice.exception.InvalidRefreshTokenException;
import project.SangHyun.advice.exception.LoginFailureException;
import project.SangHyun.advice.exception.MemberEmailAlreadyExistsException;
import project.SangHyun.advice.exception.MemberNotFoundException;
import project.SangHyun.config.security.jwt.JwtTokenProvider;
import project.SangHyun.domain.auth.AccessToken;
import project.SangHyun.domain.auth.Profile.ProfileDto;
import project.SangHyun.domain.dto.MemberLoginResponseDto;
import project.SangHyun.domain.dto.MemberRegisterResponseDto;
import project.SangHyun.domain.dto.TokenResponseDto;
import project.SangHyun.domain.entity.Member;
import project.SangHyun.domain.repository.MemberRepository;
import project.SangHyun.web.dto.MemberLoginRequestDto;
import project.SangHyun.web.dto.MemberRegisterRequestDto;
import project.SangHyun.web.dto.TokenRequestDto;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProviderService providerService;

    /**
     * Dto로 들어온 값을 통해 회원가입을 진행
     * @param requestDto
     * @return
     */
    @Transactional
    public MemberRegisterResponseDto registerMember(MemberRegisterRequestDto requestDto) {
        validateDuplicated(requestDto.getEmail());
        Member member = memberRepository.save(
                Member.builder()
                        .email(requestDto.getEmail())
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .provider(null)
                        .build());

        return MemberRegisterResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .build();
    }

    /**
     * Unique한 값을 가져야하나, 중복된 값을 가질 경우를 검증
     * @param email
     */
    public void validateDuplicated(String email) {
        if (memberRepository.findByEmail(email).isPresent())
            throw new MemberEmailAlreadyExistsException();
    }

    @Transactional
    public MemberLoginResponseDto loginMember(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(LoginFailureException::new);
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword()))
            throw new LoginFailureException();
        member.updateRefreshToken(jwtTokenProvider.createRefreshToken());
        return new MemberLoginResponseDto(member.getId(), jwtTokenProvider.createToken(requestDto.getEmail()), member.getRefreshToken());
    }

    /**
     * 소셜 로그인 구현
     * @param code
     * @param provider
     * @return
     */
    @Transactional
    public MemberLoginResponseDto loginMemberByProvider(String code, String provider) {
        AccessToken accessToken = providerService.getAccessToken(code, provider);
        ProfileDto profile = providerService.getProfile(accessToken.getAccess_token(), provider);

        Optional<Member> findMember = memberRepository.findByEmailAndProvider(profile.getEmail(), provider);
        if (findMember.isPresent()) {
            Member member = findMember.get();
            member.updateRefreshToken(jwtTokenProvider.createRefreshToken());
            return new MemberLoginResponseDto(member.getId(), jwtTokenProvider.createToken(findMember.get().getEmail()), member.getRefreshToken());
        } else {
            Member saveMember = saveMember(profile, provider);
            saveMember.updateRefreshToken(jwtTokenProvider.createRefreshToken());
            return new MemberLoginResponseDto(saveMember.getId(), jwtTokenProvider.createToken(saveMember.getEmail()), saveMember.getRefreshToken());
        }
    }

    private Member saveMember(ProfileDto profile, String provider) {
        Member member = Member.builder()
                .email(profile.getEmail())
                .password(null)
                .provider(provider)
                .build();
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }

    /**
     * 토큰 재발행
     * @param requestDto
     * @return
     */
    @Transactional
    public TokenResponseDto reIssue(TokenRequestDto requestDto) {
        if (!jwtTokenProvider.validateTokenExpiration(requestDto.getRefreshToken()))
            throw new InvalidRefreshTokenException();

        Member member = findMemberByToken(requestDto);

        if (!member.getRefreshToken().equals(requestDto.getRefreshToken()))
            throw new InvalidRefreshTokenException();

        String accessToken = jwtTokenProvider.createToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        member.updateRefreshToken(refreshToken);
        return new TokenResponseDto(accessToken, refreshToken);
    }

    public Member findMemberByToken(TokenRequestDto requestDto) {
        Authentication auth = jwtTokenProvider.getAuthentication(requestDto.getAccessToken());
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findByEmail(username).orElseThrow(MemberNotFoundException::new);
    }
}
