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
| `web` (Next.js) | 3000 | Loja, BFF, autenticação e checkout |
| Redis | 6379 | Carrinho de compras |

Ver READMEs em cada pasta e `docker compose up --build` na raiz para stack principal.

## Frontend Next.js

O frontend está em `web/`. O navegador acessa somente os Route Handlers do
Next.js, que funcionam como BFF e encaminham o JWT em cookie `httpOnly` aos
microsserviços.

Execução local:

```bash
cd web
copy .env.example .env.local
npm install
npm run dev
```

Com os serviços do ecossistema disponíveis, abra `http://localhost:3000`.
Também é possível iniciar o frontend pelo Compose:

```bash
docker compose up --build web
```
