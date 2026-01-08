package com.kosa.fillinv.member.controller;

import com.kosa.fillinv.member.dto.MemberApiResponse;
import com.kosa.fillinv.member.dto.SignUpDto;
import com.kosa.fillinv.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public MemberApiResponse<String> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return MemberApiResponse.ok("회원가입 성공");
    }
}
