-- ============================================================================
-- SISTEMA DE GESTÃO HOTELEIRA - HOTEL NEBULA
-- Parte 3: Radar de Comando - Consultas SQL Gerenciais (DQL)
-- SGBD Alvo: MySQL 8.0+ (Compatível com MySQL Workbench)
-- ============================================================================

USE hotel_nebula;

-- ----------------------------------------------------------------------------
-- CONSULTA 1: Quais quartos estão disponíveis em determinado período?
-- Exemplo de período analisado: '2026-12-10' a '2026-12-15'
-- Lógica: Exclui quartos em manutenção e quartos que possuem reservas ativas
-- (Pendente, Confirmada ou Concluida) com sobreposição de datas no intervalo.
-- ----------------------------------------------------------------------------
SET @data_inicio = '2026-12-10';
SET @data_fim    = '2026-12-15';

SELECT 
    q.id_quarto,
    q.numero_quarto,
    q.andar,
    c.nome_categoria AS categoria,
    c.capacidade_maxima,
    c.valor_diaria_base,
    q.status_atual
FROM quartos q
INNER JOIN categorias_quarto c ON q.id_categoria = c.id_categoria
WHERE q.status_atual <> 'Manutencao'
  AND q.id_quarto NOT IN (
      SELECT r.id_quarto
      FROM reservas r
      WHERE r.status_reserva IN ('Pendente', 'Confirmada', 'Concluida')
        AND r.data_checkin_previsto < @data_fim
        AND r.data_checkout_previsto > @data_inicio
  )
ORDER BY q.numero_quarto ASC;


-- ----------------------------------------------------------------------------
-- CONSULTA 2: Quais hóspedes mais realizaram reservas?
-- Exibe o total de reservas feitas, reservas efetivadas e canceladas por hóspede.
-- ----------------------------------------------------------------------------
SELECT 
    h.id_hospede,
    h.nome_completo,
    h.documento_cpf_passaporte,
    h.nacionalidade,
    COUNT(r.id_reserva) AS total_reservas,
    SUM(CASE WHEN r.status_reserva IN ('Confirmada', 'Concluida') THEN 1 ELSE 0 END) AS reservas_validas,
    SUM(CASE WHEN r.status_reserva = 'Cancelada' THEN 1 ELSE 0 END) AS reservas_canceladas
FROM hospedes h
INNER JOIN reservas r ON h.id_hospede = r.id_hospede
GROUP BY h.id_hospede, h.nome_completo, h.documento_cpf_passaporte, h.nacionalidade
ORDER BY total_reservas DESC, reservas_validas DESC, h.nome_completo ASC;


-- ----------------------------------------------------------------------------
-- CONSULTA 3: Qual foi o faturamento por mês?
-- Considera exclusivamente pagamentos com status 'Aprovado'.
-- ----------------------------------------------------------------------------
SELECT 
    DATE_FORMAT(p.data_pagamento, '%Y-%m') AS ano_mes,
    COUNT(p.id_pagamento) AS qtd_transacoes,
    SUM(p.valor_pago) AS faturamento_total,
    ROUND(AVG(p.valor_pago), 2) AS ticket_medio_transacao
FROM pagamentos p
WHERE p.status_pagamento = 'Aprovado'
GROUP BY DATE_FORMAT(p.data_pagamento, '%Y-%m')
ORDER BY ano_mes ASC;


-- ----------------------------------------------------------------------------
-- CONSULTA 4: Quais serviços extras são mais consumidos?
-- Ranqueia pela quantidade total de unidades consumidas e receita gerada.
-- ----------------------------------------------------------------------------
SELECT 
    se.id_servico,
    se.nome_servico,
    COALESCE(SUM(cs.quantidade), 0) AS quantidade_total_consumida,
    COUNT(cs.id_consumo) AS vezes_solicitado,
    COALESCE(SUM(cs.quantidade * cs.valor_unitario_cobrado), 0.00) AS receita_gerada
FROM servicos_extras se
LEFT JOIN consumo_servicos cs ON se.id_servico = cs.id_servico
GROUP BY se.id_servico, se.nome_servico
ORDER BY quantidade_total_consumida DESC, receita_gerada DESC;


-- ----------------------------------------------------------------------------
-- CONSULTA 5: Quais quartos receberam melhores avaliações?
-- Calcula a média da nota geral, limpeza e atendimento por quarto.
-- ----------------------------------------------------------------------------
SELECT 
    q.numero_quarto,
    c.nome_categoria AS categoria,
    COUNT(a.id_avaliacao) AS total_avaliacoes,
    ROUND(AVG(a.nota_geral), 2) AS media_nota_geral,
    ROUND(AVG(a.nota_limpeza), 2) AS media_limpeza,
    ROUND(AVG(a.nota_atendimento), 2) AS media_atendimento
FROM quartos q
INNER JOIN categorias_quarto c ON q.id_categoria = c.id_categoria
INNER JOIN reservas r ON q.id_quarto = r.id_quarto
INNER JOIN hospedagens hosp ON r.id_reserva = hosp.id_reserva
INNER JOIN avaliacoes a ON hosp.id_hospedagem = a.id_hospedagem
GROUP BY q.id_quarto, q.numero_quarto, c.nome_categoria
ORDER BY media_nota_geral DESC, total_avaliacoes DESC, q.numero_quarto ASC;


-- ----------------------------------------------------------------------------
-- CONSULTA 6: Quais reservas foram canceladas?
-- Lista detalhes das reservas canceladas, incluindo hóspede, quarto e valor.
-- ----------------------------------------------------------------------------
SELECT 
    r.id_reserva,
    h.nome_completo AS hospede,
    h.telefone,
    q.numero_quarto,
    c.nome_categoria AS categoria,
    r.data_reserva,
    r.data_checkin_previsto,
    r.data_checkout_previsto,
    r.valor_total_estimado AS receita_nao_realizada
FROM reservas r
INNER JOIN hospedes h ON r.id_hospede = h.id_hospede
INNER JOIN quartos q ON r.id_quarto = q.id_quarto
INNER JOIN categorias_quarto c ON q.id_categoria = c.id_categoria
WHERE r.status_reserva = 'Cancelada'
ORDER BY r.data_checkin_previsto DESC;


-- ----------------------------------------------------------------------------
-- CONSULTA 7: Quantas hospedagens cada profissional atendeu?
-- Contabiliza atendimentos de Check-in, Check-out e total de interações.
-- ----------------------------------------------------------------------------
SELECT 
    p.id_profissional,
    p.nome_completo,
    p.cargo,
    p.turno,
    COUNT(DISTINCT h_in.id_hospedagem) AS total_checkins,
    COUNT(DISTINCT h_out.id_hospedagem) AS total_checkouts,
    (COUNT(DISTINCT h_in.id_hospedagem) + COUNT(DISTINCT h_out.id_hospedagem)) AS total_atendimentos
FROM profissionais p
LEFT JOIN hospedagens h_in ON p.id_profissional = h_in.id_profissional_checkin
LEFT JOIN hospedagens h_out ON p.id_profissional = h_out.id_profissional_checkout
GROUP BY p.id_profissional, p.nome_completo, p.cargo, p.turno
ORDER BY total_atendimentos DESC, p.nome_completo ASC;
