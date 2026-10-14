# Meu primeiro Spring Boot

API REST de tarefas feita para estudar Spring Boot. Tem CRUD completo, validação de entrada, tratamento global de erros, banco PostgreSQL em container e testes em três níveis.

## Tecnologias

- Java 17
- Spring Boot 4.1.1
- Spring Web, Spring Data JPA (Hibernate) e Bean Validation
- PostgreSQL 16 com Docker Compose
- JUnit 5, Mockito e MockMvc
- H2 em memória (só nos testes)
- Maven (com wrapper)

## Funcionalidades

- CRUD completo de tarefas com persistência em PostgreSQL
- Validação de entrada (título obrigatório e limites de tamanho), com erro 400
- Tratamento global de exceções no formato `ProblemDetail` (RFC 7807)
- Separação em camadas, com DTOs para não expor a entidade
- 17 testes automatizados (unidade, repositório e web)

## Arquitetura

O código fica em `src/main/java/com/example/spring`.

| Camada | Classe | Responsabilidade |
|---|---|---|
| Controller | `TarefaController` | Recebe a requisição HTTP, valida a entrada com `@Valid`, chama o service e monta a resposta (status e `Location`). Não tem regra de negócio. |
| Service | `TarefaService` | Regras de negócio e transações com `@Transactional`. Orquestra repository e mapper. |
| Repository | `TarefaRepository` | Acesso ao banco com Spring Data JPA. |
| Entity | `Tarefa` | Representa a tabela `tarefas` (id, titulo, descricao, concluida). |
| DTO | `TarefaRequest` e `TarefaResponse` | Contrato da API. O request carrega as validações e o response evita expor a entidade. |
| Mapper | `TarefaMapper` | Converte entidade em DTO e o contrário. |
| Exception | `RecursoNaoEncontradoException` e `GlobalExceptionHandler` | Exceção de domínio e respostas de erro padronizadas. |

Fluxo de uma requisição

```
Controller -> Service -> Repository -> Banco
```

A resposta volta como DTO.

## Como rodar

Você precisa de Java 17 ou superior e Docker.

1. Clone o repositório

```bash
git clone https://github.com/PabloPP5/meu-primeiro-spring-boot.git
cd meu-primeiro-spring-boot
```

2. Suba o banco

```bash
docker compose up -d
```

3. Rode a aplicação

```bash
./mvnw spring-boot:run
```

A API fica em `http://localhost:8080`.

Para parar o banco, use `docker compose down`. Os dados ficam em um volume do Docker. Para apagar tudo, incluindo os dados, use `docker compose down -v`.

## Banco de dados

O `docker-compose.yml` sobe um PostgreSQL 16 com estas configurações, que servem só para estudo.

| Item | Valor |
|---|---|
| Banco | `tarefasdb` |
| Usuário | `tarefas` |
| Senha | `tarefas` |
| Porta | `5432` |

A tabela `tarefas` é criada pelo Hibernate na primeira execução, com `ddl-auto=update`.

## Endpoints

| Método | Rota | Descrição | Respostas |
|---|---|---|---|
| POST | `/api/tarefas` | Cria uma tarefa | `201` com header `Location`, `400` se a entrada for inválida |
| GET | `/api/tarefas` | Lista as tarefas | `200` |
| GET | `/api/tarefas/{id}` | Busca uma tarefa | `200` ou `404` |
| PUT | `/api/tarefas/{id}` | Atualiza uma tarefa | `200` ou `404` |
| DELETE | `/api/tarefas/{id}` | Remove uma tarefa | `204` ou `404` |
| GET | `/api/hello` | Endpoint simples do início do projeto | `200` |

### Exemplo

Criar uma tarefa

```bash
curl -i -X POST http://localhost:8080/api/tarefas \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Estudar Spring Boot", "descricao": "Fazer o projeto de tarefas", "concluida": false}'
```

Resposta

```json
{
  "id": 1,
  "titulo": "Estudar Spring Boot",
  "descricao": "Fazer o projeto de tarefas",
  "concluida": false
}
```

O título é obrigatório e tem até 100 caracteres. A descrição é opcional e tem até 500.

Os erros seguem o formato `ProblemDetail`, com campos como `title`, `status` e `detail`.

## Testes

```bash
./mvnw test
```

Os testes usam H2 em memória, então não precisam do PostgreSQL no ar.

| Classe | Quantidade | Tipo |
|---|---|---|
| `TarefaServiceTest` | 6 | Unidade, com Mockito |
| `TarefaRepositoryTest` | 3 | `@DataJpaTest` com H2 |
| `TarefaControllerTest` | 7 | `@WebMvcTest` com MockMvc |
| `MeuPrimeiroSpringBootApplicationTests` | 1 | Sobe o contexto da aplicação |

## Próximos passos

- Versionar o schema com Flyway ou Liquibase. Hoje o projeto usa `ddl-auto=update`, que não é o ideal para produção.
- Mover usuário e senha do banco para variáveis de ambiente.
- Documentar a API com Swagger (springdoc-openapi).

## O que eu pratiquei

- API REST com `@RestController`
- Arquitetura em camadas
- DTOs e mappers
- Persistência com Spring Data JPA e PostgreSQL
- Validação com Bean Validation
- Tratamento global de erros
- Testes de unidade, repositório e web
- Banco em container com Docker Compose
