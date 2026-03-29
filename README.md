# 🎵 Projeto Back End – MediaHub API (Usuários e Álbuns)

> 🚧 **Status do Projeto:** Em desenvolvimento  
> Este projeto está sendo desenvolvido de forma incremental, com foco em boas práticas, arquitetura escalável e simulação de um ambiente real de produção.  
> Novas funcionalidades estão sendo adicionadas continuamente.

API REST desenvolvida com foco em boas práticas de engenharia de software, arquitetura limpa, segurança com JWT, versionamento de banco de dados e facilidade de evolução.

A aplicação simula um ambiente real de produção, incluindo autenticação robusta, separação de responsabilidades, versionamento de schema e preparação para integrações futuras (MinIO, APIs externas e Docker).

---

## 📌 Índice

1. [Objetivo do projeto](#objetivo-do-projeto)
2. [Arquitetura adotada](#arquitetura-adotada)
3. [Modelagem de domínio](#modelagem-de-domínio)
4. [Segurança e autenticação](#segurança-e-autenticação)
5. [Versionamento do banco de dados](#versionamento-do-banco-de-dados)
6. [Estratégia de testes](#estratégia-de-testes)
7. [Decisões técnicas relevantes](#decisões-técnicas-relevantes)
8. [O que não foi priorizado](#o-que-não-foi-priorizado)
9. [Instalação e execução](#instalação-e-execução)
10. [Endpoints principais](#endpoints-principais)
11. [Considerações finais](#considerações-finais)

---

## 🎯 Objetivo do projeto

Construir uma API REST para gerenciamento de usuários e álbuns musicais, priorizando:

* Código limpo e organizado
* Separação clara de responsabilidades
* Segurança com autenticação stateless (JWT)
* Evolução segura do banco de dados
* Facilidade de manutenção e testes

---

## 🏗 Arquitetura adotada

A aplicação segue arquitetura em camadas:

* **Controller:** exposição dos endpoints e tratamento HTTP
* **Service:** regras de negócio e orquestração
* **Repository:** acesso ao banco com Spring Data JPA
* **DTOs:** separação entre domínio e contrato da API
* **Security:** autenticação e validação de tokens JWT

Essa abordagem reduz acoplamento e facilita evolução do sistema.

---

## 🧩 Modelagem de domínio

### Entidades principais:

* **User**
* **Album**

### Regras aplicadas:

* Usuários possuem identificação via UUID
* Álbuns possuem relacionamento com usuário (autor/criador)
* Uso de DTOs para evitar exposição direta das entidades

Essa modelagem permite evolução futura sem quebrar contratos da API.

---

## 🔐 Segurança e autenticação

A autenticação é baseada em **JWT (JSON Web Token)**.

### ✔ Access Token

* Expiração curta (5 minutos)
* Usado para acessar endpoints protegidos

### ✔ Refresh Token

* Permite gerar novo access token sem login
* Não é retornado novamente no refresh (boa prática)

---

### 🔁 Fluxo de autenticação

```
Login → accessToken + refreshToken
       ↓
Access expira
       ↓
POST /v1/auth/refresh
       ↓
Novo accessToken
```

---

## 🗄 Versionamento do banco de dados

Foi utilizado **Flyway**:

* Migrations versionadas (V1, V2...)
* Estrutura imutável
* Evolução incremental
* Banco recriável do zero

```
src/main/resources/db/migration
```

---

## 🧪 Estratégia de testes

Planejado para cobertura das camadas principais:

* Services (regras de negócio)
* Controllers (HTTP)
* Fluxos de autenticação

Foco em validar comportamento e não implementação.

---

## ⚙️ Decisões técnicas relevantes

* Uso de DTOs para proteção do domínio
* JWT stateless (sem sessão)
* Separação por camadas
* Versionamento de banco com Flyway
* Uso de UUID para usuários
* Paginação com Spring Data

---

## 🚫 O que não foi priorizado

Itens fora do escopo atual:

* Cache distribuído
* Observabilidade (logs estruturados, métricas)
* Autorização avançada (roles/perfis)

O foco foi garantir uma base sólida e escalável.

---

## ⚙️ Instalação e execução

### Pré-requisitos

* Java 21
* Maven
* PostgreSQL

---

### 1️⃣ Clonar projeto

```bash
git clone https://github.com/seu-usuario/mediahub-api.git
cd mediahub-api
```

---

### 2️⃣ Configurar banco

```sql
CREATE DATABASE mediahub;
```

---

### 3️⃣ Variáveis de ambiente

```bash
setx JWT_SECRET "mediahub-super-secret-key-2026"
setx JWT_EXPIRATION "300000"
setx JWT_REFRESH_EXPIRATION "900000"
```

---

### 4️⃣ Rodar aplicação

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

---

## 🚀 Endpoints principais

### 🔑 Login

```http
POST /v1/auth/login
```

### 🔁 Refresh Token

```http
POST /v1/auth/refresh
```

---

### 🎵 Álbuns

| Método | Endpoint        | Descrição            |
| ------ | --------------- | -------------------- |
| POST   | /v1/albums      | Criar álbum          |
| GET    | /v1/albums      | Listar com paginação |
| GET    | /v1/albums/{id} | Buscar por ID        |
| PUT    | /v1/albums/{id} | Atualizar            |
| DELETE | /v1/albums/{id} | Remover              |

---

## 🔐 Autorização

Todos endpoints protegidos utilizam:

```
Authorization: Bearer {accessToken}
```

---

## 📌 Exemplo de resposta (Login)

```json
{
  "accessToken": "token",
  "refreshToken": "token",
  "expiresIn": 300
}
```

---

## 📌 Exemplo de resposta (Refresh)

```json
{
  "accessToken": "novo_token",
  "expiresIn": 300
}
```

---

## 🧠 Considerações finais

Este projeto foi desenvolvido com foco em simular um ambiente real:

* Código limpo e organizado
* Facilidade de manutenção
* Segurança aplicada corretamente
* Base preparada para evolução

Cada decisão técnica foi tomada visando clareza, simplicidade e boas práticas de engenharia de software.
