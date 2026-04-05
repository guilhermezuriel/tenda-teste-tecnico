package com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects;

import com.guilhermezuriel.tendadesafiotecnico._shared.errors.ValidationError;
import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;

public class CouponCode {

    private final String code;

    private CouponCode(String code){
        this.code = code;
    }

    public static Result<CouponCode, ValidationError> create(String code){
        var sanitized = sanitize(code);
        if(sanitized.length() != 6){
            Result.fail(new ValidationError("O código deve conter 6 caracteres."));
        }
        return Result.ok(new CouponCode(code));
    }

    private static String sanitize(String code){
        return code.trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    public String getCode(){
        return code;
    }

}
