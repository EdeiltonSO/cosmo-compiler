# cosmo-compiler
Lorem ipsum dolor sit amet, consectetur adipiscing elit. In tellus nibh, semper non tellus at, tempus consectetur odio. Sed sed odio leo. Nullam eget lacus arcu. Pellentesque volutpat non ante vitae hendrerit. Nunc sed dolor vel est congue dapibus. Proin ac nisl eu nisl bibendum pellentesque dignissim et libero. Curabitur sed nisl auctor, ultrices massa ac, luctus neque. Vivamus malesuada suscipit urna ac aliquet. Donec mollis tempus justo, sit amet accumsan ex bibendum at. Maecenas id ante maximus, tincidunt erat eget, varius augue. In sed lacinia est. Duis dapibus non quam eget venenatis.

## TODO
- [ ] Escrever um README de respeito

## O que é?
Cosmo

## Como compilar e executar?
Até decidirmos a estrutura final do projeto, para compilar e executar o programa:

- Execute o comando `cd src` na pasta raiz
- Compile o arquivo principal `.java` com o comando `javac Cosmo.java`
- Execute o arquivo `.class` com o comando `java Cosmo input`, onde `input` é o caminho para o arquivo de entrada que se deseja compilar
  - Executar o compilador sem um arquivo de entrada resulta numa mensagem de erro
  - Fornecer um arquivo ou diretório que não existe também resulta numa mensagem de erro

## Estrutura do projeto

*Temporário...*

```
cosmo-compiler/
├── src/
│   ├── Cosmo.java
│   ├── Cosmo.class
│   ├── *.java
|   └── ...
├── test/
├── README.md
```

- `src/` - Arquivos fonte do projeto. Temos que decidir se as outras classes vão ficar na mesma raiz do arquivo principal ou se vão para um diretório mais interno.
- `test/` - Arquivos de teste do projeto. Por enquanto ficarão somente os arquivos de entrada que usaremos para testar o compilador.
