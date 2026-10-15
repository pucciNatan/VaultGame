# inventory-service

Microserviço de **estoque** do VaultGame: disponibilidade por `productId` e reservas para checkout (commit/release). É a **única fonte de verdade** de quantidade após a migração do catálogo.

## Arquitetura

```
src/main/java/com/vaultgame/inventory/
├── domain/          model, exceptions, repository ports
├── application/     AdjustStock, GetAvailability, Reserve/Commit/Release
└── infrastructure/  web, persistence (JPA/Flyway), config, messaging (stub Kafka)
```

## Executar localmente

1. Postgres:

```bash
docker compose up dbinventory -d
```

2. Na pasta `inventory-service`:

```bash
mvn spring-boot:run
```

| Variável | Padrão |
|----------|--------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5439/inventory_db` |
| `SERVER_PORT` | `8094` |
| `INVENTORY_RESERVATION_TTL_MINUTES` | `30` |

## Docker

```bash
docker compose up --build dbinventory inventory
```

API: `http://localhost:8094`

## Endpoints

### Leitura (vitrine)

| Método | Path |
|--------|------|
| GET | `/inventory/{productId}` |
| GET | `/inventory?productIds=id1,id2` |

### Interno (seed / checkout)

| Método | Path |
|--------|------|
| PUT | `/internal/inventory/{productId}` — body `{ "availableQuantity": 100 }` |
| POST | `/internal/reservations` |
| POST | `/internal/reservations/{id}/commit` |
| POST | `/internal/reservations/{id}/release` |

Após criar um produto no catálogo (`api`), defina o estoque inicial:

```bash
curl -X PUT http://localhost:8094/internal/inventory/PRODUCT_ID \
  -H "Content-Type: application/json" \
  -d '{"availableQuantity":50}'
```

## Erros

Formato: `{ "code": "...", "message": "..." }`

Códigos: `INVENTORY_NOT_FOUND`, `INSUFFICIENT_STOCK`, `RESERVATION_NOT_FOUND`, `INVALID_RESERVATION_STATE`, `INVENTORY_CONFLICT`, `INVALID_REQUEST`, `INTERNAL_ERROR`.

## Kafka (futuro)

Stub `InventoryAdjustedEvent` em `infrastructure/messaging`.
