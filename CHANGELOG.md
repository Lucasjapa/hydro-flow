# Changelog — hydro-flow

Texto pensado para **comunicação com stakeholders e equipes não desenvolvedoras**. Detalhes técnicos (commits, classes) estão em [`RELEASES.md`](RELEASES.md).

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
