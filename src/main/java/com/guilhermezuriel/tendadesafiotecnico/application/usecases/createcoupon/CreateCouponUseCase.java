package com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon;

import com.guilhermezuriel.tendadesafiotecnico.application.errors.ApplicationError;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;
import com.guilhermezuriel.tendadesafiotecnico.domain.errors.ValidationError;
import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.ExistingCouponCodeError;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.InvalidCouponError;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.repositories.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateCouponUseCase {

    private final CouponRepository repository;

    public CreateCouponUseCase(CouponRepository repository) {
        this.repository = repository;
    }

    public Result<CreateCouponOutput, ApplicationError> execute(CreateCouponInput input) {
        Result<Coupon, ValidationError> couponResult = Coupon.create(input);
        if (couponResult.isFailure()) {
            return Result.fail(new InvalidCouponError(couponResult.getError()));
        }

        Coupon coupon = couponResult.getValue();
        Optional<Coupon> existsCurrentCode = this.repository.findByCodeAndStatusNot(coupon.getCode(), CouponStatus.DELETED);

        if (existsCurrentCode.isPresent()) {
            return Result.fail(new ExistingCouponCodeError(coupon.getCode().getCode()));
        }

        this.repository.save(coupon);

        return Result.ok(CreateCouponOutput.from(coupon));
    }
}
