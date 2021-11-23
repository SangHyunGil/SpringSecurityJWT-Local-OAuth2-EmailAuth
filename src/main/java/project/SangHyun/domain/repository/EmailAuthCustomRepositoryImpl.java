package project.SangHyun.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import project.SangHyun.domain.entity.EmailAuth;
import project.SangHyun.domain.entity.QEmailAuth;


import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class EmailAuthCustomRepositoryImpl implements EmailAuthCustomRepository{

    JPAQueryFactory jpaQueryFactory;

    public EmailAuthCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime) {
        log.info("{}, {}, {}", email, authToken, currentTime);
        EmailAuth emailAuth = jpaQueryFactory
                .selectFrom(QEmailAuth.emailAuth)
                .where(QEmailAuth.emailAuth.email.eq(email),
                        QEmailAuth.emailAuth.authToken.eq(authToken),
                        QEmailAuth.emailAuth.expireDate.goe(currentTime),
                        QEmailAuth.emailAuth.expired.eq(false))
                .fetchFirst();

        return Optional.ofNullable(emailAuth);
    }
}
