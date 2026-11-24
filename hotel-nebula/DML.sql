-- ============================================================================
-- SISTEMA DE GESTÃO HOTELEIRA - HOTEL NEBULA
-- Parte 2: Script de Inserção de Dados Iniciais para Testes (DML)
-- SGBD Alvo: MySQL 8.0+ (Compatível com MySQL Workbench)
-- ============================================================================

USE hotel_nebula;

-- 1. Inserindo Categorias de Quarto
INSERT INTO categorias_quarto (nome_categoria, descricao, valor_diaria_base, capacidade_maxima) VALUES
('Nebula Standard', 'Quarto aconchegante com cama queen, Wi-Fi de alta velocidade e mesa de trabalho.', 280.00, 2),
('Nebula Deluxe', 'Quarto amplo com varanda panorâmica, cama king size e cafeteira espresso.', 450.00, 3),
('Suíte Orbital', 'Suíte executiva com sala de estar separada, banheira de hidromassagem e vista privilegiada.', 780.00, 4),
('Cobertura Galaxy', 'Suíte presidencial duplex com terraço privativo, jacuzzi e serviço de mordomo.', 1450.00, 4);

-- 2. Inserindo Quartos
INSERT INTO quartos (numero_quarto, andar, id_categoria, status_atual, observacoes) VALUES
('101', 1, 1, 'Disponivel', 'Próximo ao elevador social'),
('102', 1, 1, 'Ocupado', 'Hóspede em estadia'),
('103', 1, 1, 'Disponivel', NULL),
('201', 2, 2, 'Disponivel', 'Vista para o jardim'),
('202', 2, 2, 'Limpeza', 'Em higienização pós check-out'),
('203', 2, 2, 'Disponivel', NULL),
('301', 3, 3, 'Ocupado', 'Vista panorâmica norte'),
('302', 3, 3, 'Manutencao', 'Revisão no ar-condicionado central'),
('401', 4, 4, 'Disponivel', 'Cobertura principal');

-- 3. Inserindo Profissionais / Funcionários
INSERT INTO profissionais (nome_completo, cpf, cargo, turno, email_corporativo, data_admissao, ativo) VALUES
('Mariana Costa Silva', '111.222.333-44', 'Recepcionista', 'Manha', 'mariana.costa@hotelnebula.com.br', '2023-03-15', TRUE),
('Lucas Mendes Oliveira', '222.333.444-55', 'Recepcionista', 'Tarde', 'lucas.mendes@hotelnebula.com.br', '2023-07-01', TRUE),
('Fernanda Alves Rocha', '333.444.555-66', 'Gerente', 'Manha', 'fernanda.rocha@hotelnebula.com.br', '2022-01-10', TRUE),
('Carlos Eduardo Santos', '444.555.666-77', 'Concierge', 'Tarde', 'carlos.santos@hotelnebula.com.br', '2024-02-20', TRUE),
('Juliana Pereira Lima', '555.666.777-88', 'Recepcionista', 'Noite', 'juliana.lima@hotelnebula.com.br', '2024-05-12', TRUE);

-- 4. Inserindo Hóspedes
INSERT INTO hospedes (nome_completo, documento_cpf_passaporte, email, telefone, data_nascimento, nacionalidade) VALUES
('Rafael Augusto Nogueira', '384.910.228-01', 'rafael.nogueira@email.com', '+55 11 98765-4321', '1988-04-12', 'Brasil'),
('Camila Beatriz Ferreira', '412.559.870-19', 'camila.ferreira@email.com', '+55 21 99812-3344', '1992-09-25', 'Brasil'),
('Arthur Pendelton', 'UK99482710', 'arthur.pendelton@ukmail.co.uk', '+44 20 7946 0958', '1980-11-03', 'Reino Unido'),
('Beatriz Helena Vasconcelos', '290.118.445-77', 'beatriz.vasconcelos@email.com', '+55 31 99123-7788', '1995-02-18', 'Brasil'),
('Diego Armando Morales', 'AR77361922', 'diego.morales@correo.ar', '+54 11 4555-8910', '1985-07-30', 'Argentina'),
('Sofia Linhares Prado', '501.883.920-33', 'sofia.prado@email.com', '+55 11 97654-1122', '1998-12-05', 'Brasil');

-- 5. Inserindo Catálogo de Serviços Extras
INSERT INTO servicos_extras (nome_servico, descricao, valor_unitario, ativo) VALUES
('Café da Manhã Executivo', 'Buffet completo no restaurante panorâmico', 65.00, TRUE),
('Spa & Massagem Relaxante', 'Sessão de 60 minutos no Nebula Spa', 220.00, TRUE),
('Transfer Aeroporto VIP', 'Transporte executivo ida ou volta', 180.00, TRUE),
('Lavanderia Expressa', 'Lavagem e passadoria no mesmo dia (por peça)', 45.00, TRUE),
('Room Service - Jantar', 'Menu à la carte servido no quarto', 130.00, TRUE),
('Estacionamento Valet (Diária)', 'Manobrista e vaga coberta', 50.00, TRUE);

-- 6. Inserindo Reservas (Cobrindo vários meses de 2026, cancelamentos e recorrências)
INSERT INTO reservas (id_hospede, id_quarto, data_reserva, data_checkin_previsto, data_checkout_previsto, quantidade_hospedes, status_reserva, valor_total_estimado) VALUES
(1, 1, '2026-07-10 14:20:00', '2026-08-05', '2026-08-08', 2, 'Concluida', 840.00),
(2, 4, '2026-07-15 09:45:00', '2026-08-12', '2026-08-16', 2, 'Concluida', 1800.00),
(1, 7, '2026-08-20 18:10:00', '2026-09-10', '2026-09-13', 2, 'Concluida', 2340.00),
(3, 9, '2026-08-25 11:00:00', '2026-09-18', '2026-09-22', 3, 'Concluida', 5800.00),
(4, 2, '2026-09-02 16:30:00', '2026-09-25', '2026-09-27', 1, 'Cancelada', 560.00),
(2, 5, '2026-09-10 10:15:00', '2026-10-03', '2026-10-06', 2, 'Concluida', 1350.00),
(5, 3, '2026-09-18 20:00:00', '2026-10-14', '2026-10-18', 2, 'Concluida', 1120.00),
(6, 6, '2026-10-01 13:50:00', '2026-10-20', '2026-10-23', 2, 'Cancelada', 1350.00),
(1, 4, '2026-10-12 15:40:00', '2026-11-02', '2026-11-05', 2, 'Concluida', 1350.00),
(3, 2, '2026-11-01 08:30:00', '2026-11-22', '2026-11-27', 2, 'Concluida', 1400.00),
(5, 7, '2026-11-05 19:10:00', '2026-11-23', '2026-11-28', 3, 'Concluida', 3900.00),
(4, 1, '2026-11-10 12:00:00', '2026-12-10', '2026-12-15', 2, 'Confirmada', 1400.00),
(6, 9, '2026-11-15 17:25:00', '2026-12-20', '2026-12-26', 4, 'Confirmada', 8700.00);

-- 7. Inserindo Hospedagens (Para reservas concluídas ou em andamento)
INSERT INTO hospedagens (id_reserva, id_profissional_checkin, id_profissional_checkout, data_checkin_real, data_checkout_real, status_hospedagem, valor_total_final) VALUES
(1, 1, 2, '2026-08-05 14:15:00', '2026-08-08 11:30:00', 'Finalizada', 1020.00),
(2, 2, 1, '2026-08-12 15:00:00', '2026-08-16 12:00:00', 'Finalizada', 2220.00),
(3, 1, 1, '2026-09-10 14:05:00', '2026-09-13 11:45:00', 'Finalizada', 2800.00),
(4, 3, 2, '2026-09-18 16:20:00', '2026-09-22 12:10:00', 'Finalizada', 6580.00),
(6, 2, 5, '2026-10-03 14:30:00', '2026-10-06 10:50:00', 'Finalizada', 1660.00),
(7, 1, 2, '2026-10-14 15:10:00', '2026-10-18 11:20:00', 'Finalizada', 1450.00),
(9, 5, 1, '2026-11-02 14:00:00', '2026-11-05 11:55:00', 'Finalizada', 1680.00),
(10, 1, NULL, '2026-11-22 14:40:00', NULL, 'Em Andamento', 1400.00),
(11, 2, NULL, '2026-11-23 15:15:00', NULL, 'Em Andamento', 4120.00);

-- 8. Inserindo Consumo de Serviços Extras
INSERT INTO consumo_servicos (id_hospedagem, id_servico, quantidade, valor_unitario_cobrado, data_consumo) VALUES
(1, 1, 2, 65.00, '2026-08-06 08:30:00'),
(1, 6, 1, 50.00, '2026-08-06 10:00:00'),
(2, 2, 1, 220.00, '2026-08-13 17:00:00'),
(2, 6, 4, 50.00, '2026-08-16 11:00:00'),
(3, 1, 4, 65.00, '2026-09-11 08:15:00'),
(3, 6, 4, 50.00, '2026-09-13 11:00:00'),
(4, 3, 2, 180.00, '2026-09-18 16:00:00'),
(4, 2, 1, 220.00, '2026-09-20 15:30:00'),
(4, 5, 2, 130.00, '2026-09-21 20:45:00'),
(5, 1, 2, 65.00, '2026-10-04 09:00:00'),
(5, 3, 1, 180.00, '2026-10-06 10:30:00'),
(6, 5, 1, 130.00, '2026-10-15 21:00:00'),
(6, 6, 4, 50.00, '2026-10-18 11:00:00'),
(7, 1, 2, 65.00, '2026-11-03 08:40:00'),
(7, 6, 4, 50.00, '2026-11-05 11:30:00'),
(9, 2, 1, 220.00, '2026-11-24 16:00:00');

-- 9. Inserindo Pagamentos
INSERT INTO pagamentos (id_hospedagem, data_pagamento, valor_pago, metodo_pagamento, status_pagamento, codigo_transacao) VALUES
(1, '2026-08-08 11:32:00', 1020.00, 'Cartao Credito', 'Aprovado', 'TXN-NEB-20260808-001'),
(2, '2026-08-16 12:03:00', 2220.00, 'PIX', 'Aprovado', 'TXN-NEB-20260816-002'),
(3, '2026-09-13 11:48:00', 2800.00, 'Cartao Credito', 'Aprovado', 'TXN-NEB-20260913-003'),
(4, '2026-09-22 12:12:00', 3000.00, 'Cartao Credito', 'Aprovado', 'TXN-NEB-20260922-004A'),
(4, '2026-09-22 12:14:00', 3580.00, 'Transferencia', 'Aprovado', 'TXN-NEB-20260922-004B'),
(5, '2026-10-06 10:52:00', 1660.00, 'Cartao Debito', 'Aprovado', 'TXN-NEB-20261006-005'),
(6, '2026-10-18 11:22:00', 1450.00, 'PIX', 'Aprovado', 'TXN-NEB-20261018-006'),
(7, '2026-11-05 11:57:00', 1680.00, 'Cartao Credito', 'Aprovado', 'TXN-NEB-20261105-007'),
(8, '2026-11-22 14:42:00', 700.00, 'PIX', 'Aprovado', 'TXN-NEB-20261122-008A');

-- 10. Inserindo Avaliações dos Clientes
INSERT INTO avaliacoes (id_hospedagem, nota_geral, nota_limpeza, nota_atendimento, comentario, data_avaliacao) VALUES
(1, 5, 5, 5, 'Excelente estadia! Quarto silencioso e atendimento impecável da recepção.', '2026-08-09 10:00:00'),
(2, 4, 5, 4, 'Quarto Deluxe muito confortável, apenas o check-in demorou alguns minutos.', '2026-08-17 14:20:00'),
(3, 5, 5, 5, 'A Suíte Orbital superou todas as expectativas. Vista incrível!', '2026-09-14 09:15:00'),
(4, 5, 5, 5, 'Cobertura Galaxy espetacular, serviço de transfer pontual e equipe nota 10.', '2026-09-23 18:40:00'),
(5, 4, 4, 5, 'Ótimo custo-benefício e café da manhã muito variado.', '2026-10-07 11:05:00'),
(6, 5, 5, 5, 'Tudo limpo e organizado. Voltarei com certeza.', '2026-10-19 16:30:00'),
(7, 5, 5, 5, 'Terceira vez no Hotel Nebula e o padrão continua excelente.', '2026-11-06 08:50:00');
