Módulo de Autenticação e Segurança

1. Login com Primeiro Acesso

● ID do Caso de Teste: CT-001

● Nome: Teste de Login com Primeiro Acesso

● Objetivo: Garantir que o usuário seja direcionado a redefinir sua senha no primeiro acesso ao sistema.

● Pré-condições: O usuário está cadastrado no sistema, mas ainda não realizou o primeiro acesso.

● Entradas:

1. E-mail do usuário
2. Senha temporária

● Passos para Execução:

1. Acesse a tela de login.
2. Preencha o e-mail e a senha temporária.
3. Tente entrar no sistema.

● Resultado Esperado: O sistema deve exibir uma mensagem orientando o usuário a trocar a senha antes de continuar.

● Critérios de Sucesso: O acesso inicial é bloqueado e a instrução de troca de senha é exibida.

● Critérios de Falha: O usuário consegue acessar o sistema sem alterar a senha provisória.

<br/>

2. Login com Credenciais Válidas

● ID do Caso de Teste: CT-002

● Nome: Teste de Login com Credenciais Válidas

● Objetivo: Garantir que o usuário consiga acessar o sistema informando credenciais corretas.

● Pré-condições: O usuário está cadastrado no sistema e já redefiniu sua senha inicial.

● Entradas:

1. E-mail do usuário
2. Senha do usuário

● Passos para Execução:

1. Acesse a tela de login.
2. Preencha o e-mail e a senha corretos.
3. Clique em entrar.

● Resultado Esperado: O usuário deve acessar o sistema com sucesso e receber autorização para navegação.

● Critérios de Sucesso: O acesso é liberado corretamente.

● Critérios de Falha: O acesso é negado mesmo com as credenciais corretas.

<br/>

3. Alteração de Senha do Usuário

● ID do Caso de Teste: CT-003

● Nome: Teste de Alteração de Senha do Usuário

● Objetivo: Garantir que o usuário consiga alterar sua senha no sistema com sucesso.

● Pré-condições: O usuário está logado no sistema.

● Entradas:

1. Nova senha escolhida

● Passos para Execução:

1. Acesse a área de redefinição de senha.
2. Insira a nova senha.
3. Confirme a alteração.

● Resultado Esperado: O sistema deve alterar a senha e manter o usuário autenticado.

● Critérios de Sucesso: A senha é alterada com sucesso.

● Critérios de Falha: A senha não é alterada ou o usuário é desconectado com erro.

<br/>

4. Atualização de Senha do Usuário

● ID do Caso de Teste: CT-004

● Nome: Teste de Atualização de Senha do Usuário

● Objetivo: Garantir que a atualização da senha seja realizada apenas após validar a senha anterior.

● Pré-condições: O usuário está logado no sistema.

● Entradas:

1. Senha atual
2. Nova senha

● Passos para Execução:

1. Acesse a tela de segurança da conta.
2. Insira a senha atual do perfil.
3. Insira a nova senha desejada.
4. Confirme a atualização.

● Resultado Esperado: O sistema deve validar a senha antiga, registrar a nova e confirmar a operação.

● Critérios de Sucesso: A senha atual é validada e a nova senha é salva corretamente.

● Critérios de Falha: A senha atual é rejeitada incorretamente ou a nova não é salva.

<br/>

5. Geração de Sessão de Usuário

● ID do Caso de Teste: CT-005

● Nome: Teste de Geração de Sessão de Usuário

● Objetivo: Garantir que a sessão de acesso seja criada contendo as informações corretas de identificação.

● Pré-condições: O usuário foi autenticado com sucesso no sistema.

● Entradas:

1. Dados do usuário (E-mail e Cargo)

● Passos para Execução:

1. O sistema autoriza a entrada do usuário.
2. O sistema gera o bilhete de sessão segura.
3. O sistema verifica se o e-mail está associado corretamente à sessão.

● Resultado Esperado: A sessão segura deve ser gerada possuindo o e-mail correto do usuário autenticado.

● Critérios de Sucesso: A sessão é criada e o e-mail interno confere com o usuário.

● Critérios de Falha: A sessão falha ao ser gerada ou não possui o e-mail associado.

<br/>

6. Reconhecimento de Sessão Válida

● ID do Caso de Teste: CT-006

● Nome: Teste de Reconhecimento de Sessão Válida

● Objetivo: Garantir que o sistema reconheça uma sessão ativa como válida para permitir o uso da aplicação.

● Pré-condições: O usuário possui uma sessão ativa e acabou de acessar uma página segura.

● Entradas:

1. Sessão de acesso ativa
2. E-mail do usuário

● Passos para Execução:

1. Acesse uma funcionalidade protegida do sistema.
2. O sistema verifica a validade da sessão atual.

● Resultado Esperado: O sistema deve reconhecer a sessão como autêntica e permitir a navegação.

● Critérios de Sucesso: A sessão é aceita e o acesso à página é permitido.

● Critérios de Falha: A sessão válida é rejeitada pelo sistema.

<br/>

7. Rejeição de Sessão de Terceiros

● ID do Caso de Teste: CT-007

● Nome: Teste de Rejeição de Sessão de Terceiros

● Objetivo: Garantir que uma sessão pertencente a outra pessoa seja rejeitada ao tentar acessar dados privados.

● Pré-condições: O sistema possui registros de múltiplos usuários com sessões diferentes.

● Entradas:

1. Sessão de acesso de outro usuário
2. Dados do usuário atual

● Passos para Execução:

1. Acesse a aplicação utilizando uma sessão que pertence a um e-mail diferente.
2. O sistema cruza as informações da sessão com o e-mail local.

● Resultado Esperado: O sistema deve identificar a divergência e invalidar o acesso.

● Critérios de Sucesso: O acesso é negado corretamente por inconsistência de titularidade.

● Critérios de Falha: O sistema permite que um usuário use a sessão de outra pessoa.

<br/>

8. Expiração de Sessão

● ID do Caso de Teste: CT-008

● Nome: Teste de Expiração de Sessão

● Objetivo: Garantir que uma sessão com tempo esgotado não permita acesso ao sistema.

● Pré-condições: O usuário ficou inativo tempo suficiente para a sessão expirar.

● Entradas:

1. Sessão de acesso expirada

● Passos para Execução:

1. Tente realizar alguma ação no sistema utilizando a sessão expirada.
2. O sistema analisa o tempo limite.

● Resultado Esperado: O sistema deve bloquear a ação e informar que a sessão perdeu a validade.

● Critérios de Sucesso: O acesso é impedido imediatamente informando o esgotamento do tempo.

● Critérios de Falha: O sistema aceita a sessão com tempo esgotado como válida.

<br/>

Módulo de Gestão de Usuários e Cargos

9. Cadastro de Novo Usuário

● ID do Caso de Teste: CT-009

● Nome: Teste de Cadastro de Novo Usuário

● Objetivo: Garantir que um novo usuário possa ser cadastrado no sistema corretamente.

● Pré-condições: O sistema está disponível e o e-mail do usuário ainda não está cadastrado.

● Entradas:

1. Nome do usuário
2. E-mail do usuário
3. Senha
4. Cargo associado

● Passos para Execução:

1. Acesse a tela de criação de usuários.
2. Preencha as informações necessárias.
3. Confirme o cadastro.

● Resultado Esperado: O usuário deve ser cadastrado no sistema com sucesso.

● Critérios de Sucesso: O usuário é criado e o sistema retorna seus dados confirmando a inclusão.

● Critérios de Falha: O usuário não é salvo no sistema ou ocorre erro no processo.

<br/>

10. Atualização de Dados do Usuário

● ID do Caso de Teste: CT-010

● Nome: Teste de Atualização de Dados do Usuário

● Objetivo: Garantir que as informações de um usuário existente possam ser atualizadas.

● Pré-condições: O usuário já está cadastrado no sistema.

● Entradas:

1. Nome atualizado
2. E-mail atualizado

● Passos para Execução:

1. Acesse o perfil do usuário desejado.
2. Altere o nome e o e-mail.
3. Salve as modificações.

● Resultado Esperado: Os dados do usuário devem ser atualizados e refletidos no sistema.

● Critérios de Sucesso: O perfil do usuário exibe as informações modificadas com sucesso.

● Critérios de Falha: As alterações não são salvas ou os dados permanecem antigos.

<br/>

11. Listagem de Todos os Usuários

● ID do Caso de Teste: CT-011

● Nome: Teste de Listagem de Todos os Usuários

● Objetivo: Garantir que o painel de gestão consiga exibir todos os usuários cadastrados.

● Pré-condições: O sistema já possui usuários cadastrados.

● Entradas:

1. Nenhuma

● Passos para Execução:

1. Acesse o painel de gerenciamento de usuários.
2. Solicite a exibição da lista de cadastrados.

● Resultado Esperado: A lista com todos os usuários deve ser exibida corretamente na tela.

● Critérios de Sucesso: Os usuários cadastrados são mostrados de forma completa.

● Critérios de Falha: A lista aparece vazia incorretamente ou não é carregada.

<br/>

12. Listagem de Cargos do Sistema

● ID do Caso de Teste: CT-012

● Nome: Teste de Listagem de Cargos do Sistema

● Objetivo: Garantir que o sistema consiga listar os cargos administrativos disponíveis.

● Pré-condições: Os cargos já estão previamente cadastrados no sistema.

● Entradas:

1. Nenhuma

● Passos para Execução:

1. Acesse o menu de configurações de cargos.
2. Visualize a listagem disponível.

● Resultado Esperado: O sistema deve exibir todos os cargos disponíveis (como Administrador, Operador, etc).

● Critérios de Sucesso: A listagem exibe corretamente os perfis cadastrados.

● Critérios de Falha: Os cargos não são exibidos na tela.

<br/>

13. Listagem de Permissões de Acesso

● ID do Caso de Teste: CT-013

● Nome: Teste de Listagem de Permissões de Acesso

● Objetivo: Garantir que o administrador possa visualizar todas as permissões concedíveis a um cargo.

● Pré-condições: As permissões de sistema já estão definidas na base.

● Entradas:

1. Nenhuma

● Passos para Execução:

1. Acesse a área de edição de permissões.
2. Consulte a lista de permissões disponíveis.

● Resultado Esperado: O sistema deve mostrar as opções de permissão existentes no painel.

● Critérios de Sucesso: As permissões carregam de forma legível na interface.

● Critérios de Falha: Ocorre falha ao tentar visualizar as permissões.

<br/>

14. Cadastro de Novo Cargo

● ID do Caso de Teste: CT-014

● Nome: Teste de Cadastro de Novo Cargo

● Objetivo: Garantir que seja possível criar novos perfis de acesso no sistema.

● Pré-condições: O usuário atual possui acesso de gestão.

● Entradas:

1. Nome do novo cargo
2. Lista de permissões escolhidas

● Passos para Execução:

1. Acesse a área de criação de cargos.
2. Preencha o nome do cargo.
3. Selecione as permissões.
4. Salve o cadastro.

● Resultado Esperado: O novo cargo deve ser cadastrado com suas respectivas permissões.

● Critérios de Sucesso: O cargo é incluído com sucesso no catálogo do sistema.

● Critérios de Falha: O cargo não é criado ou as permissões não são atribuídas.

<br/>

15. Atualização de Perfil de Cargo

● ID do Caso de Teste: CT-015

● Nome: Teste de Atualização de Perfil de Cargo

● Objetivo: Garantir que as permissões e o nome de um cargo existente possam ser modificados.

● Pré-condições: O cargo já está cadastrado no sistema.

● Entradas:

1. Novo nome para o cargo
2. Novas permissões

● Passos para Execução:

1. Acesse os detalhes de um cargo existente.
2. Altere seu nome e permissões.
3. Salve as modificações.

● Resultado Esperado: O cargo deve ser atualizado com as novas configurações.

● Critérios de Sucesso: As modificações são refletidas e o cargo é atualizado.

● Critérios de Falha: O cargo mantém as configurações antigas após a tentativa de salvamento.

<br/>

16. Exclusão de Cargo Sem Vínculos

● ID do Caso de Teste: CT-016

● Nome: Teste de Exclusão de Cargo Sem Vínculos

● Objetivo: Garantir que um cargo possa ser excluído caso nenhum usuário o esteja utilizando.

● Pré-condições: O cargo existe e não há usuários cadastrados com ele.

● Entradas:

1. Cargo a ser excluído

● Passos para Execução:

1. Acesse a lista de cargos.
2. Selecione o cargo sem usuários.
3. Solicite a exclusão.

● Resultado Esperado: O cargo deve ser removido permanentemente do sistema.

● Critérios de Sucesso: A remoção do cargo é concluída com sucesso.

● Critérios de Falha: O sistema impede a exclusão indevidamente.

<br/>

17. Acesso à Lista de Perfis

● ID do Caso de Teste: CT-017

● Nome: Teste de Acesso à Lista de Perfis

● Objetivo: Garantir que as informações dos cargos sejam empacotadas e entregues corretamente à interface.

● Pré-condições: Existem cargos disponíveis cadastrados.

● Entradas:

1. Nenhuma

● Passos para Execução:

1. Abra o menu de perfis.
2. Aguarde o carregamento dos dados.

● Resultado Esperado: As informações dos perfis devem ser formatadas e exibidas adequadamente.

● Critérios de Sucesso: A lista é lida e apresentada de maneira correta no visor.

● Critérios de Falha: Os dados chegam desconfigurados ou corrompidos.

<br/>

18. Consulta de Detalhes do Cargo

● ID do Caso de Teste: CT-018

● Nome: Teste de Consulta de Detalhes do Cargo

● Objetivo: Garantir que o sistema encontre e apresente os detalhes de um cargo específico.

● Pré-condições: O cargo consultado existe no catálogo.

● Entradas:

1. Identificador do cargo desejado

● Passos para Execução:

1. Selecione um cargo na lista.
2. Solicite a visualização dos seus detalhes.

● Resultado Esperado: As informações exatas do cargo devem ser abertas na tela.

● Critérios de Sucesso: O cargo é localizado e os seus dados são apresentados.

● Critérios de Falha: O sistema não consegue abrir as informações do cargo.

<br/>

19. Consulta de Cargo Inexistente

● ID do Caso de Teste: CT-019

● Nome: Teste de Consulta de Cargo Inexistente

● Objetivo: Garantir que a busca por um cargo que não existe informe corretamente o problema.

● Pré-condições: O cargo procurado não está cadastrado no sistema.

● Entradas:

1. Identificador de um cargo apagado ou não cadastrado

● Passos para Execução:

1. Tente acessar diretamente as informações desse cargo ausente.

● Resultado Esperado: O sistema deve informar que o cargo procurado não foi encontrado.

● Critérios de Sucesso: Uma mensagem de erro indicando a ausência do registro é exibida.

● Critérios de Falha: O sistema carrega uma tela vazia em vez de avisar o usuário.

<br/>

20. Associação de Permissões a um Cargo

● ID do Caso de Teste: CT-020

● Nome: Teste de Associação de Permissões a um Cargo

● Objetivo: Garantir que a escolha de permissões seja registrada corretamente no banco.

● Pré-condições: As permissões escolhidas existem e estão disponíveis.

● Entradas:

1. Nome do cargo
2. Permissões ativas selecionadas

● Passos para Execução:

1. Crie um cargo.
2. Marque as permissões desejadas.
3. Finalize a criação.

● Resultado Esperado: O cargo deve ser armazenado já possuindo o elo com as permissões escolhidas.

● Critérios de Sucesso: O perfil de acesso guarda o nível de autorização corretamente.

● Critérios de Falha: O cargo é salvo, mas as permissões são esquecidas ou ignoradas.

<br/>

21. Bloqueio de Cargo com Permissão Inválida

● ID do Caso de Teste: CT-021

● Nome: Teste de Bloqueio de Cargo com Permissão Inválida

● Objetivo: Garantir que o sistema não salve cargos que possuam permissões incorretas ou excluídas.

● Pré-condições: Uma das permissões informadas já não existe mais no sistema.

● Entradas:

1. Dados do cargo
2. Permissão inexistente

● Passos para Execução:

1. Tente salvar o cargo com a permissão defeituosa.
2. Aguarde a validação.

● Resultado Esperado: O sistema deve bloquear a operação e informar que a permissão é inválida.

● Critérios de Sucesso: O bloqueio do cadastro é executado corretamente.

● Critérios de Falha: O sistema ignora o erro e salva o cargo com inconsistências.

<br/>

22. Exclusão Interna de Cargo Livre

● ID do Caso de Teste: CT-022

● Nome: Teste de Exclusão Interna de Cargo Livre

● Objetivo: Garantir que a rotina interna apague definitivamente os cargos vazios.

● Pré-condições: O cargo não está associado a ninguém.

● Entradas:

1. Cargo alvo

● Passos para Execução:

1. Comande a exclusão do cargo.

● Resultado Esperado: A exclusão deve ocorrer sem impedimentos técnicos.

● Critérios de Sucesso: Os dados do cargo são deletados da base definitivamente.

● Critérios de Falha: A rotina interna falha ou ignora a ordem de deleção.

<br/>

23. Prevenção de Exclusão de Cargo em Uso

● ID do Caso de Teste: CT-023

● Nome: Teste de Prevenção de Exclusão de Cargo em Uso

● Objetivo: Garantir que o sistema proíba a exclusão de um cargo que possui usuários dependentes.

● Pré-condições: Existem usuários que utilizam o cargo que será excluído.

● Entradas:

1. Cargo ocupado

● Passos para Execução:

1. Tente remover o cargo que está em uso pela equipe.

● Resultado Esperado: O sistema deve bloquear a exclusão e emitir um aviso alertando sobre o vínculo.

● Critérios de Sucesso: A exclusão é interceptada, protegendo os acessos dos usuários atuais.

● Critérios de Falha: O cargo é deletado e os usuários ficam sem permissões de acesso.

<br/>

24. Edição Interna de Perfil do Usuário

● ID do Caso de Teste: CT-024

● Nome: Teste de Edição Interna de Perfil do Usuário

● Objetivo: Garantir que os novos dados do perfil sejam efetivados na base.

● Pré-condições: O usuário acessou seu perfil para edição.

● Entradas:

1. Novas informações pessoais

● Passos para Execução:

1. Submeta o formulário de edição de perfil.
2. Aguarde a persistência.

● Resultado Esperado: As informações pessoais devem ser permanentemente guardadas.

● Critérios de Sucesso: A edição é aprovada e salva no banco de dados com sucesso.

● Critérios de Falha: As informações são aceitas na tela mas não são salvas no banco.

<br/>

25. Edição de Usuário Não Encontrado

● ID do Caso de Teste: CT-025

● Nome: Teste de Edição de Usuário Não Encontrado

● Objetivo: Garantir que a tentativa de editar um perfil deletado ou ausente resulte em erro claro.

● Pré-condições: O usuário informado não está cadastrado no sistema.

● Entradas:

1. Dados editados de um usuário fantasma

● Passos para Execução:

1. Tente aplicar alterações de perfil para esse usuário.

● Resultado Esperado: O sistema deve rejeitar as mudanças alegando que o usuário não existe.

● Critérios de Sucesso: Ação é rejeitada com segurança pelo sistema.

● Critérios de Falha: O erro trava o sistema sem emitir avisos claros.

<br/>

26. Recuperação Integrada de Usuários

● ID do Caso de Teste: CT-026

● Nome: Teste de Recuperação Integrada de Usuários

● Objetivo: Garantir que a lista de usuários contenha as informações estruturadas de toda a equipe.

● Pré-condições: O sistema possui pessoas cadastradas.

● Entradas:

1. Nenhuma

● Passos para Execução:

1. Acesse a lista da equipe.
2. Analise as informações que aparecem.

● Resultado Esperado: A lista deve entregar dados completos, como o nome e o cargo de cada um.

● Critérios de Sucesso: Os dados chegam limpos e corretos para exibição no painel.

● Critérios de Falha: Ocorre falha ao buscar as informações ou exibir os usuários.

<br/>

27. Conclusão do Cadastro de Usuário

● ID do Caso de Teste: CT-027

● Nome: Teste de Conclusão do Cadastro de Usuário

● Objetivo: Garantir que os dados inseridos, incluindo a senha criptografada, sejam salvos em definitivo.

● Pré-condições: Os dados de novo cadastro foram preenchidos corretamente.

● Entradas:

1. Informações de novo usuário

● Passos para Execução:

1. Finalize a inclusão da pessoa.
2. Consulte a base de dados em seguida.

● Resultado Esperado: O novo membro da equipe deve ser incluído de forma segura na base.

● Critérios de Sucesso: O cadastro finaliza e a pessoa passa a ter conta no sistema.

● Critérios de Falha: A rotina de segurança não criptografa a senha ou perde os dados.

<br/>

Módulo de Gestão de Famílias

28. Cadastro Completo de Família

● ID do Caso de Teste: CT-028

● Nome: Teste de Cadastro Completo de Família

● Objetivo: Garantir que seja possível cadastrar uma família informando também seus membros e cisternas.

● Pré-condições: Nenhuma família com os mesmos dados está cadastrada.

● Entradas:

1. Dados do responsável
2. Lista de membros da casa
3. Capacidade das cisternas

● Passos para Execução:

1. Acesse o formulário de inclusão de família.
2. Adicione membros e cisternas associadas.
3. Finalize o cadastro.

● Resultado Esperado: A família deve ser criada no sistema juntamente com os membros e cisternas vinculados.

● Critérios de Sucesso: A família, seus membros e cisternas são todos salvos juntos.

● Critérios de Falha: Os familiares ou as cisternas são perdidos durante o processo.

<br/>

29. Atualização de Dados Familiares

● ID do Caso de Teste: CT-029

● Nome: Teste de Atualização de Dados Familiares

● Objetivo: Garantir que as informações de uma família já cadastrada possam ser corrigidas.

● Pré-condições: A família já está cadastrada no sistema.

● Entradas:

1. Dados alterados da família

● Passos para Execução:

1. Acesse a edição da família.
2. Modifique as informações desejadas.
3. Salve o formulário.

● Resultado Esperado: A família deve ter suas informações atualizadas.

● Critérios de Sucesso: O perfil da família passa a exibir os dados novos com sucesso.

● Critérios de Falha: A alteração é rejeitada incorretamente.

<br/>

30. Consulta de Cadastro de Família

● ID do Caso de Teste: CT-030

● Nome: Teste de Consulta de Cadastro de Família

● Objetivo: Garantir que o perfil completo de uma família possa ser visualizado individualmente.

● Pré-condições: A família possui cadastro existente.

● Entradas:

1. Código da família

● Passos para Execução:

1. Busque pela família desejada.
2. Clique para ver os detalhes completos.

● Resultado Esperado: O perfil detalhado da família deve ser exibido com todas as suas características.

● Critérios de Sucesso: As informações são resgatadas e mostradas corretamente.

● Critérios de Falha: O perfil da família não carrega.

<br/>

31. Navegação na Lista de Famílias

● ID do Caso de Teste: CT-031

● Nome: Teste de Navegação na Lista de Famílias

● Objetivo: Garantir que a lista de famílias suporte paginação para facilitar a visualização de centenas de registros.

● Pré-condições: O sistema possui diversas famílias cadastradas.

● Entradas:

1. Página atual e tamanho da lista

● Passos para Execução:

1. Acesse a lista de cadastros sociais.
2. Mude para a próxima página de resultados.

● Resultado Esperado: A lista deve exibir apenas a quantidade de famílias estipulada por página.

● Critérios de Sucesso: A paginação ocorre de forma suave e carrega os dados adequadamente.

● Critérios de Falha: Todos os registros carregam de uma vez ou a página vem vazia.

<br/>

32. Busca de Famílias por Nome

● ID do Caso de Teste: CT-032

● Nome: Teste de Busca de Famílias por Nome

● Objetivo: Garantir que o filtro de texto consiga localizar famílias através do nome do responsável.

● Pré-condições: Existem famílias cadastradas com nomes variados.

● Entradas:

1. Nome a ser pesquisado (ex: 'Maria')

● Passos para Execução:

1. Digite o nome na barra de busca.
2. Confirme a pesquisa.

● Resultado Esperado: Apenas as famílias cujo nome coincide com a busca devem aparecer.

● Critérios de Sucesso: Os resultados exibidos estão corretos em relação ao termo pesquisado.

● Critérios de Falha: A pesquisa traz resultados aleatórios ou não funciona.

<br/>

33. Filtragem de Famílias por Nível de Urgência

● ID do Caso de Teste: CT-033

● Nome: Teste de Filtragem de Famílias por Nível de Urgência

● Objetivo: Garantir que seja possível visualizar apenas famílias que estejam em um status específico, como necessidade urgente.

● Pré-condições: Algumas famílias estão marcadas com urgência hídrica.

● Entradas:

1. Status de urgência selecionado

● Passos para Execução:

1. Abra o menu de filtros de situação.
2. Selecione a situação de emergência.

● Resultado Esperado: O sistema deve listar exclusivamente as famílias enquadradas nesse status.

● Critérios de Sucesso: O filtro age corretamente limitando a lista ao status escolhido.

● Critérios de Falha: O sistema ignora o filtro e mostra famílias com status diferentes.

<br/>

34. Acesso Interno aos Dados da Família

● ID do Caso de Teste: CT-034

● Nome: Teste de Acesso Interno aos Dados da Família

● Objetivo: Garantir que os dados vitais da família sejam processados pelo serviço.

● Pré-condições: A família está gravada em banco.

● Entradas:

1. Código interno da família

● Passos para Execução:

1. O sistema necessita ler os dados da família para alguma operação.

● Resultado Esperado: O modelo completo da família deve ser entregue para processamento.

● Critérios de Sucesso: A leitura interna ocorre de maneira fluída.

● Critérios de Falha: O sistema acusa erro de leitura no banco.

<br/>

35. Prevenção de Acesso a Família Excluída

● ID do Caso de Teste: CT-035

● Nome: Teste de Prevenção de Acesso a Família Excluída

● Objetivo: Garantir que ações destinadas a famílias que não existem mais sejam travadas.

● Pré-condições: A família foi removida recentemente.

● Entradas:

1. Código de família inexistente

● Passos para Execução:

1. O sistema tenta buscar os dados dessa família apagada.

● Resultado Esperado: A ação deve ser imediatamente cancelada com aviso de ausência de cadastro.

● Critérios de Sucesso: A segurança de dados e integridade é mantida.

● Critérios de Falha: O sistema entra em falha crítica sem avisos coerentes.

<br/>

36. Conexão de Vínculos Familiares

● ID do Caso de Teste: CT-036

● Nome: Teste de Conexão de Vínculos Familiares

● Objetivo: Garantir que, no momento do cadastro, os parentes e as cisternas fiquem perfeitamente atrelados ao chefe de família.

● Pré-condições: O usuário finalizou de preencher os dados completos da casa.

● Entradas:

1. Grupo completo de informações familiares

● Passos para Execução:

1. O sistema processa a aprovação do novo cadastro.

● Resultado Esperado: Os vínculos entre o responsável, os moradores e os reservatórios devem ser salvos sem quebra.

● Critérios de Sucesso: As conexões do banco de dados representam a casa corretamente.

● Critérios de Falha: Ocorrem cadastros órfãos, como moradores sem residência.

<br/>

37. Renovação de Informações da Moradia

● ID do Caso de Teste: CT-037

● Nome: Teste de Renovação de Informações da Moradia

● Objetivo: Garantir que o sistema apague membros antigos e atualize os novos em caso de mudança brusca de cadastro.

● Pré-condições: A família sofreu alterações nos moradores ou instalou novas cisternas.

● Entradas:

1. Novas listas de habitantes e reservatórios

● Passos para Execução:

1. O assistente atualiza a composição da família na tela de edição.

● Resultado Esperado: O sistema deve aceitar a nova composição e atualizar os dependentes anteriores.

● Critérios de Sucesso: A residência espelha exatamente a nova configuração informada.

● Critérios de Falha: Aparecem moradores duplicados ou cisternas somadas incorretamente.

<br/>

38. Rejeição de Edição de Cadastro Inexistente

● ID do Caso de Teste: CT-038

● Nome: Teste de Rejeição de Edição de Cadastro Inexistente

● Objetivo: Garantir que o sistema impeça atualizações direcionadas a famílias que não existem.

● Pré-condições: A família não consta no sistema.

● Entradas:

1. Dados de atualização de família não registrada

● Passos para Execução:

1. O usuário tenta forçar a edição dessa família através de um link antigo.

● Resultado Esperado: O sistema bloqueia a tela e informa que o cadastro é inválido.

● Critérios de Sucesso: A tentativa de edição é devidamente barrada.

● Critérios de Falha: O sistema cria uma família acidentalmente ou quebra a tela.

<br/>

39. Exibição de Indicadores da Família

● ID do Caso de Teste: CT-039

● Nome: Teste de Exibição de Indicadores da Família

● Objetivo: Garantir que o perfil da família já mostre automaticamente a previsão de dias restantes de água.

● Pré-condições: A família possui água armazenada na cisterna e um consumo cadastrado.

● Entradas:

1. Acesso ao perfil da casa

● Passos para Execução:

1. Abra o resumo detalhado da família.

● Resultado Esperado: Além dos dados básicos, os indicadores computados (como estimativa de dias sem água) devem aparecer.

● Critérios de Sucesso: Os cálculos ocorrem instantaneamente e são exibidos na tela.

● Critérios de Falha: Os números quebram ou a estimativa aparece vazia.

<br/>

40. Formatação Correta de Páginas de Famílias

● ID do Caso de Teste: CT-040

● Nome: Teste de Formatação Correta de Páginas de Famílias

● Objetivo: Garantir que os cadernos virtuais de paginação não quebrem as informações vitais das famílias.

● Pré-condições: A lista principal está sendo carregada.

● Entradas:

1. Solicitação de visualização em massa

● Passos para Execução:

1. Carregue a página 1 do painel geral.

● Resultado Esperado: A formatação dos dados de cada família contida na lista deve estar correta e padronizada.

● Critérios de Sucesso: A lista é convertida para exibição sem perder campos críticos.

● Critérios de Falha: Faltam colunas importantes no modo lista.

<br/>

41. Pesquisa Funcional por Nomes de Chefes de Família

● ID do Caso de Teste: CT-041

● Nome: Teste de Pesquisa Funcional por Nomes de Chefes de Família

● Objetivo: Garantir que a pesquisa textualmente livre resgate as famílias mantendo o layout da tela.

● Pré-condições: A pesquisa de nome foi iniciada.

● Entradas:

1. Parte do nome da pessoa

● Passos para Execução:

1. O sistema efetua a pesquisa no servidor.

● Resultado Esperado: A página de exibição recebe a lista formatada dos nomes que bateram com a pesquisa.

● Critérios de Sucesso: O filtro visual trabalha junto com a pesquisa perfeitamente.

● Critérios de Falha: O resultado quebra a apresentação da página.

<br/>

42. Aplicação Visual de Filtro de Risco

● ID do Caso de Teste: CT-042

● Nome: Teste de Aplicação Visual de Filtro de Risco

● Objetivo: Garantir que a seleção do nível de emergência resulte numa visualização impecável.

● Pré-condições: A tela de lista está aguardando filtros.

● Entradas:

1. Grau de emergência selecionado

● Passos para Execução:

1. O sistema varre os cadastros em risco.

● Resultado Esperado: O sistema projeta na tela apenas os casos de emergência solicitados sem bagunçar a tabela.

● Critérios de Sucesso: As famílias em risco aparecem listadas coerentemente.

● Critérios de Falha: As etiquetas de risco não correspondem ao filtro.

<br/>

43. Organização Visual do Menor Volume de Água

● ID do Caso de Teste: CT-043

● Nome: Teste de Organização Visual do Menor Volume de Água

● Objetivo: Garantir que seja possível ordenar a lista para que as famílias com cisternas mais vazias apareçam primeiro.

● Pré-condições: O gestor precisa saber quem está com menos água.

● Entradas:

1. Comando de ordenação por capacidade crescente

● Passos para Execução:

1. Clique no botão de ordenar por nível de água (Menor para Maior).

● Resultado Esperado: A lista deve ser reorganizada mostrando as cisternas secas no topo.

● Critérios de Sucesso: As famílias prioritárias pela seca ficam na primeira página.

● Critérios de Falha: A ordenação não surte efeito visual.

<br/>

44. Organização Visual do Maior Volume de Água

● ID do Caso de Teste: CT-044

● Nome: Teste de Organização Visual do Maior Volume de Água

● Objetivo: Garantir que seja possível inverter a lista para visualizar quem tem os reservatórios mais cheios.

● Pré-condições: O gestor quer saber quem está seguro hidricamente.

● Entradas:

1. Comando de ordenação por capacidade decrescente

● Passos para Execução:

1. Clique no botão de ordenar por nível de água (Maior para Menor).

● Resultado Esperado: A lista deve colocar as cisternas lotadas nas primeiras posições.

● Critérios de Sucesso: A organização ocorre de forma instantânea na tabela.

● Critérios de Falha: A organização falha ou mantém a ordem padrão.

<br/>

45. Simulação do Gasto de Água Diário

● ID do Caso de Teste: CT-045

● Nome: Teste de Simulação do Gasto de Água Diário

● Objetivo: Garantir que, com o passar dos dias, a água estocada em todas as famílias vá diminuindo com base no consumo per capita.

● Pré-condições: As famílias têm cisternas com água e um gasto estipulado por pessoa.

● Entradas:

1. Virada de dia no sistema global

● Passos para Execução:

1. O relógio do sistema aplica a dedução diária automática de todas as casas.

● Resultado Esperado: Cada reservatório deve reduzir seu volume exato com base no número de pessoas que moram ali.

● Critérios de Sucesso: A rotina noturna abate a água corretamente sem pular nenhuma casa.

● Critérios de Falha: Algumas casas continuam com a água intacta apesar do consumo.

<br/>

46. Estimativa do Prazo Final da Água

● ID do Caso de Teste: CT-046

● Nome: Teste de Estimativa do Prazo Final da Água

● Objetivo: Garantir que a matemática que prevê quando a água vai acabar funcione arredondando para menos.

● Pré-condições: A casa tem 100 litros guardados e consome 30 por dia.

● Entradas:

1. Água contida
2. Custo hídrico diário familiar

● Passos para Execução:

1. O sistema calcula a data de fim da água para exibir no painel.

● Resultado Esperado: O resultado deve indicar 3 dias (arredondando o número fracionado de forma pessimista/segura).

● Critérios de Sucesso: A previsão é sempre correta e favorece o alerta antecipado.

● Critérios de Falha: O sistema aponta dias flutuantes incorretos ou otimistas.

<br/>

47. Prevenção de Cálculo de Consumo Inexistente

● ID do Caso de Teste: CT-047

● Nome: Teste de Prevenção de Cálculo de Consumo Inexistente

● Objetivo: Garantir que a matemática do sistema não quebre caso o consumo diário de uma casa conste como zero.

● Pré-condições: Um problema de configuração deixou o consumo diário em zero litros.

● Entradas:

1. Custo diário vazio
2. Reservatório cheio

● Passos para Execução:

1. O sistema tenta calcular os dias restantes para essa família.

● Resultado Esperado: O sistema deve interromper a matemática e acusar uma inconsistência nos dados.

● Critérios de Sucesso: A quebra na divisão por zero é prevenida adequadamente.

● Critérios de Falha: O painel congela tentando calcular um tempo infinito.

<br/>

Módulo de Cisternas

48. Abastecimento Justo das Cisternas

● ID do Caso de Teste: CT-048

● Nome: Teste de Abastecimento Justo das Cisternas

● Objetivo: Garantir que a água entregue pelo caminhão-pipa caiba nas cisternas e o registro do abastecimento fique correto.

● Pré-condições: A família possui cisternas com espaço vazio para armazenagem.

● Entradas:

1. Volume despejado pelo caminhão

● Passos para Execução:

1. O caminhão enche a cisterna.
2. O sistema recebe o aviso de recebimento de água.

● Resultado Esperado: A cisterna deve acumular o volume exato, e nenhuma água recebida deve ficar sobrando na contabilidade sem registrar transbordamento.

● Critérios de Sucesso: O nível do tanque sobe perfeitamente refletindo a entrega real.

● Critérios de Falha: Ocorre perda contábil da água ou a cisterna ultrapassa 100% misteriosamente.

<br/>

49. Consumo Individual no Reservatório

● ID do Caso de Teste: CT-049

● Nome: Teste de Consumo Individual no Reservatório

● Objetivo: Garantir que a subtração da água gasta em um dia reduza especificamente o reservatório consultado.

● Pré-condições: A cisterna possui água em quantidade considerável.

● Entradas:

1. Gastos do dia da família em questão

● Passos para Execução:

1. O sistema realiza a retirada teórica do volume da cisterna.

● Resultado Esperado: A régua de nível da cisterna deve descer de maneira equivalente ao que foi consumido na casa.

● Critérios de Sucesso: A matemática do balde é respeitada pelo sistema.

● Critérios de Falha: A cisterna fica negativa sem emitir alertas.

<br/>

50. Estimativa Simples de Autonomia

● ID do Caso de Teste: CT-050

● Nome: Teste de Estimativa Simples de Autonomia

● Objetivo: Certificar que a rotina simples de contagem de dias com a água guardada retorne valores críveis e defensivos.

● Pré-condições: Água guardada no balde é conhecida.

● Entradas:

1. Volume do balde
2. Consumo fixo diário

● Passos para Execução:

1. Consulte o número de dias garantidos para essa cisterna específica.

● Resultado Esperado: O cálculo deve retornar o tempo de sobrevivência hídrica de forma exata.

● Critérios de Sucesso: A estimativa básica de autonomia da cisterna opera de forma lógica.

● Critérios de Falha: Os números diferem do consumo global da família incoerentemente.

<br/>

51. Blindagem Contra Consumo Nulo em Reservatórios

● ID do Caso de Teste: CT-051

● Nome: Teste de Blindagem Contra Consumo Nulo em Reservatórios

● Objetivo: Garantir que o serviço da cisterna isole e trate um consumo diário zero antes que ele prejudique a plataforma.

● Pré-condições: A plataforma está preenchendo um relatório analítico para uma cisterna que erroneamente não consome nada.

● Entradas:

1. Consumo estimado em zero

● Passos para Execução:

1. A rotina da cisterna inicia o preenchimento estatístico de autonomia.

● Resultado Esperado: A rotina cancela a operação avisando da anomalia técnica em seus dados de consumo.

● Critérios de Sucesso: A segurança estrutural do sistema em relação à lógica matemática não oscila.

● Critérios de Falha: Ocorre o erro gravíssimo de loop sem fim.

<br/>

Módulo de Entregas de Água (Water Delivery)

52. Emissão de Recibo de Entrega

● ID do Caso de Teste: CT-052

● Nome: Teste de Emissão de Recibo de Entrega

● Objetivo: Garantir que o registro oficial de que o caminhão-pipa entregou água para a família seja salvo pela plataforma.

● Pré-condições: O motorista finalizou a visita e vai preencher a caderneta eletrônica.

● Entradas:

1. Família atendida
2. Quantidade distribuída do caminhão

● Passos para Execução:

1. Acesse o menu de recibos.
2. Preencha a ficha de entrega e confirme.

● Resultado Esperado: O sistema deve gerar um comprovante eletrônico (ID) de que o abastecimento ocorreu no dia estipulado.

● Critérios de Sucesso: O recibo fica registrado na plataforma de entregas de água.

● Critérios de Falha: A plataforma falha ao salvar o histórico da viagem.

<br/>

53. Consulta de Histórico de Entregas por Ano

● ID do Caso de Teste: CT-053

● Nome: Teste de Consulta de Histórico de Entregas por Ano

● Objetivo: Garantir que seja possível visualizar o extrato de tudo que uma família recebeu em um ano específico.

● Pré-condições: A família teve assistências hídricas ocorridas em um ano anterior.

● Entradas:

1. Número do Ano Desejado
2. Família atendida

● Passos para Execução:

1. Acesse a ficha cadastral do cliente.
2. Selecione o ano para visualizar o extrato de água.

● Resultado Esperado: O sistema deve baixar as informações exclusivas daquele período para a família escolhida.

● Critérios de Sucesso: O relatório anual aparece na interface do usuário sem falhas.

● Critérios de Falha: A pesquisa não retorna nada ou mostra os dados de outro ano acidentalmente.

<br/>

54. Gravação da Ficha de Água Entregue

● ID do Caso de Teste: CT-054

● Nome: Teste de Gravação da Ficha de Água Entregue

● Objetivo: Confirmar se o núcleo do sistema armazena a entrega sem corromper as ligações no banco de dados.

● Pré-condições: Os dados fornecidos no balcão da API são consistentes.

● Entradas:

1. Ficha digital de água entregue

● Passos para Execução:

1. A aplicação repassa os dados para serem cravados de forma permanente no cofre (repositório).

● Resultado Esperado: A ficha deve ser aceita e os dados gravados devem representar a realidade.

● Critérios de Sucesso: O armazenamento físico condiz perfeitamente com os preenchimentos originais.

● Critérios de Falha: A entrega quebra as conexões com a família principal ou perde dados de litragem.

<br/>

55. Cálculo Real da Água Absorvida na Cisterna

● ID do Caso de Teste: CT-055

● Nome: Teste de Cálculo Real da Água Absorvida na Cisterna

● Objetivo: Assegurar que, se a entrega exceder o tamanho da cisterna, o excedente não seja contabilizado incorretamente.

● Pré-condições: O volume que foi jogado na cisterna era maior do que o seu espaço sobrando.

● Entradas:

1. Volume do Pipa
2. Capacidade Sobrando do Recipiente

● Passos para Execução:

1. O sistema confronta o que foi enviado versus o que efetivamente encheu os tanques.

● Resultado Esperado: A quantidade de água anotada no relatório como 'Útil/Absorvida' considera apenas aquilo que não transbordou (descontando o desperdício).

● Critérios de Sucesso: As métricas de volume e eficiência são justas com a realidade da armazenagem.

● Critérios de Falha: O relatório aponta volumes inflados por causa dos vazamentos/desperdício ignorados.

<br/>

56. Acesso aos Registros Históricos Internos

● ID do Caso de Teste: CT-056

● Nome: Teste de Acesso aos Registros Históricos Internos

● Objetivo: Verificar se o catálogo de pesquisas alcança os registros antigos da base para os relatórios.

● Pré-condições: Foram realizadas viagens de abastecimento na cidade com relatórios emitidos.

● Entradas:

1. Informações da jornada de busca (Ano e Casa)

● Passos para Execução:

1. O sistema dispara uma pesquisa em lote no arquivo morto para compilar um extrato anual.

● Resultado Esperado: As caixas eletrônicas correspondentes devem ser localizadas, agrupadas e enviadas de volta.

● Critérios de Sucesso: O levantamento do pacote é construído sem perda de informações históricas.

● Critérios de Falha: A máquina se perde buscando papéis misturados de outras casas.

<br/>

57. Pesquisa de Histórico de Entregas Vazias

● ID do Caso de Teste: CT-057

● Nome: Teste de Pesquisa de Histórico de Entregas Vazias

● Objetivo: Assegurar que, caso uma família nunca tenha recebido água, o sistema lide com isso amigavelmente.

● Pré-condições: A família consultada não possui registros de entrega de água no ano informado.

● Entradas:

1. Ano civil selecionado
2. Endereço da família limpa

● Passos para Execução:

1. Inicie a busca por notas de assistência no sistema.

● Resultado Esperado: A aplicação não deve causar alarme falso e simplesmente informar que a página de extrato é uma lista sem itens.

● Critérios de Sucesso: O sistema retorna pacificamente o painel vazio e não compromete a tela.

● Critérios de Falha: O sistema trava ou dá colapso tentando encontrar as planilhas faltantes.

<br/>

Módulo de Configurações do Sistema

58. Visualização das Regras Municipais Vigentes

● ID do Caso de Teste: CT-058

● Nome: Teste de Visualização das Regras Municipais Vigentes

● Objetivo: Garantir o acesso seguro às determinações legais que a prefeitura estabeleceu para a base, como as cotas de consumo.

● Pré-condições: As configurações do sistema já estão cadastradas e estabilizadas.

● Entradas:

1. Painel de Controle Municipal

● Passos para Execução:

1. Abra as configurações gerais da localidade.
2. Verifique o painel do administrador.

● Resultado Esperado: O documento digital apresentando a cota diária garantida de água por cidadão é exibido em tela perfeitamente.

● Critérios de Sucesso: A administração tem conhecimento pleno das atuais regras geradoras de cálculos base.

● Critérios de Falha: Os avisos indicam ausência catastrófica de leis do município no sistema.

<br/>

59. Alerta Crítico para Cota Zero Inexistente

● ID do Caso de Teste: CT-059

● Nome: Teste de Alerta Crítico para Cota Zero Inexistente

● Objetivo: Assegurar que se o ambiente de trabalho for inicializado em branco, alertas fundamentais sejam imediatamente despachados.

● Pré-condições: A base da cidade nova foi instalada porém nenhum administrador preencheu as bases vitais da plataforma.

● Entradas:

1. Ambiente virgem sem configurações de água pré-definidas

● Passos para Execução:

1. Um agente tenta rodar a funcionalidade de análise de cisternas.

● Resultado Esperado: Tudo deve parar. A aplicação emitirá um bloqueio formal dizendo que é impossível trabalhar sem os valores essenciais diários.

● Critérios de Sucesso: O evento que forçaria o colapso do ecossistema é freado intencionalmente alertando a negligência no perfil global.

● Critérios de Falha: A plataforma se cala, coloca zero água em todos, e causa interrupção de abastecimentos essenciais aos usuários por bug analítico.

<br/>

60. Decreto de Alteração de Consumo Global

● ID do Caso de Teste: CT-060

● Nome: Teste de Decreto de Alteração de Consumo Global

● Objetivo: Permitir o ajuste fácil e centralizado de quanto o governo planeja ceder de água em todo o ecossistema, reagindo a períodos de seca maior ou bonança.

● Pré-condições: O gestor do município tomou a decisão estratégica de aumentar a litragem de todos os relatórios da cidade.

● Entradas:

1. Decreto (Input) com a nova meta de Litros Diários por Cidadão

● Passos para Execução:

1. Acesse o núcleo principal das diretrizes da Prefeitura do site.
2. Realize a injeção do novo número.
3. Grave e replique a decisão administrativa.

● Resultado Esperado: A matriz principal aceita os cálculos, adota a nova lei municipal em seus componentes diários, e retorna uma confirmação atualizada.

● Critérios de Sucesso: A mudança impactará todos sem erros de arredondamento e as estimativas do sistema mudarão a partir desse decreto.

● Critérios de Falha: As diretrizes congelam. As novas requisições continuam consumindo baseando-se no limite do governo anterior, o botão salva mas nada faz.

<br/>

61. Segurança no Decreto Sem Fundações

● ID do Caso de Teste: CT-061

● Nome: Teste de Segurança no Decreto Sem Fundações

● Objetivo: Garantir a intercepção de prefeitos digitais alterando algo que não existe (atualizações em sistemas que nunca foram iniciados e ativados antes).

● Pré-condições: Tentativa de reformar a lei antes da constituição do município no portal digital ser escrita e implantada (primeiro acesso de servidor limpo).

● Entradas:

1. Comando de atualização sobre configurações fantasmas

● Passos para Execução:

1. Dispare a ordem de alteração na tentativa de trocar algo no sistema virgem.

● Resultado Esperado: O sistema percebe e impede ativamente o uso dizendo que é necessário, antes de tudo, construir a configuração original (Setup Incompleto).

● Critérios de Sucesso: Desastres de sobrescrição sobre informações vazias ou clonadas são evitados.

● Critérios de Falha: O sistema insere informações fragmentadas sem a raiz adequada. Estatísticas começam a errar contas sem aviso ao usuário.

<br/>
