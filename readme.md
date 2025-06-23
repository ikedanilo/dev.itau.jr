
# Título do Projeto

Dev Itau JR - API de Transações e Estatísticas
Este projeto é uma API REST desenvolvida em Java Spring Boot (v3.5.0) com Maven, que simula um backend de transações financeiras, incluindo funcionalidades como:

Cadastro de transações

Consulta de estatísticas de transações

Exclusão de transações

Documentação exposta via Swagger UI



## Variáveis de Ambiente

Antes de rodar o projeto, é necessário criar um arquivo .env (ou definir manualmente) com as seguintes variáveis de ambiente para o Docker Compose:

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
|`POSTGRES_USER`|	Usuário do banco Postgres	|postgres
|`POSTGRES_PASSWORD`|	Senha do banco Postgres	|postgres
|`POSTGRES_DB`|	Nome do banco de dados|	devitaujr

Além disso, a aplicação Spring Boot consome as seguintes variáveis (geralmente via docker-compose.yml):

- `SPRING_DATASOURCE_URL`	URL de conexão ao Postgres
- `SPRING_DATASOURCE_USERNAME` Usuário do banco
- `SPRING_DATASOURCE_PASSWORD` Senha do banco

## Instalação

Pré-requisitos:
- Java 21
- Maven
- Docker e Docker Compose

Rodando com Docker:
```bash
docker-compose up --build
```

A aplicação ficará disponível em:

👉 http://localhost:8080

E o banco de dados Postgres em:

👉 localhost:5432

Rodando local sem Docker (modo dev):
Suba manualmente um Postgres local (ou use o Docker apenas para o banco)

Ajuste o application.properties ou defina as variáveis de ambiente

Execute:

```bash
mvn clean package
java -jar target/dev.itau.jr-0.0.-SNAPSHOT.jar
```
## Documentação da API

#### Retorna todas transações cadastradas

```http
  GET /transacao
```

#### Cria transação

```http
  POST /transacao
```

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `valor` | `Double` | **Obrigatório**. Valor da transação |
| `dataHora` | `OffsetDateTime` | **Obrigatório**. Data e hora. Ex: 2025-06-23T01:12:11.250Z |

#### Deleta todas transações

```http
  DELETE /transacao
```

#### Retorna transação por id

```http
  GET /transacao{id}
```

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `int` | **Obrigatório**. Id da transação |

#### Altera transação por id

```http
  PUT /transacao{id}
```

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `int` | **Obrigatório**. Id da transação |

## Apêndice

Tecnologias utilizadas:
- Java 21
- Spring Boot 3.5.0
- Maven
- PostgreSQL
- Docker / Docker Compose
- Swagger OpenAPI para documentação