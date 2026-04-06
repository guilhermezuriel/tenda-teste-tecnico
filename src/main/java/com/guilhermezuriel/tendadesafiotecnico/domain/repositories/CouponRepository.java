package com.guilhermezuriel.tendadesafiotecnico.domain.repositories;

import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.CouponCode;

import java.util.Optional;

public interface CouponRepository {

    void save(Coupon coupon);

    Optional<Coupon> findByCode(CouponCode code);

    Optional<Coupon> findById(String id);

    Optional<Coupon> findByIdAndStatusNot(String id, CouponStatus status);

    Optional<Coupon> findByCodeAndStatusNot(CouponCode couponCode, CouponStatus status);
}
