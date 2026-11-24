package hotel.modulos;

import hotel.Configuracao;
import hotel.dominio.EstadoHotel;
import hotel.dominio.MapaQuartos;
import hotel.util.Formatacao;
import hotel.util.Terminal;

import java.math.BigDecimal;
import java.math.RoundingMode;


public final class ModuloRelatorios implements Modulo {

    private static final int LARGURA_ROTULO = 36;
    private static final int LARGURA_VALOR = 18;

    @Override
    public String nome() {
        return "Relatórios Operacionais";
    }

    @Override
    public void executar(Contexto contexto) {
        Terminal t = contexto.terminal();
        EstadoHotel estado = contexto.estado();

        int ocupados = estado.quartos().totalOcupados();
        BigDecimal taxaOcupacao = BigDecimal.valueOf(ocupados)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(MapaQuartos.TOTAL_QUARTOS), 1, RoundingMode.HALF_UP);
        BigDecimal receitaHospedagem = estado.quartos().receitaHospedagem();
        BigDecimal receitaEventos = estado.agenda().receitaEventos();

        String separador = "=".repeat(LARGURA_ROTULO + LARGURA_VALOR + 1);
        t.linha("[Relatórios Operacionais]");
        t.linha(separador);
        t.linha(centralizar(Configuracao.nomeCompletoDoHotel() + " - Relatório Operacional", separador.length()));
        t.linha(separador);
        t.linha(linha("Indicador", "Valor"));
        t.linha("-".repeat(separador.length()));
        t.linha(linha("Reservas de quartos confirmadas", String.valueOf(estado.quartos().totalReservasConfirmadas())));
        t.linha(linha("Quartos ocupados", ocupados + " de " + MapaQuartos.TOTAL_QUARTOS));
        t.linha(linha("Taxa de ocupação atual", Formatacao.percentual(taxaOcupacao, 1)));
        t.linha(linha("Hóspedes cadastrados", String.valueOf(estado.hospedes().total())));
        t.linha(linha("Eventos confirmados", String.valueOf(estado.agenda().totalConfirmados())));
        t.linha("-".repeat(separador.length()));
        t.linha(linha("Receita de hospedagem", Formatacao.moeda(receitaHospedagem)));
        t.linha(linha("Receita de eventos", Formatacao.moeda(receitaEventos)));
        t.linha(linha("Receita total geral", Formatacao.moeda(receitaHospedagem.add(receitaEventos))));
        t.linha(separador);
    }


    private String linha(String rotulo, String valor) {
        return String.format("%-" + LARGURA_ROTULO + "s %" + LARGURA_VALOR + "s", rotulo, valor);
    }

    private String centralizar(String texto, int largura) {
        int espacos = Math.max(0, (largura - texto.length()) / 2);
        return " ".repeat(espacos) + texto;
    }
}
