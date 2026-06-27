# Changelog — hydro-flow

Texto pensado para **comunicação com stakeholders e equipes não desenvolvedoras**. Detalhes técnicos (commits, classes) estão em [`RELEASES.md`](RELEASES.md).

---

## [1.2.0] — 27 de junho de 2026

### O que mudou para o usuário

- **Recuperação de senha por e-mail:** quem esqueceu a senha pode solicitar um **código de 6 dígitos** enviado ao e-mail cadastrado e, em seguida, definir uma nova senha — sem precisar estar logado.
- **Gestão de cargo do usuário:** administradores e gestores de usuários podem **alterar o cargo** de uma pessoa em endpoint dedicado (`/hf/user-management/users/{id}/role`).
- **Remoção de usuário:** apenas **administradores** podem excluir contas pela API administrativa.
- **Famílias ativas e inativas:** é possível **desativar ou reativar** uma família; famílias inativas **não entram** no cálculo automático diário de consumo de água das cisternas.
- **Proteção do último administrador:** o sistema **impede remover a permissão ADMIN** de um cargo se ele for o **único** que a possui — evitando ficar sem nenhum perfil administrativo.
- **E-mail único no cadastro:** não é mais possível **criar dois usuários com o mesmo e-mail**; a API retorna erro claro quando o endereço já está em uso.

### Atenção (mudança que exige ação)

- Telas de **“esqueci minha senha”** devem chamar `POST /hf/auth/forgot-password` e `PATCH /hf/auth/reset-password` (token numérico de 6 dígitos, válido por 1 hora).
- **Alteração de cargo** saiu de fluxos genéricos de usuário e ficou em `/hf/user-management/users/{id}/role` (admin ou gestor de usuários).
- **Ativar/desativar família** exige perfil admin, gestor de família ou gestor de usuários (`PATCH /hf/families/{id}/activate` e `/deactivate`).
- Ambientes que usam recuperação de senha precisam de **servidor SMTP** configurado (`MAIL_*` no `.env` ou variáveis de ambiente).
- Formulários de **cadastro de usuário** devem tratar resposta **422** quando o e-mail já existir.

---

## [1.1.0] — 5 de junho de 2026

### O que mudou para o usuário

- **Painel administrativo de acesso:** gestores podem **consultar usuários**, **criar e editar cargos**, **associar permissões** e **excluir cargos** que não tenham pessoas vinculadas — tudo pela API, pronta para alimentar telas de administração.
- **Troca de senha em fluxos separados:** quem já usa o sistema troca a senha em um endpoint dedicado (com validação da senha atual); quem está no **primeiro acesso** continua com fluxo próprio, sem exigir login completo antes da troca.
- **Ambiente completo no Docker:** backend, banco de dados e **frontend web** sobem juntos com um único comando; o navegador acessa o painel em **http://localhost** e a API fica disponível por trás do mesmo endereço.
- **Mensagens de erro mais previsíveis:** regras de negócio violadas (por exemplo, excluir cargo em uso) e recursos inexistentes passam a retornar códigos e textos padronizados, facilitando tratamento no frontend.
- **Consumo mínimo de água:** o sistema **não aceita mais consumo diário zero** nas configurações — valor inválido é rejeitado na API e no banco, evitando previsões de “dias restantes” distorcidas.

### Atenção (mudança que exige ação)

- Telas ou integrações que **alteravam senha ou cargo** no mesmo formulário de edição de usuário devem usar os **endpoints específicos** (`/hf/auth/update-password` para senha; gestão de cargos em `/hf/user-management`).
- O **token de login** passa a carregar **identificador do usuário e lista de permissões**; clientes que decodificam o JWT para autorização local devem alinhar-se ao novo formato.
- **Configurações com consumo diário igual a zero** deixam de ser aceitas — revisar valores já cadastrados antes de atualizar.
- O **administrador padrão** criado na instalação **não exige mais troca de senha no primeiro login**; ambientes que dependiam desse bloqueio inicial devem ajustar o fluxo de onboarding.

---

## [1.0.0] — 19 de abril de 2026

### O que mudou para o usuário

- **Contas com níveis de acesso:** o sistema passa a separar claramente quem pode só consultar, quem pode cadastrar entregas ou famílias, quem gerencia usuários e quem tem perfil de administrador.
- **Primeiro acesso mais seguro:** na primeira vez que a pessoa entra com login e senha, o sistema pode **pedir para trocar a senha antes de liberar o uso normal** — a tela de login precisa mostrar essa mensagem e levar ao fluxo de troca de senha.
- **Quem pode mudar o “cargo” de outra pessoa** fica restrito a perfis autorizados; isso evita que qualquer usuário eleve privilégios sozinho.

### Atenção (mudança que exige ação)

- Quem integra sistemas externos ao login deve tratar o caso em que o **acesso é barrado até a troca de senha** (não é falha de senha comum — é regra de primeiro acesso).
- Cadastro e edição de usuários passam a exigir informação de **perfil/cargo** onde aplicável; sistemas legados que montavam formulário sem isso precisam ser atualizados.

---

## [0.5.0] — 30 de março a 18 de abril de 2026

### O que mudou para o usuário

- **Aplicação web no navegador** pode conversar com a API sem bloqueios típicos de origem (CORS), facilitando o uso do painel em outro endereço.
- **Menos telas e campos obsoletos:** saem do produto o cadastro de **chuva mensal** e informações de **calha** ligadas à família, para reduzir complexidade e focar no que é usado no dia a dia.
- **Mais confiabilidade por trás dos panos:** aumenta a cobertura de testes automáticos nas regras de família, configurações e entregas de água.

### Atenção (mudança que exige ação)

- Se alguém ainda usava **relatórios ou telas de chuva mensal**, esses fluxos **deixam de existir** — migrar dados ou processos para outra ferramenta, se necessário.
- Formulários de família que tinham **dados de calha** devem ser simplificados; esses campos **não existem mais** na API.

---

## [0.4.0] — 22 de março de 2026

### O que mudou para o usuário

- **Login com “cartão digital” (token):** depois de entrar com e-mail e senha, o sistema devolve um **token** que deve ser reutilizado nas próximas consultas — padrão comum em aplicativos seguros.
- **Uma família pode ter várias cisternas**, e o sistema passa a **distribuir água e consumo** entre elas de forma coerente, inclusive nas entregas.

### Atenção (mudança que exige ação)

- Qualquer integração que **não enviava login** nas chamadas precisa ser atualizada para **enviar o token** nas requisições.
- Telas ou integrações que tratavam **apenas uma cisterna por família** precisam passar a suportar **lista de cisternas** (cadastro, visualização e relatórios).

---

## [0.3.0] — 17 a 19 de março de 2026

### O que mudou para o usuário

- **Registro de chuva por mês** (na época em que o recurso existia) e **busca de famílias** com filtros e listas paginadas.
- **Entregas de água** registradas pelo sistema, com **atualização automática do nível** das cisternas conforme o consumo diário.
- **Sinalização de urgência:** famílias podem aparecer em situação normal, baixa ou urgente, com **indicação de quantos dias a água tende a durar** e previsão associada.

---

## [0.2.0] — 14 de março de 2026

### O que mudou para o usuário

- **Parâmetros gerais do sistema** (como consumo de referência) passam a ser consultados e alterados pela API, com documentação para equipe técnica.
- **Cadastro e atualização de famílias** com membros, preparando o terreno para gestão comunitária de água.

---

## [0.1.0] — 13 de março de 2026

### O que mudou para o usuário

- **Primeira versão estrutural:** base de dados e aplicação preparadas para cadastrar **famílias, membros, entregas e configurações**, com ambiente padronizado para desenvolvimento e evolução contínua.

---

[//]: # ()
[//]: # (## Próximas entregas &#40;visão de produto&#41;)

[//]: # ()
[//]: # (- **Versão 1.1 &#40;ideia&#41;:** melhorias que **não obrigam** quem já integrou na 1.0 a reescrever tudo — por exemplo, mais relatórios ou telas de apoio usando a mesma API.)

[//]: # (- **Versão 2.0 &#40;ideia&#41;:** reservada para quando houver **mudança grande de contrato** &#40;avisada com antecedência&#41;, exigindo alinhamento com todos os clientes da API.)

[//]: # ()
[//]: # (*&#40;Atualize este bloco quando o roadmap for decidido em reunião de produto.&#41;*)
