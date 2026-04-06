package com.guilhermezuriel.tendadesafiotecnico.infrastructure.persistence;

import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.CouponCode;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DateRange;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DiscountAmount;
import com.guilhermezuriel.tendadesafiotecnico.domain.repositories.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
@DisplayName("CouponRepositoryImpl")
public class CouponRepositoryImplTest {

    @Autowired
    private CouponRepository repository;

    private final LocalDateTime FUTURE_DATE = LocalDateTime.now().plusDays(30);

    private Coupon buildCoupon(String code) {
        return Coupon.reconstitute(
                UUID.randomUUID().toString(),
                CouponCode.create(code).getValue(),
                "Desconto de teste",
                DiscountAmount.create(new BigDecimal("15.00")).getValue(),
                DateRange.create(FUTURE_DATE).getValue(),
                CouponStatus.ACTIVE,
                false,
                false
        );
    }

    @Test
    @DisplayName("Deve salvar e encontrar um cupom pelo ID")
    void shouldSaveAndFindById() {
        Coupon coupon = buildCoupon("SAVE20");
        repository.save(coupon);

        Optional<Coupon> found = repository.findById(coupon.getId());

        assertTrue(found.isPresent());
        assertEquals(coupon.getId(), found.get().getId());
        assertEquals("SAVE20", found.get().getCode().getCode());
    }

    @Test
    @DisplayName("Deve encontrar um cupom pelo código")
    void shouldFindByCode() {
        Coupon coupon = buildCoupon("PROMO1");
        repository.save(coupon);

        Optional<Coupon> found = repository.findByCode(coupon.getCode());

        assertTrue(found.isPresent());
        assertEquals("PROMO1", found.get().getCode().getCode());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por ID inexistente")
    void shouldReturnEmptyWhenIdNotFound() {
        Optional<Coupon> found = repository.findById(UUID.randomUUID().toString());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por código inexistente")
    void shouldReturnEmptyWhenCodeNotFound() {
        Optional<Coupon> found = repository.findByCode(CouponCode.create("GHOST1").getValue());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("findByCodeAndStatusNot deve encontrar cupom ACTIVE excluindo DELETED")
    void shouldFindByCodeExcludingDeletedStatus() {
        Coupon coupon = buildCoupon("ACT100");
        repository.save(coupon);

        Optional<Coupon> found = repository.findByCodeAndStatusNot(coupon.getCode(), CouponStatus.DELETED);

        assertTrue(found.isPresent());
        assertEquals("ACT100", found.get().getCode().getCode());
    }

    @Test
    @DisplayName("findByCodeAndStatusNot deve retornar vazio quando o status coincide com o excluído")
    void shouldReturnEmptyWhenCodeStatusMatchesExclusion() {
        Coupon coupon = buildCoupon("ACT200");
        repository.save(coupon);

        Optional<Coupon> found = repository.findByCodeAndStatusNot(coupon.getCode(), CouponStatus.ACTIVE);

        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("findByIdAndStatusNot deve encontrar cupom ACTIVE excluindo DELETED")
    void shouldFindByIdExcludingDeletedStatus() {
        Coupon coupon = buildCoupon("IDT001");
        repository.save(coupon);

        Optional<Coupon> found = repository.findByIdAndStatusNot(coupon.getId(), CouponStatus.DELETED);

        assertTrue(found.isPresent());
        assertEquals(coupon.getId(), found.get().getId());
    }

    @Test
    @DisplayName("findByIdAndStatusNot deve retornar vazio quando o status coincide com o excluído")
    void shouldReturnEmptyWhenIdStatusMatchesExclusion() {
        Coupon coupon = buildCoupon("IDT002");
        repository.save(coupon);

        Optional<Coupon> found = repository.findByIdAndStatusNot(coupon.getId(), CouponStatus.ACTIVE);

        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Deve persistir a atualização de status ao salvar novamente")
    void shouldPersistStatusUpdate() {
        Coupon coupon = buildCoupon("UPD001");
        repository.save(coupon);

        coupon.markAsDeleted();
        repository.save(coupon);

        Optional<Coupon> found = repository.findById(coupon.getId());
        assertTrue(found.isPresent());
        assertEquals(CouponStatus.DELETED, found.get().getStatus());
    }
}
