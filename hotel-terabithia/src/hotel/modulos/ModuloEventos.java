package hotel.modulos;

import hotel.dominio.AgendaEventos;
import hotel.dominio.DiaSemana;
import hotel.dominio.EventoAgendado;
import hotel.regras.RegrasEventos;
import hotel.regras.RegrasEventos.Buffet;
import hotel.regras.RegrasEventos.SelecaoAuditorio;
import hotel.util.Formatacao;
import hotel.util.Terminal;
import hotel.util.Validacao;

import java.math.BigDecimal;
import java.util.Optional;


public final class ModuloEventos implements Modulo {

    @Override
    public String nome() {
        return "Eventos";
    }

    @Override
    public void executar(Contexto contexto) {
        Terminal t = contexto.terminal();
        AgendaEventos agenda = contexto.estado().agenda();
        t.linha("[Eventos]");

        // ---------------- Parte A: capacidade e seleção de auditório
        Optional<Integer> convidadosLido = Validacao.parseInteiro(t.lerLinha("Convidados: "));
        if (convidadosLido.isEmpty() || !RegrasEventos.convidadosValido(convidadosLido.get())) {
            t.linha("Número de convidados inválido");
            if (convidadosLido.isPresent() && convidadosLido.get() > RegrasEventos.MAX_CONVIDADOS) {
                t.linha("Capacidade excedida: o maior auditório comporta " + RegrasEventos.MAX_CONVIDADOS + " convidados.");
            }
            return;
        }
        int convidados = convidadosLido.get();
        SelecaoAuditorio selecao = RegrasEventos.selecionarAuditorio(convidados);
        String descricaoAuditorio = descreverAuditorio(selecao);
        t.linha("Auditório selecionado: " + descricaoAuditorio);
        t.linha();

        // ---------------- Parte B: agenda e disponibilidade
        DiaSemana dia;
        int horaInicio;
        int duracao;
        while (true) {
            dia = lerDia(t);
            horaInicio = t.lerInteiro("Hora inicial: ", h -> h >= 0 && h <= 23,
                    "Hora inválida. Informe um número inteiro de 0 a 23.");
            duracao = t.lerInteiro("Duração: ", RegrasEventos::duracaoValida,
                    "Duração inválida. Informe um número inteiro de "
                            + RegrasEventos.DURACAO_MINIMA + " a " + RegrasEventos.DURACAO_MAXIMA + " horas.");

            Optional<String> motivo = RegrasEventos.verificarJanela(dia, horaInicio, duracao);
            if (motivo.isEmpty()) {
                Optional<EventoAgendado> conflito =
                        agenda.buscarConflito(selecao.auditorio(), dia, horaInicio, horaInicio + duracao);
                if (conflito.isPresent()) {
                    EventoAgendado e = conflito.get();
                    motivo = Optional.of("já existe uma reserva de " + e.empresa() + " das "
                            + e.horaInicio() + "h às " + e.horaFim() + "h nesse dia");
                }
            }
            if (motivo.isEmpty()) {
                break;
            }
            t.linha("Auditório indisponível: " + motivo.get() + ".");
            if (!t.lerSimNao("Deseja tentar outro dia ou horário? (S/N): ")) {
                t.linha("Reserva não efetuada.");
                return;
            }
        }
        int horaFim = horaInicio + duracao;
        String empresa = t.lerTextoObrigatorio("Empresa: ");
        t.linha("Auditório reservado para " + empresa + ": " + dia.rotulo() + " às " + horaInicio + "hs");
        t.linha("Status: Auditório reservado.");
        t.linha();

        // ---------------- Parte C: equipe de garçons
        int garcons = RegrasEventos.totalGarcons(convidados, duracao);
        BigDecimal custoGarcons = RegrasEventos.custoGarcons(garcons, duracao);
        t.linha("Garçons necessários: " + garcons);
        t.linha("Custo com garçons: " + Formatacao.moeda(custoGarcons));
        t.linha();

        // ---------------- Parte D: buffet
        Buffet buffet = RegrasEventos.calcularBuffet(convidados);
        t.linha("Buffet:");
        t.linha("Café: " + Formatacao.numero(buffet.litrosCafe(), 1) + " L");
        t.linha("Água: " + Formatacao.numero(buffet.litrosAgua(), 1) + " L");
        t.linha("Salgados: " + buffet.salgados() + " un");
        t.linha("Custo buffet: " + Formatacao.moeda(buffet.custoTotal()));
        t.linha();

        // ---------------- Parte E: relatório e decisão
        EventoAgendado evento = new EventoAgendado(selecao.auditorio(), selecao.cadeirasAdicionais(), dia,
                horaInicio, horaFim, empresa, convidados, garcons, duracao, custoGarcons, buffet.custoTotal());
        exibirRelatorio(t, evento, descricaoAuditorio);

        if (t.lerSimNao("Confirmar reserva? (S/N): ")) {
            agenda.registrar(evento);
            t.linha("Reserva efetuada com sucesso.");
        } else {
            t.linha("Reserva não efetuada.");
        }
    }

    private DiaSemana lerDia(Terminal t) {
        while (true) {
            Optional<DiaSemana> dia = DiaSemana.deTexto(t.lerLinha("Dia: "));
            if (dia.isPresent()) {
                return dia.get();
            }
            t.linha("Dia inválido. Informe segunda, terca, quarta, quinta, sexta, sabado ou domingo.");
        }
    }

    private String descreverAuditorio(SelecaoAuditorio selecao) {
        int extras = selecao.cadeirasAdicionais();
        if (extras == 0) {
            return selecao.auditorio().nome();
        }
        return selecao.auditorio().nome() + " (" + extras
                + (extras == 1 ? " cadeira adicional)" : " cadeiras adicionais)");
    }

    private void exibirRelatorio(Terminal t, EventoAgendado e, String descricaoAuditorio) {
        t.linha("Relatório técnico do evento");
        t.linha("---------------------------");
        t.linha(String.format("%-20s %s", "Auditório:", descricaoAuditorio));
        t.linha(String.format("%-20s %s", "Empresa:", e.empresa()));
        t.linha(String.format("%-20s %s", "Data (dia):", e.dia().rotulo()));
        t.linha(String.format("%-20s %dh", "Hora de início:", e.horaInicio()));
        t.linha(String.format("%-20s %dh", "Hora de término:", e.horaFim()));
        t.linha(String.format("%-20s %d", "Convidados:", e.convidados()));
        t.linha(String.format("%-20s %d", "Garçons:", e.garcons()));
        t.linha(String.format("%-20s %dh", "Duração:", e.duracaoHoras()));
        t.linha(String.format("%-20s %s", "Custo garçons:", Formatacao.moeda(e.custoGarcons())));
        t.linha(String.format("%-20s %s", "Custo buffet:", Formatacao.moeda(e.custoBuffet())));
        t.linha(String.format("%-20s %s", "Total do evento:", Formatacao.moeda(e.total())));
        t.linha();
    }
}
