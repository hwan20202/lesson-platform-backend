package com.kosa.fillinv.member.dto;

import com.kosa.fillinv.category.dto.CategoryResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseDto {
    private String imageUrl;
    private String nickname;
    private String email;
    private String phoneNum;
    private String introduction;
    private CategoryResponseDto category;
}
