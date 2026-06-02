# Jackut

Implementacao incremental da rede de relacionamentos Jackut para os testes do EasyAccept.

## Estado atual

A User Story 1 esta implementada:

- criar usuario com login, senha e nome;
- consultar atributo `nome`;
- abrir sessao com login e senha;
- zerar e encerrar o sistema;
- persistir os usuarios em arquivo relativo.

A User Story 2 esta implementada:

- consultar atributos preenchidos do perfil;
- sinalizar atributos nao preenchidos;
- editar atributos de perfil em sessao autenticada;
- persistir os atributos editados.

## Arquitetura

O EasyAccept acessa o sistema por `br.ufal.ic.p2.jackut.Facade`. A facade e fina e delega os comandos para `services.JackutService`.

A implementacao atual usa:

- `models`: estado persistente, usuario e sessao;
- `repositories`: acesso controlado aos usuarios e sessoes;
- `persistence`: salvamento e carregamento do estado em `../dados/jackut.ser`;
- `exceptions`: erros de dominio com mensagens exigidas pelos testes.

## Como verificar

Executar a partir de `jackut_project`:

```powershell
javac -encoding UTF-8 -cp "lib\easyaccept.jar" -d "out\verification" `
  (Get-ChildItem -Path "src" -Recurse -Filter "*.java").FullName
```

```powershell
java -cp "out\verification;lib\easyaccept.jar" easyaccept.EasyAccept `
  br.ufal.ic.p2.jackut.Facade tests\us1_1.txt
```

```powershell
java -cp "out\verification;lib\easyaccept.jar" easyaccept.EasyAccept `
  br.ufal.ic.p2.jackut.Facade tests\us1_2.txt
```

```powershell
java -cp "out\verification;lib\easyaccept.jar" easyaccept.EasyAccept `
  br.ufal.ic.p2.jackut.Facade tests\us2_1.txt
```

```powershell
java -cp "out\verification;lib\easyaccept.jar" easyaccept.EasyAccept `
  br.ufal.ic.p2.jackut.Facade tests\us2_2.txt
```

Ou pela `Main`:

```powershell
java -cp "out\verification;lib\easyaccept.jar" br.ufal.ic.p2.jackut.Main
```
