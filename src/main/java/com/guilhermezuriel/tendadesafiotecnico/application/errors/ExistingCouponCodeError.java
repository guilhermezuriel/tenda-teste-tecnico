package com.guilhermezuriel.tendadesafiotecnico.application.errors;

public class ExistingCouponCodeError implements ApplicationError {

    private final String code;

    public ExistingCouponCodeError(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return "Já existe um cupom com o código: " + code;
    }

    @Override
    public String getCode() {
        return "EXISTING_COUPON_CODE";
    }
}
