package com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects;

import com.guilhermezuriel.tendadesafiotecnico.domain.errors.ValidationError;
import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;

import java.math.BigDecimal;

public class DiscountAmount {

    private static final BigDecimal MINIMUM_VALUE = new BigDecimal("0.5");

    private final BigDecimal amount;

    private DiscountAmount(BigDecimal amount){
        this.amount = amount;
    }

    public static Result<DiscountAmount, ValidationError> create(BigDecimal amount){
        if(amount == null){
            return Result.fail(new ValidationError("O valor de desconto é obrigatório"));
        }
        if(amount.compareTo(MINIMUM_VALUE) < 0){
            return Result.fail(new ValidationError("O valor mínimo de desconto é 0.5"));
        }
        return Result.ok(new DiscountAmount(amount));
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
