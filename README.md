# Tenda - Desafio Tecnico

API REST para gerenciamento de cupons de desconto, desenvolvida com Java 17 e Spring Boot.

---

## Sobre o projeto

A aplicacao expoe endpoints para criacao, consulta e exclusao de cupons de desconto. O foco do desafio foi a qualidade arquitetural: separacao clara de responsabilidades, regras de negocio protegidas no dominio e um modelo de tratamento de erros explicito.

**Stack:** Java 17, Spring Boot 4, PostgreSQL, Flyway, SpringDoc OpenAPI, Lombok

---

## Arquitetura

O projeto segue Clean Architecture, organizada em quatro camadas com dependencias que apontam sempre para dentro (em direcao ao dominio):

```
interfaces -> application -> domain
infrastructure -> domain
```

- **domain**: entidades, value objects, enums, interfaces de repositorio e erros de dominio. Zero dependencias externas ou de framework.
- **application**: use cases, DTOs de entrada/saida e erros de aplicacao. Depende apenas do dominio.
- **infrastructure**: implementacoes JPA, entidades de persistencia e mappers. Depende do dominio para cumprir as interfaces.
- **interfaces**: controllers HTTP, request/response DTOs. Depende da camada de aplicacao.

---

## Estrutura de pastas

```
src/main/java/com/guilhermezuriel/tendadesafiotecnico/
│
├── _shared/
│   ├── errors/          # BaseError (interface raiz de todos os erros)
│   └── result/          # Result<T, E> (pattern Result)
│
├── domain/
│   ├── entities/
│   │   ├── Coupon.java
│   │   ├── enums/       # CouponStatus (ACTIVE, INACTIVE, DELETED)
│   │   └── valueObjects/ # CouponCode, DiscountAmount, DateRange
│   ├── errors/          # DomainError, ValidationError, BusinessRuleError
│   └── repositories/    # CouponRepository (interface)
│
├── application/
│   ├── errors/          # ApplicationError e especializacoes
│   └── usecases/
│       ├── createcoupon/
│       ├── deletecoupon/
│       └── findcoupon/
│
├── infrastructure/
│   └── persistence/
│       ├── entities/    # CouponJpaEntity
│       ├── mappers/     # CouponMapper (dominio <-> JPA)
│       └── repositories/ # CouponRepositoryImpl, CouponJpaRepository
│
└── interfaces/
    └── http/
        ├── config/      # OpenApiConfig
        ├── controllers/ # CouponController, GlobalExceptionHandler
        ├── requests/    # CreateCouponRequest
        └── responses/   # CouponResponse, ApiErrorResponse
```

---

## Decisoes tecnicas

### Pattern Result

Em vez de excecoes para controle de fluxo, foi adotado `Result<T, E>`. Metodos que podem falhar retornam `Result` e o chamador e forcado a tratar o caminho de erro explicitamente. Decisão tomada visando que a responsabilidade de lidar com o erro deve ser sempre da camada superior, ela quem deve decidir o que fazer com o erro.

### Value Objects

Os tres value objects do dominio encapsulam suas proprias regras de validacao:

- `CouponCode`: aceita apenas letras e numeros, entre 3 e 20 caracteres.
- `DiscountAmount`: valor minimo de 0.5.
- `DateRange`: data de expiracao deve ser futura.

Cada value object possui dois metodos de construcao: `create()` valida e retorna `Result`; `reconstitute()` reconstroi a partir do banco sem revalidar (os dados ja foram validados na criacao).

### Hierarquia de erros

```
AppError (interface)
  DomainError
    ValidationError
    BusinessRuleError
  ApplicationError
    CouponNotFoundError
    ExistingCouponCodeError
    CouponAlreadyDeletedError
    InvalidCouponError
```

Essa hierarquia permite identificar a origem do erro (dominio vs aplicacao) e mapear para HTTP status codes apropriados no `GlobalExceptionHandler`.

### Entidade Coupon

- O ID e um UUID gerado internamente, nunca recebido como entrada.
- `code` e a chave unica de negocio.
- O status e um enum persistido como string; conversao usa `fromString()` que retorna `Result`.
- Soft delete implementado via `markAsDeleted()`, que verifica regras antes de alterar o status.

### Separacao entre entidade de dominio e JPA

`Coupon` (dominio) e `CouponJpaEntity` (infraestrutura) sao classes distintas. O `CouponMapper` faz a conversao entre elas. Isso isola o dominio de anotacoes JPA e permite que a entidade de dominio evolua sem restricoes da camada de persistencia.

### Use cases

Um use case por operacao. Anotados com `@Service` para injecao pelo Spring, mas sem dependencias diretas de framework na logica de negocio. Recebem um Input e retornam `Result<Output, ApplicationError>`.

### Migracao de banco

Flyway gerencia o schema. A migracao inicial (`V1__create_coupons_table.sql`) cria a tabela `coupons` com soft delete via coluna `status`.

---

## Como executar

### Pre-requisitos

- Docker e Docker Compose

### Subindo tudo com Docker Compose

```bash
docker compose up --build
```

Isso sobe o PostgreSQL na porta `5433` e a aplicacao na porta `8080`.

### Executando localmente (sem Docker para a app)

1. Suba apenas o banco:

```bash
docker compose up postgres
```

2. Execute a aplicacao:

```bash
./mvnw spring-boot:run
```

A aplicacao conecta em `jdbc:postgresql://localhost:5433/tenda_desafio` com usuario e senha `postgres`.

---

## Endpoints da API

Base URL: `http://localhost:8080`

Documentacao interativa disponivel em: `http://localhost:8080/swagger-ui.html`

### POST /coupon

Cria um novo cupom de desconto.

**Request body:**
```json
{
  "code": "DESCONTO10",
  "description": "10% de desconto na compra",
  "discountValue": 10.00,
  "expirationDate": "2026-12-31T23:59:59",
  "published": true
}
```

| Campo           | Tipo          | Obrigatorio | Restricoes                           |
|-----------------|---------------|-------------|---------------------------------------|
| code            | string        | sim         | 3-20 chars, apenas letras e numeros   |
| description     | string        | sim         | nao vazio                             |
| discountValue   | decimal       | sim         | minimo 0.5                            |
| expirationDate  | LocalDateTime | sim         | data futura                           |
| published       | boolean       | nao         | default false                         |

**Respostas:**

| Status | Descricao                                |
|--------|------------------------------------------|
| 201    | Cupom criado com sucesso                 |
| 409    | Ja existe um cupom ativo com esse codigo |
| 422    | Dados invalidos (validacao ou dominio)   |

---

### GET /coupon/{id}

Retorna os dados de um cupom pelo UUID.

**Respostas:**

| Status | Descricao                       |
|--------|---------------------------------|
| 200    | Cupom encontrado                |
| 404    | Cupom nao encontrado            |
| 422    | ID invalido (nao e um UUID)     |

---

### DELETE /coupon/{id}

Realiza exclusao logica do cupom (status muda para `DELETED`). Cupons ja deletados nao podem ser deletados novamente.

**Respostas:**

| Status | Descricao                              |
|--------|----------------------------------------|
| 204    | Cupom deletado com sucesso             |
| 404    | Cupom nao encontrado ou ja deletado    |
| 422    | ID invalido (nao e um UUID)            |
