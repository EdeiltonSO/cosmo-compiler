# Cosmo Compiler

## O que é?

Cosmo

## Como compilar e executar?

1. Tenha o JDK >= 17.0.2;
2. Execute o comando `cd src` na pasta raiz;
3. Compile o arquivo principal `.java` com o comando `javac Cosmo.java`;
4. Execute o arquivo `.class` com o comando `java Cosmo input`, onde `input` é o caminho para o arquivo de entrada que se deseja compilar;

### Notas

- Executar o compilador sem um arquivo de entrada resulta numa mensagem de erro.

- Fornecer um arquivo ou diretório que não existe também resulta numa mensagem de erro.

## Estrutura do projeto

```
cosmo-compiler/
├── src/
|   ├── cosmo/
│   │   ├── Lexer.java
│   │   ├── Token.java
|   |   ├── Parser.java
│   │   ├── *.class
│   ├── Cosmo.java
│   ├── Cosmo.class
├── test/
├── README.md
```

- `src/`: Arquivos fonte do projeto;
  - `src/Cosmo.java`: Classe principal do projeto. Contém o método `main` e é responsável por chamar os métodos de 
    análise léxica, sintática e semântica, além da geração de código objeto;
  - `src/cosmo/`: Arquivos fonte das classes que implementam o backend do compilador;
    - `src/cosmo/AST`: Arquivos fonte das clases que implementam a árvore sintática abstrata (AST);
      - `src/cosmo/AST/Node*.java`: Implementações dos nós da AST;
      - `src/cosmo/AST/Printer.java`: Implementação do Visitor para a etapa de impressão da AST;
      - `src/cosmo/AST/Visitor.java`: Interface para o padrão de projeto Visitor;   
    - `src/cosmo/Lexer.java`: Implementação do analisador léxico;
    - `src/cosmo/Parser.java`: Implementação do analisador sintático;
    - `src/cosmo/Token.java`: Enumeração dos tokens reconhecidos pelo analisador léxico, além de 
      suas representações textuais;
    - `src/cosmo/*.class`: Arquivos `.class` gerados pela compilação dos arquivos `.java` acima;
- `examples/`: Arquivos de teste do projeto.
