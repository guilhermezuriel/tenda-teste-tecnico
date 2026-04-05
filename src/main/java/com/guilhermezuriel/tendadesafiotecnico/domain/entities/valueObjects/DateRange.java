package com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects;

import com.guilhermezuriel.tendadesafiotecnico._shared.errors.ValidationError;
import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;

import java.time.LocalDateTime;

public class DateRange {

    private final LocalDateTime expirationDate;

    private DateRange(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public static Result<DateRange, ValidationError> create(LocalDateTime expirationDate){
        if(expirationDate == null){
            return Result.fail(new ValidationError("A data de expiração do cupom é obrigatória"));
        }
        if(expirationDate.isBefore(LocalDateTime.now())){
            return Result.fail(new ValidationError("A data de expiração do cupom não pode ser no passado."));
        }
        return Result.ok(new DateRange(expirationDate));
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}
