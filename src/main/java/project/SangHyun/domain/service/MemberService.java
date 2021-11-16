package project.SangHyun.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.SangHyun.advice.exception.LoginFailureException;
import project.SangHyun.advice.exception.MemberEmailAlreadyExistsException;
import project.SangHyun.config.security.jwt.JwtTokenProvider;
import project.SangHyun.domain.auth.AccessToken;
import project.SangHyun.domain.auth.Profile.Profile;
import project.SangHyun.domain.dto.MemberLoginResponseDto;
import project.SangHyun.domain.dto.MemberRegisterResponseDto;
import project.SangHyun.domain.entity.Member;
import project.SangHyun.domain.repository.MemberRepository;
import project.SangHyun.web.dto.MemberLoginRequestDto;
import project.SangHyun.web.dto.MemberRegisterRequestDto;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

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

    public MemberLoginResponseDto loginMember(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(LoginFailureException::new);
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword()))
            throw new LoginFailureException();
        return new MemberLoginResponseDto(member.getId(), jwtTokenProvider.createToken(requestDto.getEmail()));
    }

    @Transactional
    public MemberLoginResponseDto loginMemberByProvider(String code, String provider) {
        AccessToken accessToken = providerService.getAccessToken(code, provider);
        Profile profile = providerService.getProfile(accessToken.getAccess_token(), provider);

        Optional<Member> findMember = memberRepository.findByEmailAndProvider(profile.getEmail(), provider);
        if (findMember.isPresent()) {
            return new MemberLoginResponseDto(findMember.get().getId(), jwtTokenProvider.createToken(findMember.get().getEmail()));
        } else {
            Member saveMember = saveMember(profile, provider);
            return new MemberLoginResponseDto(saveMember.getId(), jwtTokenProvider.createToken(saveMember.getEmail()));
        }
    }

    private Member saveMember(Profile profile, String provider) {
        Member member = Member.builder()
                .email(profile.getEmail())
                .password(null)
                .provider(provider)
                .build();
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }
}
