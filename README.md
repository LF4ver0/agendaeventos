# 📌 Sistema de Gerenciamento de Eventos

API REST desenvolvida em Java + Quarkus para gerenciamento de instituições e eventos, com controle automático de vigência, validações avançadas e tratamento global de erros.

**Projeto desenvolvido como parte de desafio técnico.**

---

## 📑 Índice

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Descrição do Sistema](#-descrição-do-sistema)
- [Arquitetura](#-arquitetura)
- [Modelo de Dados](#-modelo-de-dados)
- [Endpoints da API](#-endpoints-da-api)
- [Validações](#-validações)
- [Agendamento Automático](#-agendamento-automático)
- [Tratamento de Exceções](#-tratamento-de-exceções)
- [Como Executar](#-como-executar)
- [Documentação da API](#-documentação-da-api-swagger)
- [Testes](#-testes)
- [Build e Deploy](#-build-e-deploy)
- [Estrutura do Projeto](#-estrutura-do-projeto)

---

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem de programação
- **Quarkus 3.31.2** - Framework supersônico e subatômico
- **Hibernate ORM + Panache** - Persistência de dados simplificada
- **RESTEasy (Quarkus REST)** - API REST com Jackson
- **Quarkus Scheduler** - Agendamento de tarefas (cron)
- **Bean Validation** - Validação de dados
- **SmallRye OpenAPI** - Documentação automática (Swagger)
- **Lombok** - Redução de boilerplate

### Banco de Dados
- **MySQL** - Sistema de gerenciamento de banco de dados

### Padrões e Arquitetura
- **DTO Pattern** - Transferência de dados
- **Global Exception Handler** - Tratamento centralizado de erros
- **Arquitetura em Camadas** - Separação de responsabilidades
- **Repository Pattern** - Acesso a dados via Panache
- **Logs JSON** - Logging estruturado

---

## 📚 Descrição do Sistema

O sistema permite o gerenciamento completo de eventos vinculados a instituições, com as seguintes funcionalidades:

### Funcionalidades Principais

- ✅ **Cadastrar instituições** (pré-cadastradas via import.sql)
- ✅ **Criar eventos** vinculados a uma instituição
- ✅ **Definir período de vigência** (data inicial e final)
- ✅ **Ativar e inativar eventos automaticamente** via scheduler
- ✅ **Operações CRUD completas** para eventos
- ✅ **Paginação** na listagem de eventos
- ✅ **Validações customizadas** de negócio
- ✅ **Tratamento global de exceções**

### Regras de Negócio

- O status do evento (`ativo`) é controlado automaticamente com base na data atual
- Um evento só pode estar ativo se a data atual estiver entre `dataInicial` e `dataFinal`
- A data final deve ser maior ou igual à data inicial
- Eventos não podem ser criados com datas passadas
- Todo evento deve estar vinculado a uma instituição existente

---

## 🏗️ Arquitetura

O projeto segue uma **arquitetura em camadas** bem definida:

```
src/main/java/br/com/lfavero/
├── controller/          → Camada de apresentação (REST endpoints)
├── service/             → Camada de lógica de negócio
├── entity/              → Camada de persistência (entidades JPA)
├── web/dto/             → DTOs para request/response
│   ├── request/         → DTOs de entrada
│   └── response/        → DTOs de saída
├── mapper/              → Conversão entre entidades e DTOs
├── validation/          → Validações customizadas
├── scheduling/          → Tarefas agendadas (scheduler)
└── exception/           → Tratamento de exceções
```

### Principais Módulos

| Módulo | Responsabilidade |
|--------|-----------------|
| **entity** | Mapeamento das entidades do banco de dados |
| **controller** | Exposição dos endpoints REST |
| **service** | Implementação das regras de negócio |
| **dto** | Transferência de dados entre camadas |
| **validation** | Validações customizadas de negócio |
| **scheduling** | Tarefas agendadas (atualização de status) |
| **exception** | Tratamento centralizado de erros |
| **mapper** | Conversão entre entidades e DTOs |

---

## 📊 Modelo de Dados

### Entidade: Instituição (instituicao)

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long (PK) | Identificador único |
| nome | String | Nome da instituição |
| tipo | String | Tipo da instituição |

**Instituições Pré-cadastradas:**
- Confederação
- Singular
- Central
- Cooperativa

### Entidade: Evento (evento)

| Campo | Tipo | Constraints | Descrição |
|-------|------|-------------|-----------|
| id | Long (PK) | Auto-increment | Identificador único |
| nomeEvento | String | NotBlank, MinSize(5) | Nome do evento |
| dataInicial | LocalDate | NotNull, FutureOrPresent | Data de início |
| dataFinal | LocalDate | NotNull, FutureOrPresent | Data de término |
| ativo | Boolean | - | Status do evento |
| institution_id | Long (FK) | NotNull | Referência à instituição |

### Relacionamentos

```
instituicao (1) ─────────< (N) evento
```

- Uma instituição pode possuir **vários eventos** (OneToMany)
- Um evento pertence a **uma instituição** (ManyToOne)
- Relacionamento obrigatório (NOT NULL)
- Fetch Lazy para otimização

---

## 🔗 Endpoints da API

### Base URL
```
http://localhost:8080
```

### 📍 Instituições

#### Listar todas as instituições
```http
GET /institutions
```

**Resposta de Sucesso (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Confederação",
    "tipo": "Confederação"
  }
]
```

> **Nota:** As instituições são criadas automaticamente durante a inicialização do projeto através do arquivo `import.sql`.

---

### 📍 Eventos

#### Listar todos os eventos (com paginação)
```http
GET /events?page=0&pageSize=10
```

**Parâmetros Query:**
- `page` (opcional, default=0) - Número da página
- `pageSize` (opcional, default=10) - Quantidade de itens por página

**Resposta de Sucesso (200 OK):**
```json
[
  {
    "idEvento": 1,
    "nomeEvento": "Congresso Nacional 2025",
    "dataInicialEvento": "2025-03-15",
    "dataFinalEvento": "2025-03-17",
    "eventoAtivo": true,
    "instituicao": {
      "id": 1,
      "nome": "Confederação"
    }
  }
]
```

---

#### Buscar evento por ID
```http
GET /events/{id}
```

**Resposta de Sucesso (200 OK):**
```json
{
  "idEvento": 1,
  "nomeEvento": "Congresso Nacional 2025",
  "dataInicialEvento": "2025-03-15",
  "dataFinalEvento": "2025-03-17",
  "eventoAtivo": true,
  "instituicao": {
    "id": 1,
    "nome": "Confederação"
  }
}
```

**Resposta de Erro (404 Not Found):**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Evento não encontrado"
}
```

---

#### Criar novo evento
```http
POST /events
Content-Type: application/json
```

**Request Body:**
```json
{
  "nomeEvento": "Workshop de Inovação",
  "dataInicialEvento": "2025-04-10",
  "dataFinalEvento": "2025-04-12",
  "institutionId": 1
}
```

**Validações:**
- `nomeEvento`: obrigatório, mínimo 5 caracteres
- `dataInicialEvento`: obrigatória, não pode ser passada
- `dataFinalEvento`: obrigatória, não pode ser passada, deve ser >= dataInicial
- `institutionId`: obrigatório, deve existir

**Resposta de Sucesso (200 OK):**
```json
{
  "idEvento": 2,
  "nomeEvento": "Workshop de Inovação",
  "dataInicialEvento": "2025-04-10",
  "dataFinalEvento": "2025-04-12",
  "eventoAtivo": false,
  "instituicao": {
    "id": 1,
    "nome": "Confederação"
  }
}
```

**Resposta de Erro de Validação (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Nome deve ter no mínimo 5 caracteres"
}
```

---

#### Atualizar evento
```http
PUT /events/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "nomeEvento": "Workshop de Inovação Tecnológica",
  "dataInicialEvento": "2025-04-10",
  "dataFinalEvento": "2025-04-13"
}
```

**Resposta de Sucesso (200 OK):**
```json
{
  "idEvento": 2,
  "nomeEvento": "Workshop de Inovação Tecnológica",
  "dataInicialEvento": "2025-04-10",
  "dataFinalEvento": "2025-04-13",
  "eventoAtivo": false,
  "instituicao": {
    "id": 1,
    "nome": "Confederação"
  }
}
```

---

#### Deletar evento
```http
DELETE /events/{id}
```

**Resposta de Sucesso (204 No Content):**
```
(sem corpo de resposta)
```

---

## ✅ Validações

O sistema implementa validações em múltiplas camadas:

### Validações Bean Validation (Jakarta)

#### CreateEventRequestDto
```java
- nomeEvento:
  ✓ @NotBlank - Não pode ser vazio
  ✓ @Size(min=5) - Mínimo 5 caracteres
  
- dataInicialEvento:
  ✓ @NotNull - Obrigatório
  ✓ @FutureOrPresent - Não pode ser passada
  
- dataFinalEvento:
  ✓ @NotNull - Obrigatório
  ✓ @FutureOrPresent - Não pode ser passada
  
- institutionId:
  ✓ @NotNull - Obrigatório
  ✓ @Positive - Deve ser número positivo
```

### Validações Customizadas

#### @ValidDateRange
Validação customizada que garante que a data final seja maior ou igual à data inicial:

```java
@ValidDateRange
public class CreateEventRequestDto {
    // Valida: dataFinalEvento >= dataInicialEvento
}
```

**Implementação:** `DateRangeValidator.java`

### Validações de Negócio

Implementadas na camada de serviço (`EventValidations.java`):

- ✓ Instituição deve existir no banco de dados
- ✓ Evento não pode ter nome duplicado
- ✓ Relacionamentos (FK) devem ser válidos

---

## ⏱️ Agendamento Automático

O sistema utiliza o **Quarkus Scheduler** para gerenciar automaticamente o status dos eventos.

### Classe: `EventsScheduling.java`

**Configuração do Cron:**
```java
@Scheduled(cron = "0 0 0 * * ?")  // Executa diariamente à meia-noite
```

### Lógica de Ativação/Desativação

```java
LocalDate today = LocalDate.now();

for (EventsEntity event : events) {
    // Ativa se: hoje >= dataInicial AND hoje <= dataFinal
    boolean activate = !today.isBefore(event.initialDate) 
                    && !today.isAfter(event.finalDate);
    
    if (event.active != activate) {
        event.active = activate;
        event.persist();
    }
}
```

### Comportamento

- ✅ Eventos são **ativados** automaticamente quando a data inicial é atingida
- ✅ Eventos são **desativados** automaticamente após a data final
- ✅ Execução **diária à meia-noite** (00:00:00)
- ✅ Logs estruturados de início e fim da execução
- ✅ Transações gerenciadas automaticamente

**Logs Gerados:**
```
INFO: Scheduler Iniciando: 2025-02-13T00:00:00
INFO: Scheduler Finalizado: 2025-02-13T00:00:01
```

---

## 🛡️ Tratamento de Exceções

O sistema implementa um **tratamento global de exceções** através do `GlobalExceptionMapper`.

### Exceções Tratadas

| Tipo de Erro | Status HTTP | Resposta |
|--------------|-------------|----------|
| **ConstraintViolationException** | 400 | Erro de validação com mensagem específica |
| **JsonParseException** | 400 | "Formato de JSON inválido" |
| **EventNotFoundException** | 404 | "Evento não encontrado" |
| **PersistenceException (UK)** | 409 | "Registro duplicado" |
| **PersistenceException (FK)** | 400 | "Relacionamento inválido" |
| **PersistenceException (outros)** | 500 | "Erro ao acessar banco de dados" |
| **WebApplicationException** | Variável | Mensagem específica da exceção |
| **Exception (genérica)** | 500 | "Erro interno no servidor" |

### Formato de Resposta de Erro

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Nome deve ter no mínimo 5 caracteres"
}
```

### Logging

Todas as exceções são automaticamente logadas:
```java
LOG.error("Erro capturado:", exception);
```

---

## ▶️ Como Executar

### Pré-requisitos

- **Java 21+** ([Download JDK](https://www.oracle.com/java/technologies/downloads/))
- **Docker** (opcional, para container do MySQL)

---

### 1. Executar aplicação

```bash
# Clonar o repositório
git clone https://github.com/LF4ver0/agendaeventos.git

# Entrar no diretório
cd agendaeventos

# Executar em modo desenvolvimento (hot reload)
./mvnw quarkus:dev

# Ou no Windows
mvnw.cmd quarkus:dev

#### 3. Executar aplicação

```bash
./mvnw quarkus:dev
```

---

### Verificar se está funcionando

Após iniciar a aplicação, você verá:
```
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   

Listening on: http://localhost:8080
```

**Testar endpoints:**
```bash
# Listar instituições
curl http://localhost:8080/institutions

# Listar eventos
curl http://localhost:8080/events
```

---

## 📘 Documentação da API (Swagger)

A API possui documentação interativa gerada automaticamente pelo **SmallRye OpenAPI**.

### Acessar Swagger UI

```
http://localhost:8080/q/swagger-ui
```

### Recursos Disponíveis

- 📖 Documentação completa de todos os endpoints
- 🧪 Testador interativo de requisições
- 📋 Schemas dos DTOs
- 🔍 Exemplos de request/response
- ✅ Descrição de validações

### OpenAPI Spec (JSON)

```
http://localhost:8080/q/openapi
```

---

## 🧪 Testes (⚠️ para implementação posterior)

### Executar Testes

```bash
# Executar todos os testes
./mvnw test

# Executar com relatório de cobertura
./mvnw verify

# Executar testes de integração
./mvnw verify -Pit
```

### Estrutura de Testes

```
src/test/java/br/com/lfavero/
├── controller/          → Testes de endpoints
├── service/             → Testes de lógica de negócio
└── validation/          → Testes de validações
```

---

## 🏗️ Build e Deploy

### Modo Desenvolvimento
```bash
# Com hot reload
./mvnw quarkus:dev
```
---

## 📁 Estrutura do Projeto

```
agendaeventos/
├── .mvn/                          # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── docker/                # Dockerfiles
│   │   │   ├── Dockerfile.jvm
│   │   │   ├── Dockerfile.native
│   │   │   ├── Dockerfile.legacy-jar
│   │   │   └── Dockerfile.native-micro
│   │   ├── java/br/com/lfavero/
│   │   │   ├── controller/        # REST Controllers
│   │   │   │   ├── EventsController.java
│   │   │   │   └── InstitutionController.java
│   │   │   ├── entity/            # Entidades JPA
│   │   │   │   ├── EventsEntity.java
│   │   │   │   └── InstitutionEntity.java
│   │   │   ├── exception/         # Exceções customizadas
│   │   │   │   ├── EventNotFoundException.java
│   │   │   │   └── GlobalExceptionMapper.java
│   │   │   ├── mapper/            # Conversores DTO <-> Entity
│   │   │   │   └── EventMapper.java
│   │   │   ├── scheduling/        # Tarefas agendadas
│   │   │   │   └── EventsScheduling.java
│   │   │   ├── service/           # Lógica de negócio
│   │   │   │   └── EventsService.java
│   │   │   ├── validation/        # Validações customizadas
│   │   │   │   ├── DateRangeValidator.java
│   │   │   │   ├── EventValidations.java
│   │   │   │   └── ValidDateRange.java
│   │   │   └── web/dto/           # Data Transfer Objects
│   │   │       ├── request/
│   │   │       │   ├── CreateEventRequestDto.java
│   │   │       │   └── UpdateEventRequestDto.java
│   │   │       └── response/
│   │   │           ├── ErrorResponse.java
│   │   │           └── EventResponseDto.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── import.sql         # Dados iniciais
│   └── test/
│       └── java/br/com/lfavero/   # Testes
├── target/                         # Arquivos compilados
├── .dockerignore
├── .gitignore
├── mvnw                            # Maven Wrapper (Linux/Mac)
├── mvnw.cmd                        # Maven Wrapper (Windows)
├── pom.xml                         # Configuração Maven
└── README.md
```
---

## 📝 Notas Importantes

- ⚠️ O modo `drop-and-create` do Hibernate recria o banco a cada inicialização (ideal para desenvolvimento)
- ⚠️ Para produção, altere para `update` ou `validate`
- ⚠️ As instituições são criadas automaticamente via `import.sql`
- ⚠️ O scheduler executa **diariamente à meia-noite** (00:00:00)
- ⚠️ Certifique-se de que o MySQL está rodando antes de iniciar a aplicação, normalmente basta ter o docker instalado e em execução que o próprio Quarkus cuida do resto :)

---

## 📞 Contato

**Autor:** Lucas Favero  
**GitHub:** [LF4ver0](https://github.com/LF4ver0)  
**Repositório:** [agendaeventos](https://github.com/LF4ver0/agendaeventos)

---

## 📄 Licença

Este projeto foi desenvolvido como parte de um desafio técnico.

---

## 🎯 Próximos Passos

- [ ] Implementar testes unitários completos
- [ ] Adicionar testes de integração
- [ ] Implementar cache (Redis)
- [ ] Adicionar autenticação JWT
- [ ] Criar dashboard frontend
- [ ] Implementar notificações de eventos
- [ ] Adicionar métricas (Micrometer)
- [ ] Deploy em cloud (AWS/Azure/GCP)

---