package com.guilhermezuriel.tendadesafiotecnico.infrastructure.persistence.repositories;

import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.CouponCode;
import com.guilhermezuriel.tendadesafiotecnico.domain.repositories.CouponRepository;
import com.guilhermezuriel.tendadesafiotecnico.infrastructure.persistence.entities.CouponJpaEntity;
import com.guilhermezuriel.tendadesafiotecnico.infrastructure.persistence.mappers.CouponMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CouponRepositoryImpl implements CouponRepository {


    private final CouponJpaRepository jpaRepository;
    private final CouponMapper mapper;

    public CouponRepositoryImpl(CouponJpaRepository jpaRepository, CouponMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Coupon coupon) {
        CouponJpaEntity entity = mapper.toJpaEntity(coupon);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Coupon> findByCode(CouponCode code) {
        return jpaRepository
                .findByCode(code.getCode())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Coupon> findById(String id) {
        return jpaRepository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Coupon> findByIdAndStatusNot(String id, CouponStatus status) {
        return jpaRepository
                .findByIdAndStatusNot(id, status)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Coupon> findByCodeAndStatusNot(CouponCode couponCode, CouponStatus status) {
        return jpaRepository
                .findByCodeAndStatusNot(couponCode.getCode(), status)
                .map(mapper::toDomain);
    }
}
