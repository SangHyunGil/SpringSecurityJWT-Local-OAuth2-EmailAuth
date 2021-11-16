package project.SangHyun.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.SangHyun.domain.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndProvider(String email, String provider);
}
