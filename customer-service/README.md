# customer-service

Microserviço de **conta do usuário** no VaultGame: cadastro, login (JWT), perfil, endereços e lista de desejos. Tudo roda em **um único processo** e **um banco PostgreSQL**, com capacidades internas separadas por pacotes (hexagonal).

## Capacidades internas

| Módulo | Responsabilidade |
|--------|------------------|
| **Identity / Auth** | Registro, login, emissão e validação de JWT, roles |
| **Customer** | Perfil do cliente (sem senha) |
| **Address** | Endereços de entrega e cobrança |
| **Wishlist** | Produtos favoritos (`productId` referencia o catálogo) |

`customerId` = `userId` (UUID): tabela `users` (credenciais) + `customers` (perfil) com o mesmo id.

## Arquitetura

```
src/main/java/com/vaultgame/customer/
├── domain/          identity, customer, address, wishlist
├── application/     use cases por módulo
└── infrastructure/  security (Spring Security + JWT), web, persistence, config
```

- **domain** — sem Spring, JPA ou HTTP.
- **application** — regras e orquestração (ex.: registro cria `User` + `Customer` na mesma transação).
- **infrastructure** — REST, JPA/Flyway, `SecurityFilterChain`.

## Executar localmente

1. Na raiz do repositório, subir o Postgres:

```bash
docker compose up dbcustomer -d
```

2. Na pasta `customer-service`:

```bash
mvn spring-boot:run
```

| Variável | Padrão |
|----------|--------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5437/customer_db` |
| `SPRING_DATASOURCE_USERNAME` | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` |
| `SERVER_PORT` | `8091` |
| `JWT_SECRET` | (dev default no `application.yml`; **altere em produção**) |
| `JWT_ISSUER` | `vaultgame-identity` |
| `JWT_EXPIRATION_SECONDS` | `3600` |

## Executar com Docker

Na raiz do repositório:

```bash
docker compose up --build dbcustomer customer
```

API: `http://localhost:8091`

## Endpoints

### Auth (público ou JWT)

| Método | Path | Auth |
|--------|------|------|
| POST | `/auth/register` | público |
| POST | `/auth/login` | público |
| GET | `/auth/me` | Bearer JWT |
| GET | `/actuator/health` | público |

**Register** (`201`):

```json
{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "Ada Lovelace"
}
```

Resposta inclui `userId`, dados básicos e `tokens` (`accessToken`, `tokenType`, `expiresIn`).

**Login**:

```json
{ "email": "user@example.com", "password": "password123" }
```

### Perfil

| Método | Path | Auth |
|--------|------|------|
| GET | `/customers/me` | JWT |
| PATCH | `/customers/me` | JWT |

Header nas rotas protegidas:

```http
Authorization: Bearer <accessToken>
```

### Endereços

| Método | Path |
|--------|------|
| GET | `/customers/me/addresses` |
| POST | `/customers/me/addresses` |
| PUT | `/customers/me/addresses/{addressId}` |
| DELETE | `/customers/me/addresses/{addressId}` |

`type`: `DELIVERY` ou `BILLING`. Máximo **20** endereços por cliente.

### Wishlist

| Método | Path |
|--------|------|
| GET | `/customers/me/wishlist` |
| POST | `/customers/me/wishlist` — `{ "productId": "..." }` |
| DELETE | `/customers/me/wishlist/{productId}` |

Não valida se o produto existe no catálogo (integração futura).

## Erros

Formato:

```json
{ "code": "INVALID_CREDENTIALS", "message": "..." }
```

Códigos comuns: `EMAIL_ALREADY_REGISTERED`, `INVALID_CREDENTIALS`, `UNAUTHORIZED`, `NOT_FOUND`, `ADDRESS_LIMIT_EXCEEDED`, `WISHLIST_ITEM_ALREADY_EXISTS`, `INVALID_REQUEST`, `INTERNAL_ERROR`.

## Kafka (futuro)

Integração com outros microserviços via **Kafka** e **REST** está prevista, mas **não implementada**. Evento sugerido para evolução: `CustomerRegistered` (após registro bem-sucedido).
