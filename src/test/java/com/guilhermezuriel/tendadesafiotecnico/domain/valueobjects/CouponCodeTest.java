package com.guilhermezuriel.tendadesafiotecnico.domain.valueobjects;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.CouponCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CouponCode")
public class CouponCodeTest {

    @Test
    @DisplayName("Código deve ser criado com suceso")
    void shouldCreateCouponSucessfully(){
        Result<CouponCode, ?> result = CouponCode.create("SAVE20");
        assertTrue(result.isSuccess());
        assertEquals("SAVE20", result.getValue().getCode());
    }

    @Test
    @DisplayName("Código não pode ser criado tendo mais que 6 caracteres")
    void shouldNotCreateCouponHavingMoreThan6Characters(){
        String MORETHAN6CODE = "SAVER60";
        Result<CouponCode, ?> result = CouponCode.create(MORETHAN6CODE);
        assertTrue(result.isFailure());
    }

    @Test
    @DisplayName("Código não pode ser criado tendo menos que 6 caracteres")
    void shouldNotCreateCouponHavingLessThan6Characters(){
        String LESSTHAN6CODE = "SAV60";
        Result<CouponCode, ?> result = CouponCode.create(LESSTHAN6CODE);
        assertTrue(result.isFailure());
    }

    @Test
    @DisplayName("Código com caracteres especiais deve ser limpado")
    void shouldCleanAndTreatCodeWithSpecialCharacters(){
        Result<CouponCode, ?> result = CouponCode.create("prom!10.");
        assertTrue(result.isSuccess());
        assertEquals("PROM10", result.getValue().getCode());
    }
}
