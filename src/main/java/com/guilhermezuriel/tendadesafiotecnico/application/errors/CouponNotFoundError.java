package com.guilhermezuriel.tendadesafiotecnico.application.errors;

public class CouponNotFoundError implements ApplicationError {

    private final String id;

    public CouponNotFoundError(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Cupom não encontrado com ID: " + id;
    }

    @Override
    public String getCode() {
        return "COUPON_NOT_FOUND";
    }
}
