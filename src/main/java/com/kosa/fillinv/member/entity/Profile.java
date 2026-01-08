package com.kosa.fillinv.member.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class Profile {

    @Id
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    private String image;

    @Column(name = "introduce", nullable = false)
    private String introduce;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    public void updateImage(String image) {
        this.image = image;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateIntroduceAndCategory(String introduce, Long categoryId) {
        this.introduce = introduce;
        this.categoryId = categoryId;
        this.updatedAt = LocalDateTime.now();
    }

    // 회원가입 -> 기본 프로필 생성으로 인한 기본값 설정 메서드 수정 예정
    private static final String INTRODUCTION = "안녕하세요! %s입니다.";
    private static final Long CATEGORY_ID = 1L;

    public static Profile createDefault(Member member) {
        return Profile.builder()
                .member(member)
                .introduce(String.format(INTRODUCTION, member.getNickname()))
                .createdAt(LocalDateTime.now())
                .categoryId(CATEGORY_ID)
                .build();
    }
}
