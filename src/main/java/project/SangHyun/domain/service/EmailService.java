package project.SangHyun.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Async
    public void send(String email, String authToken) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email+"@koreatech.ac.kr");
        smm.setSubject("회원가입 이메일 인증");
        smm.setText("http://localhost:8080/sign/confirm-email?email="+email+"&authToken="+authToken);

        javaMailSender.send(smm);
    }
}
