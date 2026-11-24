-- ============================================================================
-- SISTEMA DE GESTÃO HOTELEIRA - HOTEL NEBULA
-- Parte 2: Script de Criação do Banco de Dados (DDL)
-- SGBD Alvo: MySQL 8.0+ (Compatível com MySQL Workbench)
-- ============================================================================

DROP DATABASE IF EXISTS hotel_nebula;
CREATE DATABASE hotel_nebula
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE hotel_nebula;

-- 1. Tabela de Hóspedes
CREATE TABLE hospedes (
    id_hospede INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(150) NOT NULL,
    documento_cpf_passaporte VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    telefone VARCHAR(25) NOT NULL,
    data_nascimento DATE NOT NULL,
    nacionalidade VARCHAR(60) NOT NULL DEFAULT 'Brasil',
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Tabela de Profissionais / Funcionários
CREATE TABLE profissionais (
    id_profissional INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    cargo ENUM('Recepcionista', 'Gerente', 'Camareira', 'Concierge', 'Manutencao', 'Auditor') NOT NULL,
    turno ENUM('Manha', 'Tarde', 'Noite', 'Madrugada') NOT NULL,
    email_corporativo VARCHAR(120) NOT NULL UNIQUE,
    data_admissao DATE NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- 3. Tabela de Categorias de Quarto (Normalização 3FN)
CREATE TABLE categorias_quarto (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome_categoria VARCHAR(60) NOT NULL UNIQUE,
    descricao TEXT,
    valor_diaria_base DECIMAL(10, 2) NOT NULL,
    capacidade_maxima INT NOT NULL,
    CONSTRAINT chk_diaria_positiva CHECK (valor_diaria_base > 0),
    CONSTRAINT chk_capacidade_positiva CHECK (capacidade_maxima > 0)
) ENGINE=InnoDB;

-- 4. Tabela de Quartos
CREATE TABLE quartos (
    id_quarto INT AUTO_INCREMENT PRIMARY KEY,
    numero_quarto VARCHAR(10) NOT NULL UNIQUE,
    andar INT NOT NULL,
    id_categoria INT NOT NULL,
    status_atual ENUM('Disponivel', 'Ocupado', 'Manutencao', 'Limpeza') NOT NULL DEFAULT 'Disponivel',
    observacoes VARCHAR(255),
    CONSTRAINT fk_quartos_categoria
        FOREIGN KEY (id_categoria) REFERENCES categorias_quarto(id_categoria)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 5. Tabela de Reservas
CREATE TABLE reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_hospede INT NOT NULL,
    id_quarto INT NOT NULL,
    data_reserva DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_checkin_previsto DATE NOT NULL,
    data_checkout_previsto DATE NOT NULL,
    quantidade_hospedes INT NOT NULL DEFAULT 1,
    status_reserva ENUM('Pendente', 'Confirmada', 'Cancelada', 'Concluida', 'No-Show') NOT NULL DEFAULT 'Confirmada',
    valor_total_estimado DECIMAL(10, 2) NOT NULL,
    CONSTRAINT chk_datas_reserva CHECK (data_checkout_previsto > data_checkin_previsto),
    CONSTRAINT chk_qtd_hospedes_reserva CHECK (quantidade_hospedes > 0),
    CONSTRAINT fk_reservas_hospede
        FOREIGN KEY (id_hospede) REFERENCES hospedes(id_hospede)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_reservas_quarto
        FOREIGN KEY (id_quarto) REFERENCES quartos(id_quarto)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 6. Tabela de Hospedagens (Check-in / Check-out efetivos)
CREATE TABLE hospedagens (
    id_hospedagem INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL UNIQUE,
    id_profissional_checkin INT NOT NULL,
    id_profissional_checkout INT NULL,
    data_checkin_real DATETIME NOT NULL,
    data_checkout_real DATETIME NULL,
    status_hospedagem ENUM('Em Andamento', 'Finalizada') NOT NULL DEFAULT 'Em Andamento',
    valor_total_final DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT chk_datas_hospedagem CHECK (data_checkout_real IS NULL OR data_checkout_real >= data_checkin_real),
    CONSTRAINT fk_hospedagens_reserva
        FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_hospedagens_prof_checkin
        FOREIGN KEY (id_profissional_checkin) REFERENCES profissionais(id_profissional)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_hospedagens_prof_checkout
        FOREIGN KEY (id_profissional_checkout) REFERENCES profissionais(id_profissional)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 7. Tabela de Catálogo de Serviços Extras
CREATE TABLE servicos_extras (
    id_servico INT AUTO_INCREMENT PRIMARY KEY,
    nome_servico VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    valor_unitario DECIMAL(10, 2) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_valor_servico CHECK (valor_unitario >= 0)
) ENGINE=InnoDB;

-- 8. Tabela Associativa: Consumo de Serviços Extras por Hospedagem (N:N)
CREATE TABLE consumo_servicos (
    id_consumo INT AUTO_INCREMENT PRIMARY KEY,
    id_hospedagem INT NOT NULL,
    id_servico INT NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    valor_unitario_cobrado DECIMAL(10, 2) NOT NULL,
    data_consumo DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_qtd_consumo CHECK (quantidade > 0),
    CONSTRAINT fk_consumo_hospedagem
        FOREIGN KEY (id_hospedagem) REFERENCES hospedagens(id_hospedagem)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_consumo_servico
        FOREIGN KEY (id_servico) REFERENCES servicos_extras(id_servico)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 9. Tabela de Pagamentos
CREATE TABLE pagamentos (
    id_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    id_hospedagem INT NOT NULL,
    data_pagamento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor_pago DECIMAL(10, 2) NOT NULL,
    metodo_pagamento ENUM('Cartao Credito', 'Cartao Debito', 'PIX', 'Dinheiro', 'Transferencia') NOT NULL,
    status_pagamento ENUM('Pendente', 'Aprovado', 'Estornado', 'Recusado') NOT NULL DEFAULT 'Aprovado',
    codigo_transacao VARCHAR(100) UNIQUE,
    CONSTRAINT chk_valor_pagamento CHECK (valor_pago > 0),
    CONSTRAINT fk_pagamentos_hospedagem
        FOREIGN KEY (id_hospedagem) REFERENCES hospedagens(id_hospedagem)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 10. Tabela de Avaliações / Comentários dos Clientes
CREATE TABLE avaliacoes (
    id_avaliacao INT AUTO_INCREMENT PRIMARY KEY,
    id_hospedagem INT NOT NULL UNIQUE,
    nota_geral TINYINT NOT NULL,
    nota_limpeza TINYINT NOT NULL,
    nota_atendimento TINYINT NOT NULL,
    comentario TEXT,
    data_avaliacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_nota_geral CHECK (nota_geral BETWEEN 1 AND 5),
    CONSTRAINT chk_nota_limpeza CHECK (nota_limpeza BETWEEN 1 AND 5),
    CONSTRAINT chk_nota_atendimento CHECK (nota_atendimento BETWEEN 1 AND 5),
    CONSTRAINT fk_avaliacoes_hospedagem
        FOREIGN KEY (id_hospedagem) REFERENCES hospedagens(id_hospedagem)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Índices adicionais para otimização de consultas analíticas e operacionais
CREATE INDEX idx_reservas_datas ON reservas (data_checkin_previsto, data_checkout_previsto, status_reserva);
CREATE INDEX idx_pagamentos_data_status ON pagamentos (data_pagamento, status_pagamento);
CREATE INDEX idx_hospedagens_status ON hospedagens (status_hospedagem);
