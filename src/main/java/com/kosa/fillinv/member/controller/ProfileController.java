package com.kosa.fillinv.member.controller;

import com.kosa.fillinv.member.dto.MemberApiResponse;
import com.kosa.fillinv.member.dto.IntroductionRequestDto;
import com.kosa.fillinv.member.dto.NicknameRequestDto;
import com.kosa.fillinv.member.dto.ProfileImageRequestDto;
import com.kosa.fillinv.member.dto.ProfileResponseDto;
import com.kosa.fillinv.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final MemberService memberService;

    @GetMapping("/me")
    public MemberApiResponse<ProfileResponseDto> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return MemberApiResponse.ok(memberService.getProfile(userDetails.getUsername()));
    }

    @PatchMapping("/me/image")
    public MemberApiResponse<String> updateProfileImage(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ProfileImageRequestDto requestDto) {
        memberService.updateProfileImage(userDetails.getUsername(), requestDto.getImage());
        return MemberApiResponse.ok("프로필 이미지가 수정되었습니다.");
    }

    @PatchMapping("/me/nickname")
    public MemberApiResponse<String> updateNickname(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody NicknameRequestDto requestDto) {
        memberService.updateNickname(userDetails.getUsername(), requestDto.getNickname());
        return MemberApiResponse.ok("닉네임이 수정되었습니다.");
    }

    @PatchMapping("/me/introduction")
    public MemberApiResponse<String> updateIntroduction(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody IntroductionRequestDto requestDto) {
        memberService.updateIntroduction(userDetails.getUsername(), requestDto);
        return MemberApiResponse.ok("자기소개 및 카테고리가 수정되었습니다.");
    }
}
