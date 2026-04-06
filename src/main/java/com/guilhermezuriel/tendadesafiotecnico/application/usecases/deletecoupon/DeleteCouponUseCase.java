package com.guilhermezuriel.tendadesafiotecnico.application.usecases.deletecoupon;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.ApplicationError;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.CouponAlreadyDeletedError;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.CouponNotFoundError;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;
import com.guilhermezuriel.tendadesafiotecnico.domain.errors.DomainError;
import com.guilhermezuriel.tendadesafiotecnico.domain.repositories.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteCouponUseCase {

    private final CouponRepository repository;

    public DeleteCouponUseCase(CouponRepository repository) {
        this.repository = repository;
    }

    public Result<Void, ApplicationError> execute(DeleteCouponInput input) {
        Optional<Coupon> possibleCoupon = this.repository.findByIdAndStatusNot(input.getId(), CouponStatus.DELETED);

        if (possibleCoupon.isEmpty()) {
            return Result.fail(new CouponNotFoundError(input.getId()));
        }

        Coupon coupon = possibleCoupon.get();
        Result<Void, DomainError> deleteResult = coupon.markAsDeleted();

        if (deleteResult.isFailure()) {
            return Result.fail(new CouponAlreadyDeletedError(input.getId()));
        }

        this.repository.save(coupon);

        return Result.ok(null);
    }
}
