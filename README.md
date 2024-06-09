# Jogo da Velha com RMI

## Alunos

Caio Vinicius de Souza Costa - 83647

Diogo Vitor de Oliveira Leme - 836846

## Descrição

Este repositório contém uma aplicação completa para gerenciamento de reservas de salas, composta por três principais componentes:

1. **API (Back-end)**: Desenvolvida com FastAPI, uma moderna framework para construção de APIs em Python.
2. **Banco de Dados**: Utiliza PostgreSQL para armazenar e gerenciar todos os dados da aplicação de forma segura e eficiente.
3. **Aplicativo Mobile (Front-end)**: Um app móvel desenvolvido em Kotlin, fornecendo uma interface amigável para usuários realizarem reservas de salas.

## Componentes

### 1. API (Back-end) - FastAPI
A API é responsável por gerenciar todas as operações do sistema, incluindo a criação, atualização, leitura e exclusão de reservas de salas. Ela foi desenvolvida utilizando FastAPI devido ao seu desempenho, facilidade de uso e suporte para tipagem estática.

#### Principais Recursos:
- Criação de professores, salas e reservas
- Atualização de cadastro existentes
- Listagem de cadastros existentes
- Exclusão de cadastros existentes

### 2. Banco de Dados - PostgreSQL
O PostgreSQL é utilizado como o banco de dados principal da aplicação. Ele armazena todas as informações de reservas, usuários e salas. A escolha pelo PostgreSQL se deu pela sua robustez, segurança e capacidade de lidar com grandes volumes de dados.

#### Estrutura Básica:
- Tabela de Professores
- Tabela de Salas
- Tabela de Reservas

### 3. Aplicativo Mobile (Front-end) - Kotlin
O aplicativo móvel, desenvolvido em Kotlin, fornece uma interface intuitiva e eficiente para que os usuários possam realizar reservas de salas diretamente de seus dispositivos móveis. O app comunica-se com a API para realizar todas as operações necessárias.

#### Principais Funcionalidades:
- Visualização de professores, reservas e salas
- Criação de novos cadastros
- Realização de reservas
- Edição de cadastros existentes
- Exclusão de cadastros existentes
