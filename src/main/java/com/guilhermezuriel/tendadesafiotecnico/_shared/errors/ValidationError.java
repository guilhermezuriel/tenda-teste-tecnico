package com.guilhermezuriel.tendadesafiotecnico._shared.errors;

public class ValidationError implements DomainError{

    private final String message;
    private final String field;


    public ValidationError(String message){
        this(message, null);
    }

    public ValidationError(String message, String field){
        this.message = message;
        this.field = field;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return "VALIDATION_ERROR";
    }

    public String getField(){
        return field;
    }
}
