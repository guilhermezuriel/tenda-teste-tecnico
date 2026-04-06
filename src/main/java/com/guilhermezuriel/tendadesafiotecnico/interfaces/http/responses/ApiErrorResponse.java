package com.guilhermezuriel.tendadesafiotecnico.interfaces.http.responses;

import com.guilhermezuriel.tendadesafiotecnico._shared.errors.BaseError;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.ExistingCouponCodeError;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.InvalidCouponError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiErrorResponse {

    private final String code;
    private final String message;

    private ApiErrorResponse(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ResponseEntity<ApiErrorResponse> from(BaseError error) {
        ApiErrorResponse body = ApiErrorResponse.builder()
                .code(error.getCode())
                .message(error.getMessage())
                .build();

        return ResponseEntity.status(resolveStatus(error)).body(body);
    }

    private static HttpStatus resolveStatus(BaseError error) {
        if (error instanceof ExistingCouponCodeError) return HttpStatus.CONFLICT;
        if (error instanceof InvalidCouponError) return HttpStatus.UNPROCESSABLE_ENTITY;
        return switch (error.getCode()) {
            case "COUPON_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "COUPON_ALREADY_DELETED" -> HttpStatus.CONFLICT;
            case "VALIDATION_ERROR" -> HttpStatus.UNPROCESSABLE_ENTITY;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    public static class Builder {
        private String code;
        private String message;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(this);
        }
    }
}
