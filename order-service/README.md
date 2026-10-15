# order-service

Microserviço de **pedidos** e **carrinho** (Redis): visitante (`X-Cart-Id`), usuário autenticado (JWT), merge ao logar e checkout orquestrado (catálogo, estoque, pagamento, e-mail).

## Capacidades internas

| Módulo | Persistência | Responsabilidade |
|--------|--------------|------------------|
| **cart** | Redis | Itens por chave `cart:guest:{uuid}` ou `cart:user:{customerId}` |
| **order** | PostgreSQL | Pedidos, linhas, status, idempotência |

## Arquitetura

```
src/main/java/com/vaultgame/order/
├── domain/          cart, order, ports (catalog, inventory, payment, email, customer)
├── application/     CartService, PlaceOrderService, GetOrderService
└── infrastructure/  redis, web, persistence, client (RestClient), security, messaging stub
```

## Executar localmente

1. Dependências:

```bash
docker compose up dborder redis dbinventory inventory paymentgateway email dbcustomer customer -d
```

2. Catálogo `api` em `http://localhost:8080` (fora do compose).

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
| `CATALOG_BASE_URL` | `http://localhost:8080` |
| `INVENTORY_BASE_URL` | `http://localhost:8094` |
| `PAYMENT_BASE_URL` | `http://localhost:8090` |
| `EMAIL_BASE_URL` | `http://localhost:8092` |
| `CUSTOMER_BASE_URL` | `http://localhost:8091` |
| `CART_TTL_DAYS` | `7` |

## Docker

```bash
docker compose up --build dborder redis order inventory paymentgateway email customer
```

API: `http://localhost:8093` (catálogo no host: `CATALOG_BASE_URL=http://host.docker.internal:8080`).

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

Fluxo: valida carrinho → catálogo → reserva estoque → pedido `PENDING_PAYMENT` → pagamento → commit/release estoque → e-mail se `PAID` → limpa carrinho.

## Erros

`EMPTY_CART`, `MISSING_CART_IDENTITY`, `IDEMPOTENCY_CONFLICT`, `ORDER_NOT_FOUND`, `CATALOG_PRODUCT_NOT_FOUND`, `DOWNSTREAM_ERROR`, `UNAUTHORIZED`, etc.

## Kafka (futuro)

Stub `OrderPlacedEvent` em `infrastructure/messaging`.
