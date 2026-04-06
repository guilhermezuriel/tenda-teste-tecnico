package com.guilhermezuriel.tendadesafiotecnico.infrastructure.persistence.mappers;

import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.CouponCode;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DateRange;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DiscountAmount;
import com.guilhermezuriel.tendadesafiotecnico.infrastructure.persistence.entities.CouponJpaEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CouponMapper {

    public CouponJpaEntity toJpaEntity(Coupon coupon) {
        CouponJpaEntity entity = new CouponJpaEntity();
        entity.setId(coupon.getId());
        entity.setCode(coupon.getCode().getCode());
        entity.setDescription(coupon.getDescription());
        entity.setDiscountValue(coupon.getDiscountValue().getAmount());
        entity.setExpirationDate(coupon.getExpirationDate().getExpirationDate());
        entity.setStatus(coupon.getStatus());
        entity.setPublished(coupon.isPublished());
        entity.setRedeemed(coupon.isRedeemed());
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }


    public Coupon toDomain(CouponJpaEntity entity) {
        return Coupon.reconstitute(
                entity.getId(),
                CouponCode.reconstitute(entity.getCode()),
                entity.getDescription(),
                DiscountAmount.reconstitute(entity.getDiscountValue()),
                DateRange.reconstitute(entity.getExpirationDate()),
                entity.getStatus(),
                entity.isPublished(),
                entity.isRedeemed()
        );
    }
}
