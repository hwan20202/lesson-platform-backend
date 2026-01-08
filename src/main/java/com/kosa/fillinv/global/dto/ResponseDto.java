package com.kosa.fillinv.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private String message;

    public static <T> ResponseDto<T> ok(T data) {
        return new ResponseDto<>(true, data, null);
    }

    public static <T> ResponseDto<T> success(String message) {
        return new ResponseDto<>(true, null, message);
    }

    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>(false, null, message);
    }
}
