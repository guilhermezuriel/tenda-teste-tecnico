package com.guilhermezuriel.tendadesafiotecnico.domain.valueobjects;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DateRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("DateRange")
public class DateRangeTest {

    @Test
    @DisplayName("Deve ser possível cadastrar um cupom no passado")
    void shouldBeAbleToCreateACoupon(){
        LocalDateTime NOWPLUS5 = LocalDateTime.now().plusMinutes(5);
        Result<DateRange, ?> dateRangeResult = DateRange.create(NOWPLUS5);
        assertTrue(dateRangeResult.isSuccess());
    }

    @Test
    @DisplayName("Não deve ser possível cadastrar um cupom no passado")
    void shouldNotBeAbleToCreateACouponWithNullableExpirationDate(){
        LocalDateTime DATE = null;
        Result<DateRange, ?> dateRangeResult = DateRange.create(DATE);
        assertTrue(dateRangeResult.isFailure());
    }

    @Test
    @DisplayName("Não deve ser possível cadastrar um cupom no passado")
    void shouldNotBeAbleToCreateACouponInThePast(){
        LocalDateTime NOWMINUS5 = LocalDateTime.now().minusMinutes(5);
        Result<DateRange, ?> dateRangeResult = DateRange.create(NOWMINUS5);
        assertTrue(dateRangeResult.isFailure());
    }
}
