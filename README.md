# Jackut

Nome: Victor André Lopes Brasileiro

Matrícula: 202407269

Projeto da disciplina de Programação 2 (UFAL/IC). Jackut e uma rede de relacionamentos implementada em Java para execucao dos testes de aceitacao do EasyAccept.

---

## Funcionalidades Entregues

O estado atual cobre as User Stories 1 a 4.

| User Story | Funcionalidades principais |
| --- | --- |
| US1 | Criacao de conta, validacao de login e senha, abertura de sessao, consulta de nome, limpeza e persistencia do sistema |
| US2 | Edicao de perfil, consulta de atributos preenchidos e tratamento de atributos inexistentes |
| US3 | Convite de amizade, confirmacao por adicao reciproca, consulta e listagem de amigos |
| US4 | Envio de recados, leitura em ordem de chegada, remocao apos leitura e persistencia dos recados pendentes |

## Execucao

Os comandos abaixo devem ser executados a partir da pasta `jackut_project`.

```powershell
javac -encoding UTF-8 -cp "lib\easyaccept.jar" -d "out\verification" `
  (Get-ChildItem -Path "src" -Recurse -Filter "*.java").FullName
```

```powershell
java "-Dfile.encoding=UTF-8" -cp "out\verification;lib\easyaccept.jar" br.ufal.ic.p2.jackut.Main
```

Tambem e possivel executar um script especifico do EasyAccept:

```powershell
java "-Dfile.encoding=UTF-8" -cp "out\verification;lib\easyaccept.jar" easyaccept.EasyAccept `
  br.ufal.ic.p2.jackut.Facade tests\us1_1.txt
```

## Organizacao Do Codigo

O codigo-fonte possui Javadocs em pacotes, classes e metodos publicos para documentar contratos, parametros, retornos e excecoes.

```text
jackut_project/src/br/ufal/ic/p2/jackut/
|-- Main.java
|-- Facade.java
|-- services/
|   `-- JackutService.java
|-- repositories/
|   |-- SessaoRepository.java
|   `-- UsuarioRepository.java
|-- models/
|   |-- EstadoJackut.java
|   |-- Recado.java
|   |-- Sessao.java
|   `-- Usuario.java
|-- persistence/
|   `-- PersistenciaService.java
`-- exceptions/
    |-- JackutException.java
    `-- excecoes especificas de dominio
```

## Funcionamento Geral

O EasyAccept acessa o sistema por `br.ufal.ic.p2.jackut.Facade`. A `Facade` expoe apenas os comandos esperados pelos testes e delega as operacoes para `JackutService`.

O fluxo principal e:

```text
EasyAccept -> Facade -> JackutService -> repositories -> models
                                      -> persistence
                                      -> exceptions
```

`JackutService` coordena os casos de uso. Os repositories controlam acesso a usuarios e sessoes. Os models guardam as regras e o estado do dominio. A persistencia fica isolada em `PersistenciaService`, usando arquivo relativo em `dados/jackut.ser` ou em uma pasta equivalente localizada automaticamente.

## Responsabilidades Principais

| Componente | Responsabilidade |
| --- | --- |
| `Main` | Localizar a pasta de testes e executar os scripts do EasyAccept |
| `Facade` | Servir como entrada publica dos testes e delegar os comandos |
| `JackutService` | Coordenar criacao de conta, sessao, perfil, amizade, recados e persistencia |
| `UsuarioRepository` | Consultar e registrar usuarios no estado do sistema |
| `SessaoRepository` | Criar, consultar e limpar sessoes abertas em memoria |
| `EstadoJackut` | Agrupar o estado persistente do sistema |
| `Usuario` | Proteger login, senha, perfil, amigos, convites e recados |
| `Recado` | Representar uma mensagem recebida por um usuario |
| `PersistenciaService` | Carregar, salvar e apagar o estado serializado |
| `exceptions` | Concentrar erros de dominio com mensagens exigidas pelos testes |

## Regras Implementadas

### Contas E Sessoes

`criarUsuario` valida login e senha antes de registrar uma nova conta. `abrirSessao` autentica login e senha e cria uma sessao em memoria. As operacoes protegidas usam o id da sessao para localizar o usuario autenticado.

### Perfil

O perfil do usuario e dinamico. O atributo `nome` e criado junto com a conta, e outros atributos podem ser adicionados ou alterados por `editarPerfil`. Consultas a atributos ausentes resultam em erro de dominio.

### Amizades

Uma amizade so e confirmada quando ha adicao reciproca. A primeira chamada de `adicionarAmigo` registra um convite pendente. Quando o outro usuario adiciona de volta, o convite e removido e a amizade passa a existir para os dois lados.

### Recados

`enviarRecado` adiciona uma mensagem na fila do destinatario. `lerRecado` remove e retorna o primeiro recado recebido, preservando a ordem de chegada. Enviar recado para si mesmo e tentar ler uma fila vazia geram erros especificos.

## Mensagens De Erro

| Erro | Mensagem do contrato |
| --- | --- |
| Login invalido | `Login inválido.` |
| Senha invalida | `Senha inválida.` |
| Conta existente | `Conta com esse nome já existe.` |
| Login ou senha invalidos | `Login ou senha inválidos.` |
| Usuario nao cadastrado | `Usuário não cadastrado.` |
| Atributo nao preenchido | `Atributo não preenchido.` |
| Auto-adicao de amizade | `Usuário não pode adicionar a si mesmo como amigo.` |
| Amigo ja confirmado | `Usuário já está adicionado como amigo.` |
| Convite pendente | `Usuário já está adicionado como amigo, esperando aceitação do convite.` |
| Auto-envio de recado | `Usuário não pode enviar recado para si mesmo.` |
| Sem recados | `Não há recados.` |

As mensagens sao mantidas em excecoes especificas para evitar duplicacao de strings de erro na logica de negocio.

## Escolhas De Design

O sistema usa um objeto de estado (`EstadoJackut`) para persistir os dados de forma coesa. O estado nao e manipulado diretamente pela `Facade`; o acesso passa por repositories e pelo service de aplicacao.

Usuarios sao identificados por login unico. O perfil usa um mapa para permitir atributos dinamicos. Amigos e convites usam conjuntos ordenados para evitar duplicidade sem perder a ordem esperada pelos testes. Recados ficam em fila, pois a leitura deve seguir o comportamento FIFO.

As sessoes ficam apenas em memoria, enquanto usuarios, perfil, amizades e recados pendentes sao salvos quando o sistema e encerrado.

## Padroes Presentes

### Facade

A `Facade` oferece uma interface unica para o EasyAccept. Ela reduz o acoplamento entre os scripts de teste e a organizacao interna do sistema, mantendo os comandos publicos estaveis mesmo quando a implementacao evolui.

### Service Layer

`JackutService` concentra a coordenacao dos casos de uso. Ele valida o fluxo entre repositories, models e persistencia sem transformar a `Facade` em uma classe com regra de negocio.

### Repository

`UsuarioRepository` e `SessaoRepository` isolam a forma como usuarios e sessoes sao localizados. Isso evita que outras camadas manipulem diretamente mapas ou detalhes internos do armazenamento.

### Domain Model

As entidades do dominio possuem comportamento proprio. `Usuario` altera perfil, controla convites, confirma amigos e gerencia sua fila de recados sem expor colecoes mutaveis para modificacao externa.

### State Snapshot

`EstadoJackut` representa um retrato serializavel do sistema. Essa decisao simplifica o carregamento, o salvamento e a limpeza do estado sem misturar persistencia com regra de negocio.

---
