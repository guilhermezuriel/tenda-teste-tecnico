package com.guilhermezuriel.tendadesafiotecnico.application.errors;

import com.guilhermezuriel.tendadesafiotecnico.domain.errors.DomainError;
import com.guilhermezuriel.tendadesafiotecnico.domain.errors.ValidationError;

public class InvalidCouponError implements ApplicationError {

    private final DomainError cause;

    public InvalidCouponError(DomainError cause) {
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return cause.getMessage();
    }

    @Override
    public String getCode() {
        return "INVALID_COUPON";
    }

    public DomainError getCause() {
        return cause;
    }
}
