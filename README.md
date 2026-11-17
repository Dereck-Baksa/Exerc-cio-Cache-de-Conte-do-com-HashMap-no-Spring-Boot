# Cache de conteúdo com HashMap e Spring Boot

Projeto didático de uma API REST para cadastrar e consultar conteúdos. Ele demonstra como um cache em memória pode evitar consultas repetidas ao banco de dados e como implementar uma estrutura semelhante a um `HashMap` sem usar a implementação pronta do Java.

## Como funciona

O fluxo de uma consulta `GET /conteudos/{id}` é:

1. A API verifica se o conteúdo está no cache.
2. Em caso de **HIT**, retorna o conteúdo armazenado e atualiza o horário do último acesso.
3. Em caso de **MISS**, consulta o banco H2, guarda o resultado no cache e retorna o conteúdo.

Para tornar a diferença observável, a consulta à fonte de dados simula uma espera de 2 segundos. Assim, a primeira consulta tende a ser mais lenta e as seguintes, enquanto o item permanecer válido no cache, tendem a ser mais rápidas.

O cache possui limite de 5 itens e validade de 60 segundos por padrão. Quando está cheio, remove o item menos recentemente acessado (**LRU**). Ao atualizar ou excluir um conteúdo, sua entrada é invalidada. As estatísticas acumulam hits, misses e remoções por limite durante a execução.

`MeuMapa<K, V>` é a estrutura de mapa implementada no projeto. Ela calcula o índice do bucket usando o hash da chave e trata colisões por encadeamento de nós. Oferece operações de inserção/atualização (`put`), busca (`get`), remoção (`remove`), listagem de chaves, limpeza e consulta do tamanho.

## Requisitos

- Java 21
- Maven

## Executar

Na raiz do projeto:

```bash
mvn spring-boot:run
```

A API ficará disponível em `http://localhost:8080`. Para empacotar e executar o JAR:

```bash
mvn clean package
java -jar target/cache-0.0.1-SNAPSHOT.jar
```

Ao abrir `http://localhost:8080/`, a API retorna uma mensagem com os endpoints disponíveis. A raiz não é uma interface web; as operações devem ser chamadas nos endpoints indicados abaixo.

O banco H2 é em memória (`jdbc:h2:mem:xpto`): os dados são recriados quando a aplicação reinicia. A console do H2 fica em `http://localhost:8080/h2-console`, com JDBC URL `jdbc:h2:mem:xpto`, usuário `sa` e senha em branco.

## Como usar a API

### Criar conteúdo

```bash
curl -X POST http://localhost:8080/conteudos \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Meu primeiro conteúdo","corpo":"Texto do conteúdo","autor":"Ana"}'
```

A resposta contém o conteúdo criado, incluindo o `id` gerado e `atualizadoEm`. Os campos `titulo`, `corpo` e `autor` são obrigatórios.

### Buscar por ID e observar o cache

```bash
curl -i http://localhost:8080/conteudos/1
```

A resposta inclui o cabeçalho `X-Cache`: `MISS` quando o conteúdo precisou ser buscado na fonte, ou `HIT` quando foi servido pelo cache. Repita a consulta para observar o hit (desde que o item não tenha expirado ou sido removido).

### Atualizar conteúdo

```bash
curl -X PUT http://localhost:8080/conteudos/1 \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Título atualizado","corpo":"Texto atualizado","autor":"Ana"}'
```

A atualização invalida a entrada correspondente no cache.

### Excluir conteúdo

```bash
curl -X DELETE http://localhost:8080/conteudos/1
```

### Consultar estatísticas

```bash
curl http://localhost:8080/cache/estatisticas
```

A resposta informa `tamanho`, `limite`, `hits`, `misses`, `evictions` e `hitRatio` (percentual de hits sobre o total de consultas registradas).

### Limpar o cache

```bash
curl -X DELETE http://localhost:8080/cache
```

## Configuração do cache

Em `src/main/resources/application.properties`:

```properties
app.cache.max-itens=5
app.cache.ttl-segundos=60
```

Altere esses valores para mudar, respectivamente, o limite de entradas e o tempo de validade em segundos.
