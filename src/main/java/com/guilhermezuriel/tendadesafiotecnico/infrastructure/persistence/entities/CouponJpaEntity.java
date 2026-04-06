package com.guilhermezuriel.tendadesafiotecnico.infrastructure.persistence.entities;


import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
public class CouponJpaEntity {

    @Id
    private String id;

    @Column( length = 6, nullable = false)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean redeemed;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}