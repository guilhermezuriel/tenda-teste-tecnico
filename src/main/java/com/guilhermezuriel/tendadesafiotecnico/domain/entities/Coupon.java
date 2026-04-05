package com.guilhermezuriel.tendadesafiotecnico.domain.entities;

import com.guilhermezuriel.tendadesafiotecnico._shared.errors.ValidationError;
import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponInput;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.CouponCode;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DateRange;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DiscountAmount;


public class Coupon {

    private CouponCode code;
    private String description;
    private DiscountAmount discountValue;
    private DateRange expirationDate;

    private Coupon(
            CouponCode code,
            String description,
            DiscountAmount discountValue,
            DateRange expirationDate
    ){
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
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

        return Result.ok(new Coupon(
                couponResult.getValue(),
                input.description(),
                discountResult.getValue(),
                dateRangeResult.getValue()
        ));
    }



}
