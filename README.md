# VaultGame
Plataforma e-commerce de games baseada em microsserviços orientados a eventos (Kafka), Spring Boot, React/TypeScript e persistência poliglota (PostgreSQL + MongoDB).

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

Ver READMEs em cada pasta e `docker compose up --build` na raiz para stack principal.
