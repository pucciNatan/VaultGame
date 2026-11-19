# email-service

Microserviço de **envio de e-mails** com templates HTML (Thymeleaf) e SMTP. Em desenvolvimento, os e-mails vão para **Mailpit** (nada sai para a internet); a caixa de entrada fica em `http://localhost:8025`.

**Produção de eventos:** outros serviços publicam em Kafka; este serviço **consome** e envia e-mail. O endpoint REST `POST /emails/send` permanece para smoke manual.

Convenções: ver [ARCHITECTURE.md](../ARCHITECTURE.md).

## Arquitetura

Hexagonal, **sem banco de dados**:

- **domain** — `EmailMessage`, `EmailTemplate`
- **application** — `SendTemplatedEmailPort` / use case
- **adapter/in** — REST + **Kafka listeners**
- **adapter/out** — SMTP (JavaMail)
- **config** — `KafkaConsumerConfig`, wiring Spring Kafka

## Mailpit

| Porta | Uso |
|-------|-----|
| **1025** | SMTP (Spring envia aqui) |
| **8025** | Interface web (ver e-mails capturados) |

## Executar localmente

1. Na raiz do repositório:

```bash
docker compose up mailpit kafka -d
```

2. Na pasta `email-service`:

```bash
mvn spring-boot:run
```

| Variável | Padrão |
|----------|--------|
| `SPRING_MAIL_HOST` | `localhost` |
| `SPRING_MAIL_PORT` | `1025` |
| `MAIL_FROM` | `noreply@vaultgame.local` |
| `SERVER_PORT` | `8092` |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9094` |

## Executar com Docker

Na raiz:

```bash
docker compose up --build kafka mailpit email
```

- API: `http://localhost:8092`
- Mailpit UI: `http://localhost:8025`

## Endpoints

| Método | Path | Descrição |
|--------|------|-----------|
| POST | `/emails/send` | Envia e-mail a partir de template (dev/smoke) |
| GET | `/actuator/health` | Health check |

### Exemplo: welcome

```bash
curl -s -X POST http://localhost:8092/emails/send \
  -H "Content-Type: application/json" \
  -d "{\"template\":\"WELCOME\",\"to\":\"dev@example.com\",\"variables\":{\"fullName\":\"Ada\",\"email\":\"dev@example.com\"}}"
```

## Templates

| Enum | Arquivo | Variáveis |
|------|---------|-----------|
| `WELCOME` | `templates/email/welcome.html` | `fullName`, `email` |
| `ORDER_CONFIRMED` | `templates/email/order-confirmed.html` | `fullName`, `orderId` |

## Kafka

Consumer group: **`email-service`**.

| Tópico | Ação |
|--------|------|
| `vaultgame.customer.registered` | Envia `WELCOME` |
| `vaultgame.order.paid` | Envia `ORDER_CONFIRMED` |

Envelope JSON (todos os eventos):

```json
{
  "eventId": "uuid",
  "occurredAt": "2026-01-01T12:00:00Z",
  "payload": { }
}
```

**Idempotência (v1):** deduplicação em memória por `eventId` (reinício do processo pode reenviar — documentado para dev).

## Segurança

Sem autenticação nesta fase (serviço de dev). Em produção: consumo interno via Kafka e/ou proteção de rede — não expor SMTP público.
