package com.kosa.fillinv.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroductionRequestDto {
    private String introduction;
    private Long categoryId;
}
