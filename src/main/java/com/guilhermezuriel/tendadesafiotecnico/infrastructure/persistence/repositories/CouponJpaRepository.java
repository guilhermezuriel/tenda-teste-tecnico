package com.guilhermezuriel.tendadesafiotecnico.infrastructure.persistence.repositories;

import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;
import com.guilhermezuriel.tendadesafiotecnico.infrastructure.persistence.entities.CouponJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponJpaRepository extends JpaRepository<CouponJpaEntity, String> {

    Optional<CouponJpaEntity> findByCode(String code);

    Optional<CouponJpaEntity> findByCodeAndStatus(String code, CouponStatus status);

    Optional<CouponJpaEntity> findByIdAndStatusNot(String id, CouponStatus status);
}
