package com.kosa.fillinv.member.service;

import com.kosa.fillinv.member.dto.SignUpDto;
import com.kosa.fillinv.member.entity.Member;
import com.kosa.fillinv.member.entity.Profile;
import com.kosa.fillinv.member.repository.MemberRepository;
import com.kosa.fillinv.member.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        validateDuplicateEmail(signUpDto.getEmail());
        validateDuplicateNickname(signUpDto.getNickname());
        validateDuplicatePhoneNum(signUpDto.getPhoneNum());

        Member member = createMember(signUpDto);
        member = memberRepository.save(member);

        Profile profile = createProfile(member);
        profileRepository.save(profile); // 회원가입 시 프로필도 함께 생성
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            log.warn("중복된 이메일 입니다: {}", email);
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
    }

    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            log.warn("중복된 닉네임 입니다: {}", nickname);
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
    }

    private void validateDuplicatePhoneNum(String phoneNum) {
        if (memberRepository.existsByPhoneNum(phoneNum)) {
            log.warn("중복된 전화번호 입니다: {}", phoneNum);
            throw new IllegalArgumentException("이미 사용중인 전화번호입니다.");
        }
    }

    private Member createMember(SignUpDto signUpDto) {
        return Member.builder()
                .id(UUID.randomUUID().toString())
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .nickname(signUpDto.getNickname())
                .phoneNum(signUpDto.getPhoneNum())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Profile createProfile(Member member) {
        return Profile.builder()
                .member(member)
                .introduce("안녕하세요! " + member.getNickname() + "입니다.")
                .createdAt(LocalDateTime.now())
                .categoryId(1L)
                .build();
    }
}
