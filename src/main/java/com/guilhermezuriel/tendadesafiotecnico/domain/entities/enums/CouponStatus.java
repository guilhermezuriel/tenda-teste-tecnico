package com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.domain.errors.ValidationError;

public enum CouponStatus {
    ACTIVE,
    INACTIVE,
    DELETED;

    public static Result<CouponStatus, ValidationError> fromString(String value) {
        if (value == null || value.isBlank()) {
            return Result.fail(new ValidationError(
                    "Status é obrigatório",
                    "status"
            ));
        }

        for (CouponStatus status : values()) {
            if (status.name().equalsIgnoreCase(value.trim())) {
                return Result.ok(status);
            }
        }

        return Result.fail(new ValidationError(
                "Status inválido. Valores permitidos: ACTIVE, INACTIVE, DELETED",
                "status"
        ));
    }
}