package project.SangHyun.config.security.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.SangHyun.advice.exception.MemberNotFoundException;
import project.SangHyun.domain.entity.Member;
import project.SangHyun.domain.repository.MemberRepository;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        return MemberDetails.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(member.getRoles().stream()
                                        .map(auth -> new SimpleGrantedAuthority(auth.toString()))
                                        .collect(Collectors.toList()))
                .build();
    }
}
