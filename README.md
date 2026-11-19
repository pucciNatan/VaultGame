# VaultGame
Plataforma e-commerce de games baseada em microsserviços orientados a eventos (Kafka), Spring Boot, React/TypeScript e persistência poliglota (PostgreSQL + MongoDB).

Convenções de arquitetura hexagonal nos serviços Java: [ARCHITECTURE.md](ARCHITECTURE.md).

## Serviços (dev)

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| `api` (catálogo) | 8080 | Produtos no MongoDB (sem campo `stock`; estoque no inventory-service) |
| `fake-payment-gateway` | 8090 | Pagamento simulado |
| `customer-service` | 8091 | Conta, JWT, endereços, wishlist |
| `email-service` | 8092 | E-mails transacionais |
| `order-service` | 8093 | Carrinho (Redis) e pedidos |
| `inventory-service` | 8094 | Estoque e reservas |
| Redis | 6379 | Carrinho de compras |
| Kafka | 9094 (host) / `kafka:9092` (Docker) | Checkout saga (request-reply) + eventos assíncronos |

Ver READMEs em cada pasta e `docker compose up --build` na raiz para stack principal.

### Kafka (dev)

Subir o broker:

```bash
docker compose up kafka -d
```

Stack com checkout via Kafka (catálogo `api` no host com
`KAFKA_BOOTSTRAP_SERVERS=localhost:9094`):

```bash
docker compose up --build kafka mailpit email customer order dborder redis dbinventory inventory paymentgateway dbcustomer
```

| Tópico | Papel |
|--------|-------|
| `vaultgame.checkout.commands.catalog` | order → api (request-reply) |
| `vaultgame.checkout.commands.inventory` | order → inventory (reserve/commit/release) |
| `vaultgame.checkout.commands.payment` | order → payment gateway |
| `vaultgame.checkout.replies` | respostas correlacionadas → order |
| `vaultgame.customer.registered` | customer → email (`WELCOME`) |
| `vaultgame.order.paid` | order → email (`ORDER_CONFIRMED`) |

Apps no host usam `localhost:9094`; containers usam `kafka:9092`.
