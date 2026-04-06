package com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon;

import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateCouponOutput(
        String id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDateTime expirationDate,
        CouponStatus status,
        boolean published,
        boolean redeemed
) {
    public static CreateCouponOutput from(Coupon coupon) {
        return new CreateCouponOutput(
                coupon.getId(),
                coupon.getCode().getCode(),
                coupon.getDescription(),
                coupon.getDiscountValue().getAmount(),
                coupon.getExpirationDate().getExpirationDate(),
                coupon.getStatus(),
                coupon.isPublished(),
                coupon.isRedeemed()
        );
    }
}
