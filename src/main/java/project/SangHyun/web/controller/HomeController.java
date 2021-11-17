package project.SangHyun.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.SangHyun.domain.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @ApiOperation(value = "테스트 페이지", notes = "인증을 위한 테스트 페이지입니다.")
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

}
