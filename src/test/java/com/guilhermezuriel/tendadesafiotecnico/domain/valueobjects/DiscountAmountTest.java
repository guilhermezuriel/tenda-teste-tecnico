package com.guilhermezuriel.tendadesafiotecnico.domain.valueobjects;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DiscountAmount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DiscountAmount")
public class DiscountAmountTest {

    @Test
    @DisplayName("Desconto deve ser criado com sucesso")
    void shouldCreateDiscountAmountSuccessfully() {
        Result<DiscountAmount, ?> result = DiscountAmount.create(new BigDecimal("10.00"));
        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("10.00"), result.getValue().getAmount());
    }

    @Test
    @DisplayName("Desconto com valor mínimo (0.5) deve ser criado com sucesso")
    void shouldCreateDiscountAmountWithMinimumValue() {
        Result<DiscountAmount, ?> result = DiscountAmount.create(new BigDecimal("0.5"));
        assertTrue(result.isSuccess());
    }

    @Test
    @DisplayName("Desconto não pode ser criado com valor abaixo do mínimo")
    void shouldNotCreateDiscountAmountBelowMinimum() {
        Result<DiscountAmount, ?> result = DiscountAmount.create(new BigDecimal("0.49"));
        assertTrue(result.isFailure());
    }

    @Test
    @DisplayName("Desconto não pode ser criado com valor nulo")
    void shouldNotCreateDiscountAmountWithNullValue() {
        Result<DiscountAmount, ?> result = DiscountAmount.create(null);
        assertTrue(result.isFailure());
    }
}
