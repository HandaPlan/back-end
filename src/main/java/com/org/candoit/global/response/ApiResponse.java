package com.org.candoit.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final String code;
    private final String message;
    private T data;

    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public ApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(String.valueOf(HttpStatus.OK.value()), "success", data);
    }

    public static <T> ApiResponse<T> successWithoutData() {
        return new ApiResponse<>(String.valueOf(HttpStatus.OK.value()), "success");
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }
}
