package com.kosa.fillinv.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    public static <T> MemberApiResponse<T> ok(T data) {
        return new MemberApiResponse<>(true, data, null);
    }

    public static <T> MemberApiResponse<T> success(String message) {
        return new MemberApiResponse<>(true, null, message);
    }

    public static <T> MemberApiResponse<T> fail(String message) {
        return new MemberApiResponse<>(false, null, message);
    }
}
