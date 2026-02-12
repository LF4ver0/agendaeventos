# 📌 Sistema de Gerenciamento de Eventos

Este projeto foi desenvolvido como parte de um **desafio técnico para Desenvolvedor Java**, com o objetivo de implementar um sistema CRUD para gerenciamento de instituições e eventos, com controle automático de vigência.

---

## 🚀 Tecnologias Utilizadas

### Backend
- Java 17+
- Quarkus
- Hibernate ORM (Panache)
- REST API
- Scheduler (Agendamento de tarefas)

### Banco de Dados
- MySQL

### Outros
- DTO Pattern
- Arquitetura em Camadas
- Validações customizadas

---

## 📚 Descrição do Sistema

O sistema permite:

- Cadastrar instituições
- Cadastrar eventos vinculados a uma instituição
- Definir período de vigência (data inicial e final)
- Ativar e inativar eventos automaticamente
- Realizar operações CRUD completas

O status do evento é controlado automaticamente com base na data atual.

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:


Principais módulos:

- `entity` → Mapeamento das entidades
- `controller` → Endpoints REST
- `service` → Regras de negócio
- `dto` → Transferência de dados
- `validation` → Validações
- `scheduling` → Tarefas agendadas

---

## 📊 Modelo de Dados

### Instituição
| Campo | Tipo   |
|-------|--------|
| id    | Long   |
| nome  | String |
| tipo  | String |

### Evento
| Campo       | Tipo    |
|-------------|---------|
| id          | Long    |
| nome        | String  |
| dataInicial | Date    |
| dataFinal   | Date    |
| ativo       | Boolean |
| instituicao | FK      |

Relacionamento:
- Uma instituição pode possuir vários eventos (OneToMany)

---

## 🔗 Endpoints Principais

### Instituições

| Método | Endpoint              | Descrição                 |
|--------|-----------------------|---------------------------|
| GET    | /institutions         | Listar instituições       |

Como o objetivo principal do projeto estava nos controle dos eventos, as instituições são criadas por padrão durante a inicialização do projeto.

### Eventos

| Método | Endpoint              | Descrição                 |
|--------|-----------------------|---------------------------|
| GET    | /events               | Listar eventos             |
| POST   | /events               | Criar evento               |
| PUT    | /events/{id}          | Atualizar evento           |
| DELETE | /events/{id}          | Remover evento             |

---

## ⏱️ Agendamento Automático

O sistema utiliza tarefas agendadas para:

- Ativar eventos quando a data inicial é atingida
- Inativar eventos após a data final

Classe responsável:
Executa periodicamente para validar a vigência dos eventos.

---

## ✅ Validações

As principais validações incluem:

- Data inicial menor que data final
- Campos obrigatórios
- Vínculo válido com instituição
- Regras de negócio centralizadas

## ▶️ Como Executar o Projeto

### Pré-requisitos

- Java 17+
- Maven
- Docker (para container do MySQL)

### Passos

```bash
# Clonar o projeto
git clone https://github.com/LF4ver0/agendaeventos.git

# Entrar no diretório
cd projeto

# Executar
./mvnw quarkus:dev
