# Documentação da Estrutura de Dados — Hotel Nebula

##  Visão Geral
O banco de dados **`hotel_nebula`** foi projetado para centralizar e estruturar todas as operações do hotel, substituindo controles manuais e planilhas dispersas por um modelo relacional íntegro, normalizado até a 

A arquitetura separa claramente o ciclo de intenção de estadia (**Reservas**) da execução operacional real (**Hospedagens**), garantindo rastreabilidade financeira, controle de ocupação e histórico de atendimento.

---

##  Regras de Negócio Principais
* **Padronização de Tarifas (3FN):** Os valores de diária e capacidade máxima pertencem à categoria do quarto (`categorias_quarto`), evitando redundância e inconsistências ao atualizar preços.
* **Separação entre Reserva e Hospedagem:** Uma reserva (`reservas`) representa o agendamento futuro e pode ser cancelada ou resultar em *No-Show*. Apenas quando o hóspede realiza o *check-in* é gerado um registro efetivo em `hospedagens` (relação `1:0..1`).
* **Rastreabilidade de Atendimento:** Cada hospedagem registra separadamente o profissional responsável pelo *check-in* e o profissional responsável pelo *check-out* (`profissionais`).
* **Preservação de Histórico Financeiro:** Ao lançar um serviço extra consumido (`consumo_servicos`), o sistema grava o `valor_unitario_cobrado` no ato do consumo. Assim, reajustes futuros na tabela `servicos_extras` não alteram faturas antigas.
* **Pagamentos Fracionados:** Uma hospedagem pode receber múltiplos pagamentos (`1:N` com `pagamentos`), permitindo cobranças parciais (ex.: sinal via PIX no *check-in* e quitação via Cartão de Crédito no *check-out*).
* **Avaliações Verificadas:** Apenas hospedagens efetivamente realizadas podem receber avaliações (`1:0..1` com `avaliacoes`), com notas restritas de `1` a `5` via restrições `CHECK`.

---

##  Dicionário de Entidades (Tabelas)

| Tabela | Descrição | Chaves Principais |
| :--- | :--- | :--- |
| **`hospedes`** | Cadastro único de clientes (nacionais e estrangeiros). | **PK:** `id_hospede`<br>**UQ:** `documento_cpf_passaporte`, `email` |
| **`profissionais`** | Colaboradores do hotel, cargos e turnos de trabalho. | **PK:** `id_profissional`<br>**UQ:** `cpf`, `email_corporativo` |
| **`categorias_quarto`** | Tipos de acomodação, capacidade e tarifa base. | **PK:** `id_categoria`<br>**UQ:** `nome_categoria` |
| **`quartos`** | Unidades habitacionais físicas, andar e status operacional. | **PK:** `id_quarto`<br>**FK:** `id_categoria` $\rightarrow$ `categorias_quarto` |
| **`reservas`** | Agendamentos prévios vinculados a hóspede, quarto e datas. | **PK:** `id_reserva`<br>**FK:** `id_hospede`, `id_quarto` |
| **`hospedagens`** | Estadias efetivas iniciadas no *check-in* e encerradas no *check-out*. | **PK:** `id_hospedagem`<br>**FK:** `id_reserva` (UQ), `id_profissional_checkin`, `id_profissional_checkout` |
| **`servicos_extras`** | Catálogo de serviços adicionais (Spa, Café, Transfer, Valet etc.). | **PK:** `id_servico`<br>**UQ:** `nome_servico` |
| **`consumo_servicos`** | Tabela associativa (`N:N`) que registra os serviços consumidos em cada estadia. | **PK:** `id_consumo`<br>**FK:** `id_hospedagem`, `id_servico` |
| **`pagamentos`** | Transações financeiras vinculadas às hospedagens. | **PK:** `id_pagamento`<br>**FK:** `id_hospedagem`<br>**UQ:** `codigo_transacao` |
| **`avaliacoes`** | Notas (geral, limpeza, atendimento) e comentários pós-estadia. | **PK:** `id_avaliacao`<br>**FK:** `id_hospedagem` (UQ) |

---

##  Integridade, Restrições e Performance
* **Integridade Referencial (`FOREIGN KEY`):** Todas as chaves estrangeiras utilizam `ON UPDATE CASCADE` e políticas restritivas (`ON DELETE RESTRICT`) em cadastros mestres para impedir a exclusão acidental de hóspedes, quartos ou profissionais com histórico vinculado.
* **Validação de Domínio (`CHECK` e `ENUM`):**
  * Datas validadas para impedir *check-out* anterior ao *check-in*.
  * Valores monetários e quantidades validados para impedir números negativos ou nulos indevidos.
  * Status padronizados via `ENUM` (ex.: `Disponivel`, `Ocupado`, `Manutencao`, `Limpeza`).
* **Índices de Otimização (`INDEX`):** Criados índices compostos sobre datas de reserva, status de pagamento e status de hospedagem para acelerar relatórios gerenciais e buscas de disponibilidade.

---

##  Ordem de Execução dos Scripts
Para implantar e testar o ambiente no **MySQL Workbench**, execute os arquivos na seguinte sequência:
1. `DDL.sql` — Cria o banco, tabelas, chaves, restrições e índices.
2. `DML.sql` — Popula o banco com dados fictícios consistentes para testes.
3. `DQL.sql` — Executa as consultas analíticas e operacionais do Radar de Comando.
