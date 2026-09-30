# fake-payment-gateway

Serviço independente que simula uma API externa de pagamentos para desenvolvimento e testes. Outros sistemas podem integrá-lo como se fosse um gateway real, usando tokens fictícios para obter respostas **determinísticas** (aprovado, recusado, pendente, timeout ou erro).

## Arquitetura

Arquitetura hexagonal simples, com três camadas:

- **domain** — modelo (`Payment`, `PaymentStatus`), exceções de negócio e port `PaymentRepository` (sem Spring, JPA ou HTTP).
- **application** — `PaymentService` (criação, consulta, idempotência) e `PaymentSimulator` (comportamento por token; único componente com delay).
- **infrastructure** — REST (`PaymentController`, DTOs, `GlobalExceptionHandler`), persistência JPA/Flyway e configuração Spring.

```
src/main/java/com/vaultgame/fakepaymentgateway/
├── domain/
│   ├── model/
│   ├── exception/
│   └── repository/
├── application/
│   ├── service/
│   └── simulation/
└── infrastructure/
    ├── web/
    ├── persistence/
    └── config/
```

## Executar localmente

1. Na raiz do repositório, subir apenas o PostgreSQL do gateway:

```bash
docker compose up dbpayments -d
```

2. Rodar a aplicação (Java 21 + Maven):

```bash
mvn spring-boot:run
```

Variáveis opcionais:

| Variável | Padrão |
|----------|--------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5436/fake_payment_gateway` |
| `SPRING_DATASOURCE_USERNAME` | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` |
| `SERVER_PORT` | `8090` |
| `PAYMENT_TIMEOUT_MS` | `5000` |

## Executar com Docker

Na raiz do repositório (`VaultGame/`):

```bash
docker compose up --build dbpayments paymentgateway
```

API em `http://localhost:8090`.

## Endpoints

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `POST` | `/payments` | Cria/processa um pagamento simulado |
| `GET` | `/payments/{transactionId}` | Consulta um pagamento |
| `GET` | `/actuator/health` | Health check |

### Criar pagamento

```http
POST /payments
Content-Type: application/json
Idempotency-Key: abc-123
```

```json
{
  "reference": "order-123",
  "amount": 199.90,
  "currency": "BRL",
  "paymentMethod": {
    "type": "CREDIT_CARD",
    "token": "test-approved"
  }
}
```

Resposta (`201 Created`):

```json
{
  "transactionId": "tx-550e8400-e29b-41d4-a716-446655440000",
  "reference": "order-123",
  "status": "APPROVED"
}
```

### Consultar pagamento

```http
GET /payments/tx-550e8400-e29b-41d4-a716-446655440000
```

## Tokens fictícios

O campo `paymentMethod.token` define o comportamento. Não há aleatoriedade.

| Token | Comportamento |
|-------|----------------|
| `test-approved` | Pagamento aprovado (`APPROVED`), persistido |
| `test-declined` | Pagamento recusado (`DECLINED`), persistido |
| `test-pending` | Pagamento criado como `PENDING`, persistido |
| `test-timeout` | Aguarda `PAYMENT_TIMEOUT_MS` e responde **504**; **não** persiste |
| `test-error` | Responde **500** (`PAYMENT_PROCESSING_ERROR`); **não** persiste |

Token desconhecido → **400** (`INVALID_PAYMENT_TOKEN`).

Dados de cartão reais **não** são armazenados; apenas metadados da transação.

## Timeout

Configure o tempo de espera do cenário `test-timeout`:

```bash
PAYMENT_TIMEOUT_MS=10000
```

Padrão: `5000` ms. A lógica de espera está centralizada em `PaymentSimulator`.

## Idempotência

Envie o header opcional:

```http
Idempotency-Key: abc-123
```

Comportamento:

1. **Sem header** — cada `POST` cria uma nova transação.
2. **Mesma chave + mesmo corpo** — retorna a transação já criada (hash SHA-256 de `reference`, `amount`, `currency`, `type` e `token`). Evita duplicar cobrança em retries de rede.
3. **Mesma chave + corpo diferente** — **409** (`IDEMPOTENCY_KEY_CONFLICT`).
4. **Timeout ou erro simulado** — nada é gravado; a mesma chave pode ser reenviada (a operação não completou).

A chave e um hash do request são persistidos no PostgreSQL; o token em si não é salvo.

## Erros

Formato consistente:

```json
{
  "code": "PAYMENT_PROCESSING_ERROR",
  "message": "Unable to process payment"
}
```

| HTTP | Código (exemplos) |
|------|-------------------|
| 400 | `INVALID_REQUEST`, `INVALID_PAYMENT_TOKEN` |
| 404 | `PAYMENT_NOT_FOUND` |
| 409 | `IDEMPOTENCY_KEY_CONFLICT` |
| 500 | `PAYMENT_PROCESSING_ERROR`, `INTERNAL_ERROR` |
| 504 | `PAYMENT_TIMEOUT` |

Stack traces não são expostos ao cliente.
