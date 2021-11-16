package project.SangHyun.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.SangHyun.domain.auth.AuthCode;
import project.SangHyun.domain.dto.MemberLoginResponseDto;
import project.SangHyun.domain.dto.MemberRegisterResponseDto;
import project.SangHyun.domain.result.SingleResult;
import project.SangHyun.domain.service.ProviderService;
import project.SangHyun.domain.service.ResponseService;
import project.SangHyun.domain.service.MemberService;
import project.SangHyun.web.dto.MemberLoginRequestDto;
import project.SangHyun.web.dto.MemberRegisterRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
public class SignController {

    private final MemberService memberService;
    private final ResponseService responseService;

    @PostMapping("/register")
    public SingleResult<MemberRegisterResponseDto> register(@RequestBody MemberRegisterRequestDto requestDto) {
        MemberRegisterResponseDto responseDto = memberService.registerMember(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    @PostMapping("/login/{provider}")
    public SingleResult<MemberLoginResponseDto> loginByKakao(@RequestBody AuthCode authCode, @PathVariable String provider) {
        MemberLoginResponseDto responseDto = memberService.loginMemberByProvider(authCode.getCode(), provider);
        return responseService.getSingleResult(responseDto);
    }

    @PostMapping("/login")
    public SingleResult<MemberLoginResponseDto> login(@RequestBody MemberLoginRequestDto requestDto) {
        MemberLoginResponseDto responseDto = memberService.loginMember(requestDto);
        return responseService.getSingleResult(responseDto);
    }
}
