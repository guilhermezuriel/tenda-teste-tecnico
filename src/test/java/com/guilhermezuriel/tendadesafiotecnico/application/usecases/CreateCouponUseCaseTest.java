package com.guilhermezuriel.tendadesafiotecnico.application.usecases;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.ApplicationError;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.ExistingCouponCodeError;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.InvalidCouponError;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponInput;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponOutput;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateCouponUseCase")
public class CreateCouponUseCaseTest {

    @Mock
    private CouponRepository repository;

    @InjectMocks
    private CreateCouponUseCase useCase;

    private final LocalDateTime FUTURE_DATE = LocalDateTime.now().plusDays(30);

    private CreateCouponInput validInput() {
        return new CreateCouponInput("SAVE20", "Desconto de verão", new BigDecimal("20.00"), FUTURE_DATE, false);
    }

    @Test
    @DisplayName("Deve criar cupom com sucesso quando o código não existe")
    void shouldCreateCouponSuccessfully() {
        when(repository.findByCodeAndStatusNot(any(CouponCode.class), eq(CouponStatus.DELETED)))
                .thenReturn(Optional.empty());

        Result<CreateCouponOutput, ApplicationError> result = useCase.execute(validInput());

        assertTrue(result.isSuccess());
        assertEquals("SAVE20", result.getValue().code());
        verify(repository, times(1)).save(any(Coupon.class));
    }

    @Test
    @DisplayName("Deve retornar InvalidCouponError quando a validação de domínio falha")
    void shouldReturnInvalidCouponErrorWhenDomainValidationFails() {
        var input = new CreateCouponInput("TOOLONGCODE", "Desconto", new BigDecimal("10.00"), FUTURE_DATE, false);

        Result<CreateCouponOutput, ApplicationError> result = useCase.execute(input);

        assertTrue(result.isFailure());
        assertInstanceOf(InvalidCouponError.class, result.getError());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar ExistingCouponCodeError quando o código já está em uso")
    void shouldReturnExistingCouponCodeErrorWhenCodeAlreadyExists() {
        Coupon existingCoupon = Coupon.reconstitute(
                "existing-id",
                CouponCode.create("SAVE20").getValue(),
                "Cupom existente",
                DiscountAmount.create(new BigDecimal("10.00")).getValue(),
                DateRange.create(FUTURE_DATE).getValue(),
                CouponStatus.ACTIVE,
                false,
                false
        );
        when(repository.findByCodeAndStatusNot(any(CouponCode.class), eq(CouponStatus.DELETED)))
                .thenReturn(Optional.of(existingCoupon));

        Result<CreateCouponOutput, ApplicationError> result = useCase.execute(validInput());

        assertTrue(result.isFailure());
        assertInstanceOf(ExistingCouponCodeError.class, result.getError());
        verify(repository, never()).save(any());
    }
}
