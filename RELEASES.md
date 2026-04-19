# Notas de release — hydro-flow

Documento derivado do histórico de commits (`git log`). As versões abaixo são **sugestões de agrupamento** para comunicação de release; ajuste números ou datas conforme a política do projeto.

**Datas** vêm das datas de **autor** dos commits no repositório (auditoria). O **changelog em linguagem acessível** está em [`CHANGELOG.md`](CHANGELOG.md).

---

## Release 0.1.0 — Fundação do domínio e infraestrutura

**Período:** 13 de março de 2026  
**Foco:** projeto inicial, modelo de dados, migrações e ambiente de desenvolvimento.

| Commit   | Resumo |
|----------|--------|
| `97b7795` | Initial commit |
| `9a5319f` | Entidades JPA (User, Family, Member, WaterDelivery, MonthlyRainfall, SystemSettings), Liquibase, Docker Compose (PostgreSQL), Spotless, `FamilyRepository` |

**Destaques**

- Schema inicial versionado com Liquibase.
- Base para gestão de água no domínio (família, membros, entregas, configurações).

---

## Release 0.2.0 — API de configurações e famílias

**Período:** 14 de março de 2026  
**Foco:** primeiros endpoints REST e CRUD operacional.

| Commit   | Resumo |
|----------|--------|
| `4a96d37` | CRUD de `SystemSettings`, repositórios adicionais, Swagger/OpenAPI, seed de configurações, README |
| `97cc90c` | CRUD de família (`FamilyController`/`FamilyService`), DTOs com validação, campos de calha na família, rotas sob `/hf/*`, respostas OpenAPI padronizadas |

**Destaques**

- Configurações globais persistidas e expostas via API.
- Criação e atualização de famílias com membros.

---

## Release 0.3.0 — Chuva mensal, busca de famílias e hidráulica

**Período:** 17–19 de março de 2026  
**Foco:** dados pluviométricos, evolução da API de famílias, cisternas, entregas e agendamento.

| Commit   | Resumo |
|----------|--------|
| `2a395bd` | CRUD de chuva mensal (`MonthlyRainfall`), paginação e filtro por nome em famílias, rotas REST em plural (`/hf/families`), banner e versão no `application.yml` |
| `85d3cdb` | `WaterDeliveryController`/Service, `CisternScheduler` (consumo diário), nível de cisterna em `Family`, `@EnableScheduling`, Swagger em DTOs |
| `df23ed5` | Status da família (NORMAL / LOW / URGENT), métricas no `FamilyDTO` (consumo, dias restantes, próxima entrega), integração chuva → nível de cisterna, `SecurityConfig`, filtros por status e entregas por família |

**Destaques**

- Linha do tempo “hidráulica”: entregas, níveis, previsões e alertas de status.
- Segurança básica liberando endpoints conforme evolução do produto.

---

## Release 0.4.0 — Autenticação JWT e múltiplas cisternas

**Data de referência:** 22 de março de 2026  
**Foco:** segurança stateless e modelo hidráulico mais realista (várias cisternas por família).

| Commit   | Resumo |
|----------|--------|
| `f859ef2` | Spring Security + JWT, usuários, refatoração 1:N família ↔ cisternas, distribuição de consumo/entrega entre cisternas, ordenação e filtros no `FamilyController`, Bearer no Swagger |

**Destaques**

- API passa a exigir autenticação na maior parte dos recursos (conforme configuração vigente).
- Suporte a múltiplas cisternas alinhado às regras de negócio de volume.

### ⚠️ Breaking changes

- **Autenticação JWT:** integrações e scripts que chamavam a API sem token passam a receber **401** nas rotas protegidas; é necessário fluxo de login e envio do **Bearer** nas requisições.
- **Modelo família ↔ cisternas (1:N):** clientes que serializavam/deserializavam família com **uma única** cisterna embutida precisam migrar para **lista de cisternas** (payload e contratos de criação/atualização de família e entregas).

---

## Release 0.5.0 — CORS, simplificação do modelo e testes

**Período:** 30 de março – 18 de abril de 2026  
**Foco:** integração com frontend, remoção de escopo não essencial e base de qualidade automatizada.

| Commit   | Resumo |
|----------|--------|
| `9d24758` / `d43bb2d` | CORS para o frontend (incluindo merge do PR #1) |
| `2a62a1f` | Remoção de índices de chuva mensal (`MonthlyRainfall`) e campos de calha em `Family`/`FamilyDTO`; Liquibase e serviços alinhados |
| `02ad9f6` | `UserServiceTest`, plugin test-logger, JVM args para Mockito, `bootRun` dependente de `test` |
| `4f3895a` | Testes unitários para `FamilyService`, `SystemSettingsService`, `WaterDeliveryService`; ajuste em `Cistern` para testes |

**Destaques**

- Contrato HTTP mais previsível para SPA (CORS).
- Domínio enxuto: menos superfície de manutenção sem chuva/calha.
- Regressão coberta por testes nos serviços centrais.

### ⚠️ Breaking changes

- **Remoção do módulo de chuva mensal:** endpoints, tabelas e tipos relacionados a **índices pluviométricos** deixam de existir; ajustar frontends, ETLs e documentação que dependiam desse recurso.
- **Remoção de campos de calha na família:** integrações que enviavam ou exibiam **área de calha** ou **coeficiente de eficiência** na família devem remover esses campos dos payloads e telas.

---

## Release 1.0.0 — RBAC, primeiro acesso e autorização refinada

**Data de referência:** 19 de abril de 2026  
**Foco:** governança de identidade e permissões, alinhada a produção.

| Commit   | Resumo |
|----------|--------|
| `d52c3c2` | Entidades `Role` e `Permission`, fluxo de primeiro acesso no login (`AuthController`), `@PreAuthorize` em `UserController`, Liquibase (tabelas de permissões, seed admin), `UserService`/`UserDTO` com vínculo de cargo |
| `a1e84bb` | Anotações customizadas de segurança, `AuthorizationService` (ex.: admin ou dono do recurso), regras de alteração de cargo no `UserService`, constantes de permissões, `UserServiceTest` alinhado aos fluxos |

**Destaques**

- Modelo RBAC explícito no banco e na API.
- Primeiro acesso com troca de senha obrigatória antes do token pleno.
- Autorização centralizada e testada onde a regra é mais sensível.

### ⚠️ Breaking changes

- **Login:** usuários com **primeiro acesso** recebem **403** e corpo orientando troca de senha, em vez de token imediato; o fluxo de UI deve tratar esse caso (ex.: tela de redefinição).
- **Usuários:** criação e atualização passam a considerar **cargo (role)** e **permissões**; apenas perfis autorizados alteram cargos alheios (regra reforçada no serviço e nas anotações).

---

[//]: # (## Próximas versões &#40;planejado&#41;)

[//]: # ()
[//]: # (Trecho **não amarrado a commits** — alinhar com o backlog do produto antes de publicar como compromisso.)

[//]: # ()
[//]: # (### 1.1.0 &#40;curto prazo&#41;)

[//]: # ()
[//]: # (- Evoluções incrementais da API sob contrato estável &#40;1.0.0&#41;.)

[//]: # (- Possíveis melhorias: auditoria de ações sensíveis, endpoints de consulta de perfis/permissões para o painel administrativo, endurecimento de validações já existentes.)

[//]: # ()
[//]: # (### 2.0.0 &#40;médio / longo prazo&#41;)

[//]: # ()
[//]: # (- Reservado para **mudanças de contrato maiores** &#40;novo versionamento de API, alterações profundas no modelo de família/cisterna, ou política de segurança que exija migração coordenada de todos os clientes&#41;.)

[//]: # (- Definir conteúdo quando houver proposta técnica aprovada; até lá, manter compatibilidade na série 1.x sempre que possível.)

[//]: # ()
[//]: # (---)

## Visão consolidada (uma linha por release)

| Versão   | Período / data (ref. commits) | Tema |
|----------|----------------------------------|------|
| **0.1.0** | 13 mar 2026 | Domínio + Liquibase + Docker + qualidade de código |
| **0.2.0** | 14 mar 2026 | Settings + famílias na API |
| **0.3.0** | 17–19 mar 2026 | Chuva (época), cisternas, entregas, status e métricas |
| **0.4.0** | 22 mar 2026 | JWT + multi-cisterna (**breaking**) |
| **0.5.0** | 30 mar – 18 abr 2026 | CORS + simplificação (**breaking**) + testes |
| **1.0.0** | 19 abr 2026 | Perfis, permissões, primeiro acesso (**breaking**) |

---

## Como manter este documento

1. Após cada entrega, acrescente uma seção **Release X.Y.Z** com **período ou data**, tabela de commits, **destaques** e, se couber, **⚠️ Breaking changes**.
2. Atualize em paralelo o **[`CHANGELOG.md`](CHANGELOG.md)** com a mesma versão em linguagem voltada a stakeholders e usuários.
3. Ajuste a secção **Próximas versões** quando o roadmap mudar; promova itens para releases reais ao fechar a versão.
4. Se usar tags Git, crie `v0.1.0`, `v1.0.0`, etc., apontando para o último commit do grupo correspondente.
