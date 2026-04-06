package com.guilhermezuriel.tendadesafiotecnico.domain.entities;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponInput;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;
import com.guilhermezuriel.tendadesafiotecnico.domain.errors.DomainError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Coupon")
public class CouponTest {

    private final LocalDateTime FUTURE_DATE = LocalDateTime.now().plusDays(30);

    private CreateCouponInput validInput() {
        return new CreateCouponInput("SAVE20", "Desconto de verão", new BigDecimal("20.00"), FUTURE_DATE, false);
    }

    @Test
    @DisplayName("Cupom deve ser criado com estado inicial correto")
    void shouldCreateCouponWithCorrectInitialState() {
        Result<Coupon, ?> result = Coupon.create(validInput());

        assertTrue(result.isSuccess());
        Coupon coupon = result.getValue();
        assertNotNull(coupon.getId());
        assertFalse(coupon.getId().isBlank());
        assertEquals("SAVE20", coupon.getCode().getCode());
        assertEquals(CouponStatus.ACTIVE, coupon.getStatus());
        assertFalse(coupon.isRedeemed());
    }

    @Test
    @DisplayName("Cupom deve respeitar o campo published informado")
    void shouldRespectPublishedField() {
        var input = new CreateCouponInput("SAVE20", "Desconto", new BigDecimal("10.00"), FUTURE_DATE, true);
        Coupon coupon = Coupon.create(input).getValue();
        assertTrue(coupon.isPublished());
    }

    @Test
    @DisplayName("Cupom não deve ser criado com código inválido")
    void shouldNotCreateCouponWithInvalidCode() {
        var input = new CreateCouponInput("TOOLONGCODE", "Desconto", new BigDecimal("10.00"), FUTURE_DATE, false);
        Result<Coupon, ?> result = Coupon.create(input);
        assertTrue(result.isFailure());
    }

    @Test
    @DisplayName("Cupom não deve ser criado com desconto abaixo do mínimo")
    void shouldNotCreateCouponWithInvalidDiscount() {
        var input = new CreateCouponInput("SAVE20", "Desconto", new BigDecimal("0.10"), FUTURE_DATE, false);
        Result<Coupon, ?> result = Coupon.create(input);
        assertTrue(result.isFailure());
    }

    @Test
    @DisplayName("Cupom não deve ser criado com desconto nulo")
    void shouldNotCreateCouponWithNullDiscount() {
        var input = new CreateCouponInput("SAVE20", "Desconto", null, FUTURE_DATE, false);
        Result<Coupon, ?> result = Coupon.create(input);
        assertTrue(result.isFailure());
    }

    @Test
    @DisplayName("Cupom não deve ser criado com data de expiração no passado")
    void shouldNotCreateCouponWithPastExpirationDate() {
        var input = new CreateCouponInput("SAVE20", "Desconto", new BigDecimal("10.00"), LocalDateTime.now().minusDays(1), false);
        Result<Coupon, ?> result = Coupon.create(input);
        assertTrue(result.isFailure());
    }

    @Test
    @DisplayName("Cupom não deve ser criado com data de expiração nula")
    void shouldNotCreateCouponWithNullExpirationDate() {
        var input = new CreateCouponInput("SAVE20", "Desconto", new BigDecimal("10.00"), null, false);
        Result<Coupon, ?> result = Coupon.create(input);
        assertTrue(result.isFailure());
    }

    @Test
    @DisplayName("Cupom não deve ser criado com descrição em branco")
    void shouldNotCreateCouponWithBlankDescription() {
        var input = new CreateCouponInput("SAVE20", "  ", new BigDecimal("10.00"), FUTURE_DATE, false);
        Result<Coupon, ?> result = Coupon.create(input);
        assertTrue(result.isFailure());
    }

    @Test
    @DisplayName("Cupom não deve ser criado com descrição nula")
    void shouldNotCreateCouponWithNullDescription() {
        var input = new CreateCouponInput("SAVE20", null, new BigDecimal("10.00"), FUTURE_DATE, false);
        Result<Coupon, ?> result = Coupon.create(input);
        assertTrue(result.isFailure());
    }

    @Test
    @DisplayName("Cupom deve ser marcado como deletado com sucesso")
    void shouldMarkCouponAsDeletedSuccessfully() {
        Coupon coupon = Coupon.create(validInput()).getValue();

        Result<Void, DomainError> result = coupon.markAsDeleted();

        assertTrue(result.isSuccess());
        assertEquals(CouponStatus.DELETED, coupon.getStatus());
    }

    @Test
    @DisplayName("Cupom já deletado não deve ser deletado novamente")
    void shouldFailMarkingAlreadyDeletedCoupon() {
        Coupon coupon = Coupon.create(validInput()).getValue();
        coupon.markAsDeleted();

        Result<Void, DomainError> result = coupon.markAsDeleted();

        assertTrue(result.isFailure());
        assertEquals("COUPON_ALREADY_DELETED", result.getError().getCode());
    }
}
