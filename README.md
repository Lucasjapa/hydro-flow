# 💧 Hydro Flow

Sistema de gestão de abastecimento hídrico desenvolvido para auxiliar no controle e planejamento
da distribuição de água para famílias em regiões de escassez.

## 📋 Sobre o Projeto

O Hydro Flow permite o gerenciamento de:

- **Famílias e membros** — cadastro, localização geográfica e busca paginada com filtros
- **Cisternas** — múltiplas cisternas por família, com nível de água e distribuição de consumo/entregas
- **Entregas de água** — registro de entregas com data e volume; atualização automática dos níveis das cisternas
- **Status e previsões** — indicação de situação (normal, baixa, urgente), dias restantes de água e próxima entrega
- **Configurações do sistema** — consumo diário de referência (mínimo 1 litro/dia)
- **Usuários e acesso (RBAC)** — login JWT, perfis com permissões, gestão administrativa de cargos, troca de senha e **recuperação de senha por e-mail**
- **Famílias ativas/inativas** — cadastro pode ser desativado sem exclusão; o consumo diário automático considera apenas famílias ativas

Documentação de arquitetura (C4) em [`docs/c4/`](docs/c4/). Histórico de versões em [`CHANGELOG.md`](CHANGELOG.md) e [`RELEASES.md`](RELEASES.md).

## 🛠️ Tecnologias

- **Java 25** com **Spring Boot 4.0**
- **Spring Data JPA** + **PostgreSQL 15**
- **Spring Security** + **JWT**
- **Liquibase** para versionamento do banco de dados
- **Docker Compose** + **Jib** para infraestrutura e imagens containerizadas
- **Nginx** como proxy reverso (frontend + API no mesmo host)
- **JaCoCo** para cobertura de testes

## 🚀 Primeira Execução

### Opção A — Stack completa (recomendado)

**1. Construa a imagem do backend:**

```bash
./gradlew jibDockerBuild
```

**2. Construa a imagem do frontend** (repositório `hydro-flow-front`, tag `0.0.1`) e suba os serviços:

```bash
cd infra
docker compose up -d
```

**3. Acesse:**

| Recurso | URL |
|---------|-----|
| Painel web | http://localhost |
| API (via nginx) | http://localhost/hydro-flow/ |
| Swagger (direto no backend) | http://localhost:8080/swagger-ui/index.html |

> Credenciais padrão do administrador seed: ver `infra/env/hydro-flow.env` (`ADMIN_EMAIL`).

### Opção B — Apenas banco + IDE

**1. Suba o PostgreSQL:**

```bash
cd infra
docker compose up -d postgres
```

**2. Execute a aplicação** pela IDE ou via terminal:

```bash
./gradlew bootRun
```

**3. Acesse o Swagger:**

```
http://localhost:8080/swagger-ui/index.html
```

## 🧪 Testes e qualidade

```bash
./gradlew test          # testes unitários
./gradlew build         # compila, testa e gera relatório JaCoCo
./gradlew spotlessCheck # verifica formatação (Palantir)
```

Relatórios gerados em `build/reports/tests/test/` e `build/reports/jacoco/test/html/`.

### Recuperação de senha (e-mail)

Configure as variáveis de ambiente de SMTP antes de usar `POST /hf/auth/forgot-password`:

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `MAIL_HOST` | Servidor SMTP | `localhost` |
| `MAIL_PORT` | Porta SMTP | `1025` |
| `MAIL_USERNAME` | Usuário SMTP | — |
| `MAIL_PASSWORD` | Senha SMTP | — |
| `MAIL_FROM` | Remetente | `noreply@hydroflow.com` |

Em desenvolvimento local, [Mailpit](https://mailpit.axllent.org/) na porta 1025 captura os e-mails sem envio real.

A CI (GitHub Actions) executa Spotless, build e testes com PostgreSQL 15 na porta **5433**.

## 🗄️ Limpeza do Banco

Para resetar o banco de dados:

```bash
cd infra
./clear-all-data.sh
```

> ⚠️ Este script apaga todos os dados e recria o banco do zero.