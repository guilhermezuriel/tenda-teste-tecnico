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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
@Tag(name = "Cupons", description = "Operações de gerenciamento de cupons de desconto")
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

    @Operation(
            summary = "Criar cupom",
            description = "Cria um novo cupom de desconto. O código é sanitizado (maiúsculas, sem caracteres especiais) e deve ter exatamente 6 caracteres após o tratamento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CouponResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe um cupom ativo com esse código",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Dados inválidos — erros de validação dos campos ou regras de domínio",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
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

    @Operation(
            summary = "Buscar cupom por ID",
            description = "Retorna os dados de um cupom a partir do seu UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cupom encontrado",
                    content = @Content(schema = @Schema(implementation = CouponResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cupom não encontrado para o ID informado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "ID inválido — deve ser um UUID válido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findByCode(
            @Parameter(description = "UUID do cupom", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String id
    ) {
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

    @Operation(
            summary = "Deletar cupom",
            description = "Realiza a exclusão lógica de um cupom, alterando seu status para DELETED. Cupons já deletados não podem ser deletados novamente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cupom deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cupom não encontrado ou já deletado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "ID inválido — deve ser um UUID válido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "UUID do cupom", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String id
    ) {
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
