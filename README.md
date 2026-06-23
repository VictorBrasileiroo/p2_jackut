# Jackut

Nome: Victor André Lopes Brasileiro

Matrícula: 202407269

Projeto da disciplina de Programação 2 (UFAL/IC). Jackut e uma rede de relacionamentos implementada em Java para execucao dos testes de aceitacao do EasyAccept.

Entrega atual: Milestone 2, com as User Stories 1 a 9 implementadas.

---

## Funcionalidades Entregues

O estado atual cobre as User Stories 1 a 9.

| User Story | Funcionalidades principais |
| --- | --- |
| US1 | Criacao de conta, validacao de login e senha, abertura de sessao, consulta de nome, limpeza e persistencia do sistema |
| US2 | Edicao de perfil, consulta de atributos preenchidos e tratamento de atributos inexistentes |
| US3 | Convite de amizade, confirmacao por adicao reciproca, consulta e listagem de amigos |
| US4 | Envio de recados, leitura em ordem de chegada, remocao apos leitura e persistencia dos recados pendentes |
| US5 | Criacao de comunidades, dono, descricao, membros iniciais e persistencia |
| US6 | Entrada de usuarios em comunidades e listagem das comunidades de cada usuario |
| US7 | Envio e leitura de mensagens de comunidade, separadas dos recados privados |
| US8 | Relacionamentos de fa-idolo, paquera e inimizade, com regras de bloqueio e recados automaticos |
| US9 | Remocao de conta, incluindo limpeza de comunidades, amizades, recados, mensagens e relacionamentos |

## Execucao

Os comandos abaixo devem ser executados a partir da pasta `jackut_project`.

```powershell
javac -encoding UTF-8 -cp "lib\easyaccept.jar" -d "out\verification" `
  (Get-ChildItem -Path "src" -Recurse -Filter "*.java").FullName
```

```powershell
java "-Dfile.encoding=UTF-8" -cp "out\verification;lib\easyaccept.jar" br.ufal.ic.p2.jackut.Main
```

Para gerar os Javadocs:

```powershell
javadoc -quiet -encoding UTF-8 -charset UTF-8 -classpath "lib\easyaccept.jar" `
  -d "out\javadoc" `
  (Get-ChildItem -Path "src" -Recurse -Filter "*.java").FullName
```

Tambem e possivel executar um script especifico do EasyAccept:

```powershell
java "-Dfile.encoding=UTF-8" -cp "out\verification;lib\easyaccept.jar" easyaccept.EasyAccept `
  br.ufal.ic.p2.jackut.Facade tests\us1_1.txt
```

## Documentacao Da Entrega

Os documentos principais da entrega sao:

| Arquivo | Papel |
| --- | --- |
| `README.md` | Visao geral do projeto, execucao, arquitetura e principais escolhas |
| `relatorio/relatorio-milestone2.md` | Relatorio do Milestone 2 com arquitetura, fluxos, padroes e verificacao |

## Organizacao Do Codigo

O codigo-fonte possui Javadocs em pacotes, classes e metodos publicos para documentar contratos, parametros, retornos e excecoes. As excecoes de dominio sao checked exceptions, por meio de `JackutException extends Exception`.

```text
jackut_project/src/br/ufal/ic/p2/jackut/
|-- Main.java
|-- Facade.java
|-- services/
|   |-- JackutApplication.java
|   |-- SistemaService.java
|   |-- UsuarioService.java
|   |-- SessaoService.java
|   |-- AmizadeService.java
|   |-- RecadoService.java
|   |-- ComunidadeService.java
|   `-- RelacionamentoService.java
|-- repositories/
|   |-- UsuarioRepository.java
|   |-- SessaoRepository.java
|   `-- ComunidadeRepository.java
|-- models/
|   |-- EstadoJackut.java
|   |-- Usuario.java
|   |-- Sessao.java
|   |-- Recado.java
|   |-- Mensagem.java
|   `-- Comunidade.java
|-- persistence/
|   `-- PersistenciaService.java
`-- exceptions/
    |-- JackutException.java
    `-- excecoes especificas de dominio
```

## Funcionamento Geral

O EasyAccept acessa o sistema por `br.ufal.ic.p2.jackut.Facade`. A `Facade` expoe apenas os comandos esperados pelos testes e delega as operacoes para `JackutApplication`.

O fluxo principal e:

```text
EasyAccept -> Facade -> JackutApplication -> services -> repositories -> models
                                               |
                                               v
                                           persistence
```

`JackutApplication` monta as dependencias e coordena apenas operacoes transversais, como remocao de conta. As regras de usuario, sessao, amizade, recado, comunidade e relacionamentos ficam em services menores. Os repositories controlam acesso ao estado. Os models guardam regras e dados do dominio. A persistencia fica isolada em `PersistenciaService`, usando arquivo relativo em `dados/jackut.ser` ou em uma pasta equivalente localizada automaticamente.

## Responsabilidades Principais

| Componente | Responsabilidade |
| --- | --- |
| `Main` | Localizar a pasta de testes e executar os scripts do EasyAccept |
| `Facade` | Servir como entrada publica dos testes e delegar os comandos |
| `JackutApplication` | Montar dependencias e coordenar fluxos transversais |
| `UsuarioService` | Criar conta, editar perfil e consultar atributos |
| `SessaoService` | Abrir sessoes e localizar usuarios autenticados |
| `AmizadeService` | Solicitar, confirmar, verificar e listar amizades |
| `RecadoService` | Enviar e ler recados privados |
| `ComunidadeService` | Criar comunidades, adicionar membros e enviar mensagens coletivas |
| `RelacionamentoService` | Controlar idolos, fas, paqueras e inimigos |
| `SistemaService` | Zerar, encerrar e persistir estado |
| `EstadoJackut` | Agrupar usuarios e comunidades persistentes |
| `Usuario` | Proteger perfil, comunidades, amizades, recados, mensagens e relacionamentos |
| `Comunidade` | Proteger nome, descricao, dono e membros |
| `exceptions` | Concentrar erros de dominio com mensagens exigidas pelos testes |

## Regras Implementadas

### Contas, Perfil E Sessoes

`criarUsuario` valida login e senha antes de registrar uma nova conta. `abrirSessao` autentica login e senha e cria uma sessao em memoria. As operacoes protegidas usam o id da sessao para localizar o usuario autenticado. O perfil continua dinamico, permitindo criar ou alterar atributos por `editarPerfil`.

### Amizades E Recados

Uma amizade so e confirmada por adicao reciproca. Recados privados sao enviados de um usuario para outro e lidos em ordem de chegada. Inimizades bloqueiam tentativas de amizade e envio de recado para quem marcou o remetente como inimigo.

### Comunidades E Mensagens

Comunidades possuem nome unico, descricao, dono e membros. O dono entra como primeiro membro. Usuarios podem participar de varias comunidades, mantendo ordem de entrada. Mensagens enviadas a comunidades chegam a todos os membros que nao bloquearam o remetente por inimizade. Mensagens de comunidade e recados privados possuem filas separadas.

### Novos Relacionamentos

O relacionamento fa-idolo e publico: `getFas` lista quem adicionou o usuario como idolo. Paqueras sao privadas para quem adicionou; quando ha reciprocidade, o sistema envia recados automaticos aos dois usuarios. Inimizades impedem que o usuario bloqueado adicione o outro como amigo, idolo ou paquera, e tambem impedem recados diretos.

### Remocao De Conta

`removerUsuario` remove a conta associada a uma sessao e limpa suas referencias: perfil, amizades, convites, comunidades criadas, participacao em comunidades, recados enviados, mensagens enviadas e relacionamentos.

## Escolhas De Design

O sistema usa `EstadoJackut` como snapshot persistente de usuarios e comunidades. O acesso passa por repositories para evitar manipulacao livre dos mapas internos.

A camada de aplicacao foi dividida por area funcional para evitar concentrar regras em uma unica classe. Essa divisao responde ao feedback do milestone 1 sobre modularidade e separacao de responsabilidades.

As excecoes especificas continuam representando os erros do dominio, mas agora herdam de `JackutException`, que por sua vez herda de `Exception`. Isso atende ao padrao solicitado de excecoes verificadas sem voltar ao problema de `throws Exception` generico.

## Padroes Presentes

### Facade

A `Facade` oferece uma interface unica para o EasyAccept e preserva os nomes dos comandos dos scripts.

### Service Layer

Os services organizam casos de uso por area funcional. Isso reduz acoplamento e evita que uma unica classe acumule usuario, sessao, amizade, recado, comunidade e relacionamentos.

### Repository

`UsuarioRepository`, `SessaoRepository` e `ComunidadeRepository` isolam o acesso ao estado e as colecoes internas.

### Domain Model

`Usuario` e `Comunidade` possuem comportamento proprio e protegem suas colecoes internas contra alteracao externa.

### State Snapshot

`EstadoJackut` representa o estado persistente do sistema e permite salvar/carregar usuarios, comunidades, recados, mensagens e relacionamentos como uma unidade.

### Checked Exceptions

As excecoes especificas de dominio herdam de `JackutException`, que herda de `Exception`. Isso evita `RuntimeException` para erros esperados pelo contrato e deixa explicito quais operacoes podem falhar por regra de negocio.

---
