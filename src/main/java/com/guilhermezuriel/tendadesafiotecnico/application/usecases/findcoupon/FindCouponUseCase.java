package com.guilhermezuriel.tendadesafiotecnico.application.usecases.findcoupon;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.ApplicationError;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.CouponNotFoundError;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.repositories.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindCouponUseCase {

    private final CouponRepository repository;

    public FindCouponUseCase(CouponRepository repository) {
        this.repository = repository;
    }

    public Result<FindCouponOutput, ApplicationError> execute(FindCouponInput input) {
        Optional<Coupon> possibleCoupon = this.repository.findById(input.getId());

        if (possibleCoupon.isEmpty()) {
            return Result.fail(new CouponNotFoundError(input.getId()));
        }

        return Result.ok(FindCouponOutput.from(possibleCoupon.get()));
    }
}
