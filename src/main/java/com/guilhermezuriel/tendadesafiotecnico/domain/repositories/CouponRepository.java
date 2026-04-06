package com.guilhermezuriel.tendadesafiotecnico.domain.repositories;

import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.CouponCode;

import java.util.Optional;

public interface CouponRepository {

    void save(Coupon coupon);

    void delete(Coupon coupon);

    Optional<Coupon> findByCode(CouponCode code);
}
