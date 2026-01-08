package com.kosa.fillinv.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IntroductionRequestDto {
    private String introduction;
    private Long categoryId;
}
