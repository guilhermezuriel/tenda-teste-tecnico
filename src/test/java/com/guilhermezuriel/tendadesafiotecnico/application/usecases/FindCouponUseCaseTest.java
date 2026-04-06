package com.guilhermezuriel.tendadesafiotecnico.application.usecases;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.ApplicationError;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.CouponNotFoundError;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.findcoupon.FindCouponInput;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.findcoupon.FindCouponOutput;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.findcoupon.FindCouponUseCase;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.Coupon;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.enums.CouponStatus;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.CouponCode;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DateRange;
import com.guilhermezuriel.tendadesafiotecnico.domain.entities.valueObjects.DiscountAmount;
import com.guilhermezuriel.tendadesafiotecnico.domain.repositories.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindCouponUseCase")
public class FindCouponUseCaseTest {

    @Mock
    private CouponRepository repository;

    @InjectMocks
    private FindCouponUseCase useCase;

    private final String VALID_ID = UUID.randomUUID().toString();
    private final LocalDateTime FUTURE_DATE = LocalDateTime.now().plusDays(30);

    private Coupon buildCoupon() {
        return Coupon.reconstitute(
                VALID_ID,
                CouponCode.create("SAVE20").getValue(),
                "Desconto de verão",
                DiscountAmount.create(new BigDecimal("20.00")).getValue(),
                DateRange.create(FUTURE_DATE).getValue(),
                CouponStatus.ACTIVE,
                false,
                false
        );
    }

    @Test
    @DisplayName("Deve retornar o cupom quando encontrado")
    void shouldFindCouponSuccessfully() {
        FindCouponInput input = FindCouponInput.create(VALID_ID).getValue();
        when(repository.findById(VALID_ID)).thenReturn(Optional.of(buildCoupon()));

        Result<FindCouponOutput, ApplicationError> result = useCase.execute(input);

        assertTrue(result.isSuccess());
        assertEquals(VALID_ID, result.getValue().id());
        assertEquals("SAVE20", result.getValue().code());
    }

    @Test
    @DisplayName("Deve retornar CouponNotFoundError quando o cupom não existe")
    void shouldReturnCouponNotFoundError() {
        FindCouponInput input = FindCouponInput.create(VALID_ID).getValue();
        when(repository.findById(VALID_ID)).thenReturn(Optional.empty());

        Result<FindCouponOutput, ApplicationError> result = useCase.execute(input);

        assertTrue(result.isFailure());
        assertInstanceOf(CouponNotFoundError.class, result.getError());
        assertEquals("COUPON_NOT_FOUND", result.getError().getCode());
    }
}
