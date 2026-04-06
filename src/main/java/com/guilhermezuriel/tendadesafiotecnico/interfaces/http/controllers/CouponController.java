package com.guilhermezuriel.tendadesafiotecnico.interfaces.http.controllers;

import com.guilhermezuriel.tendadesafiotecnico._shared.result.Result;
import com.guilhermezuriel.tendadesafiotecnico.application.errors.ApplicationError;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponOutput;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.createcoupon.CreateCouponUseCase;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.deletecoupon.DeleteCouponInput;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.deletecoupon.DeleteCouponUseCase;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.findcoupon.FindCouponInput;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.findcoupon.FindCouponOutput;
import com.guilhermezuriel.tendadesafiotecnico.application.usecases.findcoupon.FindCouponUseCase;
import com.guilhermezuriel.tendadesafiotecnico.interfaces.http.requests.CreateCouponRequest;
import com.guilhermezuriel.tendadesafiotecnico.interfaces.http.responses.ApiErrorResponse;
import com.guilhermezuriel.tendadesafiotecnico.interfaces.http.responses.CouponResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;
    private final FindCouponUseCase findCouponUseCase;

    public CouponController(
            CreateCouponUseCase createCouponUseCase,
            DeleteCouponUseCase deleteCouponUseCase,
            FindCouponUseCase findCouponUseCase
    ) {
        this.createCouponUseCase = createCouponUseCase;
        this.deleteCouponUseCase = deleteCouponUseCase;
        this.findCouponUseCase = findCouponUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCouponRequest request) {
        Result<CreateCouponOutput, ? extends ApplicationError> result =
                createCouponUseCase.execute(request.toInput());

        if (result.isFailure()) {
            return ApiErrorResponse.from(result.getError());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CouponResponse.from(result.getValue()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByCode(@PathVariable String id) {
        var input = FindCouponInput.create(id);
        if (input.isFailure()) {
            return ApiErrorResponse.from(input.getError());
        }

        Result<FindCouponOutput, ? extends ApplicationError> result = findCouponUseCase.execute(input.getValue());
        if (result.isFailure()) {
            return ApiErrorResponse.from(result.getError());
        }

        return ResponseEntity.ok(CouponResponse.from(result.getValue()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        var input = DeleteCouponInput.create(id);
        if (input.isFailure()) {
            return ApiErrorResponse.from(input.getError());
        }

        Result<Void, ? extends ApplicationError> result = deleteCouponUseCase.execute(input.getValue());
        if (result.isFailure()) {
            return ApiErrorResponse.from(result.getError());
        }

        return ResponseEntity.noContent().build();
    }
}
