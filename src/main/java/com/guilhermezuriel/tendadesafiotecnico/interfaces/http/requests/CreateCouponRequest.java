package com.guilhermezuriel.tendadesafiotecnico.interfaces.http.requests;

import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponInput;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateCouponRequest(
        @NotBlank(message = "O código do cupom é obrigatório")
        String code,

        @NotBlank(message = "A descrição do cupom é obrigatória")
        String description,

        @NotNull(message = "O valor de desconto é obrigatório")
        @DecimalMin(value = "0.5", message = "O valor mínimo de desconto é 0.5")
        BigDecimal discountValue,

        @NotNull(message = "A data de expiração é obrigatória")
        @Future(message = "A data de expiração não pode ser no passado")
        LocalDateTime expirationDate,

        boolean published
) {
    public CreateCouponInput toInput() {
        return new CreateCouponInput(code, description, discountValue, expirationDate, published);
    }
}
