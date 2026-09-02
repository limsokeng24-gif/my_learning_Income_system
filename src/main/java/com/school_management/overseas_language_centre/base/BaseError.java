package com.school_management.overseas_language_centre.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BaseError<T> {
    private Boolean status;
    private Integer code;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private T errors;

    public static <T> BaseError<T> of(Integer code, String message, T errors) {
        return BaseError.<T>builder()
                .status(false)
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();
    }
}

//ExecptionHandling