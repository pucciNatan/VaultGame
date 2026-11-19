# order-service

Microserviço de **pedidos** e **carrinho** (Redis): visitante (`X-Cart-Id`), usuário autenticado (JWT), merge ao logar e checkout orquestrado via **saga Kafka** (request-reply síncrono na API).

**E-mail de confirmação:** publicado em Kafka (`vaultgame.order.paid`) quando o pagamento fica `PAID`.

Convenções: ver [ARCHITECTURE.md](../ARCHITECTURE.md).

## Capacidades internas

| Módulo | Persistência | Responsabilidade |
|--------|--------------|------------------|
| **cart** | Redis | Itens por chave `cart:guest:{uuid}` ou `cart:user:{customerId}` |
| **order** | PostgreSQL | Pedidos, linhas, status, idempotência |

Checkout **síncrono na HTTP**, comunicação inter-serviços por **Kafka** (catálogo, estoque, pagamento). E-mail e `fullName` vêm do JWT (`email`, `fullName`).

## Executar localmente

1. Dependências:

```bash
docker compose up dborder redis dbinventory inventory paymentgateway kafka dbcustomer customer -d
```

2. Catálogo `api` em `http://localhost:8080` com `KAFKA_BOOTSTRAP_SERVERS=localhost:9094`.

3. Na pasta `order-service`:

```bash
mvn spring-boot:run
```

| Variável | Padrão |
|----------|--------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5438/order_db` |
| `REDIS_HOST` / `REDIS_PORT` | `localhost` / `6379` |
| `SERVER_PORT` | `8093` |
| `JWT_SECRET` / `JWT_ISSUER` | iguais ao `customer-service` |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9094` |
| `CART_TTL_DAYS` | `7` |

## Docker

```bash
docker compose up --build dborder redis kafka order inventory paymentgateway email customer
```

API: `http://localhost:8093`. O catálogo (`api`) precisa do Kafka no host ao rodar fora do compose.

## Carrinho: guest vs JWT

| Cenário | Headers |
|---------|---------|
| Visitante | `X-Cart-Id: <uuid>` em GET/POST/PATCH/DELETE `/cart/**` |
| Logado | `Authorization: Bearer <token>` (usa carrinho do `customerId`) |
| Merge | JWT + `X-Cart-Id` do visitante → `POST /cart/merge` |

## Endpoints

### Carrinho

| Método | Path |
|--------|------|
| GET | `/cart` |
| POST | `/cart/items` — `{ "productId", "quantity" }` |
| PATCH | `/cart/items/{productId}` |
| DELETE | `/cart/items/{productId}` |
| DELETE | `/cart` |
| POST | `/cart/merge` (JWT) |

### Pedidos (JWT)

| Método | Path |
|--------|------|
| POST | `/orders/checkout` — header `Idempotency-Key` opcional |
| GET | `/orders/{orderId}` |
| GET | `/orders/me?limit=20` |

**Checkout** body:

```json
{
  "shippingAddressId": "uuid-do-endereco",
  "paymentMethod": { "type": "CARD", "token": "test-approved" }
}
```

Fluxo: valida carrinho → **Kafka** catálogo → reserva estoque → pedido `PENDING_PAYMENT` → pagamento → commit/release estoque → **`vaultgame.order.paid`** se `PAID` → limpa carrinho.

## Erros

`EMPTY_CART`, `MISSING_CART_IDENTITY`, `IDEMPOTENCY_CONFLICT`, `ORDER_NOT_FOUND`, `CATALOG_PRODUCT_NOT_FOUND`, `DOWNSTREAM_ERROR`, `UNAUTHORIZED`, etc.

## Kafka

### Checkout (request-reply)

| Tópico | Uso |
|--------|-----|
| `vaultgame.checkout.commands.catalog` | Resolver produtos |
| `vaultgame.checkout.commands.inventory` | Reserve / commit / release |
| `vaultgame.checkout.commands.payment` | Cobrança |
| `vaultgame.checkout.replies` | Respostas (chave = `correlationId`) |

Timeout padrão: `vaultgame.checkout.kafka.reply-timeout` (10s).

### Eventos

Tópico **`vaultgame.order.paid`** — payload inclui `orderId`, `customerId`, `email`, `fullName`, `total`, `currency`. Chave Kafka: `orderId`.