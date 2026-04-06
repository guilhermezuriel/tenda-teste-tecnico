package com.guilhermezuriel.tendadesafiotecnico.application.usecases.findcoupon;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.domain.errors.ValidationError;
import lombok.Getter;

import java.util.UUID;

@Getter
public class FindCouponInput {

    private final String id;

    private FindCouponInput(String id) {
        this.id = id;
    }

    public static Result<FindCouponInput, ValidationError> create(String id) {
        if (id == null || id.isBlank()) {
            return Result.fail(new ValidationError("ID é obrigatório", "id"));
        }

        try {
            UUID.fromString(id.trim());
            return Result.ok(new FindCouponInput(id.trim()));
        } catch (IllegalArgumentException e) {
            return Result.fail(new ValidationError(
                    "ID inválido. Deve ser um UUID válido",
                    "id"
            ));
        }
    }
}