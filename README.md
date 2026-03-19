# 💧 Hydro Flow

Sistema de gestão de abastecimento hídrico desenvolvido para auxiliar no controle e planejamento
da distribuição de água para famílias em regiões de escassez.

## 📋 Sobre o Projeto

O Hydro Flow permite o gerenciamento de:

- **Famílias e membros** — cadastro de famílias, seus integrantes e localização geográfica
- **Cisternas** — controle da capacidade de armazenamento e presença de sistema de calhas
- **Entregas de água** — registro de entregas realizadas com data e volume em litros
- **Dados pluviométricos** — registro mensal de precipitação (mm) para planejamento
- **Configurações do sistema** — consumo diário de água e coeficiente de eficiência das calhas

## 🛠️ Tecnologias

- **Java 25** com **Spring Boot 4.0**
- **Spring Data JPA** + **PostgreSQL**
- **Spring Security**
- **Liquibase** para versionamento do banco de dados
- **Docker Compose** para infraestrutura local

## 🚀 Primeira Execução

**1. Suba o banco de dados:**
```bash
cd infra
docker compose up -d
```

**2. Execute a aplicação** pela sua IDE ou via terminal:
```bash
./gradlew bootRun
```

**3. Acesse o Swagger:**
```
http://localhost:8080/swagger-ui/index.html
```

## 🗄️ Limpeza do Banco

Caso queira resetar o banco de dados:
```bash
cd infra
./clear-all-data.sh
```

> ⚠️ Este script apaga todos os dados e recria o banco do zero.