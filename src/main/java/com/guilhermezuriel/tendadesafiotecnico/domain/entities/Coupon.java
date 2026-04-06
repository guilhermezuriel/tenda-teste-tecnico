package com.guilhermezuriel.tendadesafiotecnico.domain.entities;

import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;
import com.guilhermezuriel.tendadesafiotecnico.domain.errors.ValidationError;
import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponInput;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.CouponCode;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DateRange;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DiscountAmount;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Coupon {

    private final String id;
    private final CouponCode code;
    private final String description;
    private final DiscountAmount discountValue;
    private final DateRange expirationDate;
    private final CouponStatus status;
    private final boolean published;
    private final boolean redeemed;

    private Coupon(
            String id,
            CouponCode code,
            String description,
            DiscountAmount discountValue,
            DateRange expirationDate,
            CouponStatus status,
            boolean published,
            boolean redeemed
    ){
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = status;
        this.published = published;
        this.redeemed = redeemed;
    }

    public static Result<Coupon, ValidationError> create(CreateCouponInput input){
        Result<CouponCode, ValidationError> couponResult = CouponCode.create(input.code());
        if(couponResult.isFailure()){
            return Result.fail(couponResult.getError());
        }
        Result<DiscountAmount, ValidationError> discountResult = DiscountAmount.create(input.discountValue());
        if(discountResult.isFailure()){
            return Result.fail(discountResult.getError());
        }
        Result<DateRange, ValidationError> dateRangeResult = DateRange.create(input.expirationDate());
        if(dateRangeResult.isFailure()){
            return Result.fail(dateRangeResult.getError());
        }

        if(input.description() == null || input.description().isBlank()){
            return Result.fail(new ValidationError("O campo de descrição do cupom é obrigatória"));
        }

        var status = CouponStatus.ACTIVE;

        return Result.ok(new Coupon(
                UUID.randomUUID().toString(),
                couponResult.getValue(),
                input.description(),
                discountResult.getValue(),
                dateRangeResult.getValue(),
                status,
                input.published(),
                false
        ));
    }



}
