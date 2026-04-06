package com.guilhermezuriel.tendadesafiotecnico.application.usecases;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.ApplicationError;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.CouponNotFoundError;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.deletecoupon.DeleteCouponInput;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.deletecoupon.DeleteCouponUseCase;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteCouponUseCase")
public class DeleteCouponUseCaseTest {

    @Mock
    private CouponRepository repository;

    @InjectMocks
    private DeleteCouponUseCase useCase;

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
    @DisplayName("Deve deletar o cupom com sucesso e persistir o novo status")
    void shouldDeleteCouponSuccessfully() {
        DeleteCouponInput input = DeleteCouponInput.create(VALID_ID).getValue();
        Coupon coupon = buildCoupon();
        when(repository.findByIdAndStatusNot(VALID_ID, CouponStatus.DELETED)).thenReturn(Optional.of(coupon));

        Result<Void, ApplicationError> result = useCase.execute(input);

        assertTrue(result.isSuccess());
        assertEquals(CouponStatus.DELETED, coupon.getStatus());
        verify(repository, times(1)).save(coupon);
    }

    @Test
    @DisplayName("Deve retornar CouponNotFoundError quando o cupom não existe ou já foi deletado")
    void shouldReturnCouponNotFoundError() {
        DeleteCouponInput input = DeleteCouponInput.create(VALID_ID).getValue();
        when(repository.findByIdAndStatusNot(VALID_ID, CouponStatus.DELETED)).thenReturn(Optional.empty());

        Result<Void, ApplicationError> result = useCase.execute(input);

        assertTrue(result.isFailure());
        assertInstanceOf(CouponNotFoundError.class, result.getError());
        verify(repository, never()).save(any());
    }
}
