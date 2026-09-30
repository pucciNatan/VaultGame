# email-service

Microserviço de **envio de e-mails** com templates HTML (Thymeleaf) e SMTP. Em desenvolvimento, os e-mails vão para **Mailpit** (nada sai para a internet); a caixa de entrada fica em `http://localhost:8025`.

Outros microserviços **não** estão integrados ainda. O consumo via **Kafka** está planejado (ver abaixo).

## Arquitetura

Hexagonal, **sem banco de dados**:

- **domain** — `EmailMessage`, `EmailTemplate`, ports `EmailSenderPort` e `EmailTemplateRendererPort`
- **application** — `SendTemplatedEmailService`
- **infrastructure** — `JavaMailSenderAdapter`, `ThymeleafTemplateRenderer`, REST, config
- **infrastructure.messaging** — record `EmailCommand` + pacote reservado para listener Kafka futuro

```
src/main/java/com/vaultgame/email/
├── domain/
├── application/service/
└── infrastructure/
    ├── mail/
    ├── web/
    ├── config/
    └── messaging/
```

## Mailpit

| Porta | Uso |
|-------|-----|
| **1025** | SMTP (Spring envia aqui) |
| **8025** | Interface web (ver e-mails capturados) |

## Executar localmente

1. Na raiz do repositório:

```bash
docker compose up mailpit -d
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

## Executar com Docker

Na raiz:

```bash
docker compose up --build mailpit email
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

Resposta (`202 Accepted`):

```json
{ "status": "SENT", "to": "dev@example.com", "template": "WELCOME" }
```

### Exemplo: pedido confirmado

```json
{
  "template": "ORDER_CONFIRMED",
  "to": "dev@example.com",
  "subject": "Order #123 confirmed",
  "variables": {
    "fullName": "Ada",
    "orderId": "order-123"
  }
}
```

## Templates

| Enum | Arquivo | Variáveis |
|------|---------|-----------|
| `WELCOME` | `templates/email/welcome.html` | `fullName`, `email` |
| `ORDER_CONFIRMED` | `templates/email/order-confirmed.html` | `fullName`, `orderId` |

Se `subject` for omitido, usa o assunto padrão do template.

## Erros

```json
{ "code": "EMAIL_SEND_FAILED", "message": "Failed to send email" }
```

Códigos: `INVALID_REQUEST`, `EMAIL_SEND_FAILED`, `INTERNAL_ERROR`.

## Kafka (planned)

**Não implementado nesta fase** (sem `spring-kafka` no projeto).

Plano:

- Tópico sugerido: `vaultgame.email.send`
- Payload alinhado a [`EmailCommand`](src/main/java/com/vaultgame/email/infrastructure/messaging/EmailCommand.java): `template`, `to`, `subject`, `variables`
- Consumer futuro delegará para `SendTemplatedEmailService`
- Exemplo de fluxo: evento `CustomerRegistered` no `customer-service` → mensagem Kafka → e-mail `WELCOME`

O endpoint `POST /emails/send` existe apenas para validar Mailpit e templates antes do messaging.

## Segurança

Sem autenticação nesta fase (serviço de dev). Em produção: consumo interno via Kafka e/ou proteção de rede — não expor SMTP público.
