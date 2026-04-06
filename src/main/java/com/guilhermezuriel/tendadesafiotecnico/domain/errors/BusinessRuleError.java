package com.guilhermezuriel.tendadesafiotecnico.domain.errors;

public class BusinessRuleError implements DomainError {

    private final String message;
    private final String code;

    public BusinessRuleError(String message, String code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }
}
