package com.guilhermezuriel.tendadesafiotecnico.application.errors;

public class CouponAlreadyDeletedError implements ApplicationError {

    private final String id;

    public CouponAlreadyDeletedError(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Cupom com ID " + id + " já foi deletado";
    }

    @Override
    public String getCode() {
        return "COUPON_ALREADY_DELETED";
    }
}
