package project.SangHyun.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.SangHyun.domain.dto.MemberLoginResponseDto;
import project.SangHyun.domain.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

}
