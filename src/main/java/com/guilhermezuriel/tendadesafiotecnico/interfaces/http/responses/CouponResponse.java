package com.guilhermezuriel.tendadesafiotecnico.interfaces.http.responses;

import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponOutput;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.findcoupon.FindCouponOutput;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponse(
        String id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDateTime expirationDate,
        CouponStatus status,
        boolean published,
        boolean redeemed
) {
    public static CouponResponse from(CreateCouponOutput output) {
        return new CouponResponse(
                output.id(),
                output.code(),
                output.description(),
                output.discountValue(),
                output.expirationDate(),
                output.status(),
                output.published(),
                output.redeemed()
        );
    }

    public static CouponResponse from(FindCouponOutput output) {
        return new CouponResponse(
                output.id(),
                output.code(),
                output.description(),
                output.discountValue(),
                output.expirationDate(),
                output.status(),
                output.published(),
                output.redeemed()
        );
    }
}
