package com.guilhermezuriel.tendadesafiotecnico.application.usecases.deletecoupon;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.domain.errors.ValidationError;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DeleteCouponInput {

    private final String id;

    private DeleteCouponInput(String id) {
        this.id = id;
    }

    public static Result<DeleteCouponInput, ValidationError> create(String id) {
        if (id == null || id.isBlank()) {
            return Result.fail(new ValidationError("ID é obrigatório", "id"));
        }

        try {
            UUID.fromString(id.trim());
            return Result.ok(new DeleteCouponInput(id.trim()));
        } catch (IllegalArgumentException e) {
            return Result.fail(new ValidationError(
                    "ID inválido. Deve ser um UUID válido",
                    "id"
            ));
        }
    }
}