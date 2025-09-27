package com.org.candoit.global.response;

import java.util.Arrays;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = {RestController.class}, basePackages = {"com.org.candoit.domain"})
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMissingParam(MissingServletRequestParameterException ex) {
        String msg = String.format("'%s' 파라미터가 필요합니다.", ex.getParameterName());
        return new ApiResponse<>("400", msg, null);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Class<?> req = e.getRequiredType();

        if (req != null && req.isEnum()) {
            List<String> allowed = Arrays.stream(req.getEnumConstants())
                .map(en -> ((Enum<?>) en).name().toLowerCase())
                .toList();

            String msg = String.format("'%s' 파라미터는 허용 값 중 하나를 사용하세요. [%s] (입력값: %s)",
                e.getName(), String.join(", ", allowed), e.getValue());

            ApiResponse<?> body = new ApiResponse<>("400", msg, null);
            return ResponseEntity.badRequest().body(body);
        }

        ApiResponse<?> body = new ApiResponse<>("400", "올바른 파라미터를 입력하세요.", null);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ApiResponse.fail(errorCode));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException e) {
        ErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ApiResponse.fail(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = GlobalErrorCode.BAD_REQUEST;
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ApiResponse.fail(errorCode));
    }
}
