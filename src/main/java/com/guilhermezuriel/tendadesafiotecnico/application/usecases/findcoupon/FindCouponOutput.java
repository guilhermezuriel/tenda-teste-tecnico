package com.guilhermezuriel.tendadesafiotecnico.application.usecases.findcoupon;

import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponOutput;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FindCouponOutput(
        String id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDateTime expirationDate,
        CouponStatus status,
        boolean published,
        boolean redeemed
) {

    public static FindCouponOutput from(Coupon coupon) {
        return new FindCouponOutput(
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
