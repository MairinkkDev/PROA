package hotel.regras;

import hotel.dominio.Auditorio;
import hotel.dominio.DiaSemana;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;


public final class RegrasEventos {

    public static final int MAX_CONVIDADOS = Auditorio.COLORADO.capacidadeMaxima(); // 350
    public static final int DURACAO_MINIMA = 1;
    public static final int DURACAO_MAXIMA = 12;

    private static final BigDecimal VALOR_GARCOM_HORA = new BigDecimal("10.50");
    private static final int CONVIDADOS_POR_GARCOM = 12;
    private static final int HORAS_POR_GARCOM_EXTRA = 2;

    private static final BigDecimal CAFE_LITROS_POR_CONVIDADO = new BigDecimal("0.2");
    private static final BigDecimal AGUA_LITROS_POR_CONVIDADO = new BigDecimal("0.5");
    private static final int SALGADOS_POR_CONVIDADO = 7;
    private static final BigDecimal CAFE_PRECO_LITRO = new BigDecimal("0.80");
    private static final BigDecimal AGUA_PRECO_LITRO = new BigDecimal("0.40");
    private static final BigDecimal SALGADOS_PRECO_CENTO = new BigDecimal("34.00");

    private RegrasEventos() {
    }


    public record SelecaoAuditorio(Auditorio auditorio, int cadeirasAdicionais) {
    }


    public record Buffet(
            BigDecimal litrosCafe,
            BigDecimal litrosAgua,
            int salgados,
            BigDecimal custoCafe,
            BigDecimal custoAgua,
            BigDecimal custoSalgados) {

        public BigDecimal custoTotal() {
            return custoCafe.add(custoAgua).add(custoSalgados);
        }
    }

    // ---------------------------------------------------------------


    public static boolean convidadosValido(int convidados) {
        return convidados >= 1 && convidados <= MAX_CONVIDADOS;
    }


    public static SelecaoAuditorio selecionarAuditorio(int convidados) {
        if (!convidadosValido(convidados)) {
            throw new IllegalArgumentException("Número de convidados inválido: " + convidados);
        }
        Auditorio laranja = Auditorio.LARANJA;
        if (convidados <= laranja.lugares()) {
            return new SelecaoAuditorio(laranja, 0);
        }
        if (convidados <= laranja.capacidadeMaxima()) {
            return new SelecaoAuditorio(laranja, convidados - laranja.lugares());
        }
        return new SelecaoAuditorio(Auditorio.COLORADO, 0);
    }

    // ---------------------------------------------------------------

    public static boolean duracaoValida(int horas) {
        return horas >= DURACAO_MINIMA && horas <= DURACAO_MAXIMA;
    }


    public static Optional<String> verificarJanela(DiaSemana dia, int horaInicio, int duracao) {
        int fechamento = dia.horaFechamento();
        int fim = horaInicio + duracao;
        if (horaInicio < DiaSemana.HORA_ABERTURA) {
            return Optional.of("o auditório só abre às " + DiaSemana.HORA_ABERTURA + "h");
        }
        if (horaInicio >= fechamento) {
            return Optional.of("o auditório fecha às " + fechamento + "h " + descricaoDoDia(dia));
        }
        if (fim > fechamento) {
            return Optional.of("o evento terminaria às " + fim + "h, mas o auditório fecha às "
                    + fechamento + "h " + descricaoDoDia(dia));
        }
        return Optional.empty();
    }

    private static String descricaoDoDia(DiaSemana dia) {
        return dia.fimDeSemana() ? "aos sábados e domingos" : "de segunda a sexta";
    }

    // --------------------------------------------------------------- Parte C


    public static int garconsBase(int convidados) {
        return (convidados + CONVIDADOS_POR_GARCOM - 1) / CONVIDADOS_POR_GARCOM;
    }

    /** Reforço: um garçom a mais para cada 2 horas completas de evento. */
    public static int garconsReforco(int duracaoHoras) {
        return duracaoHoras / HORAS_POR_GARCOM_EXTRA;
    }

    public static int totalGarcons(int convidados, int duracaoHoras) {
        return garconsBase(convidados) + garconsReforco(duracaoHoras);
    }


    public static BigDecimal custoGarcons(int totalGarcons, int duracaoHoras) {
        return VALOR_GARCOM_HORA
                .multiply(BigDecimal.valueOf(totalGarcons))
                .multiply(BigDecimal.valueOf(duracaoHoras))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // --------------------------------------------------------------- Parte D


    public static Buffet calcularBuffet(int convidados) {
        BigDecimal qtd = BigDecimal.valueOf(convidados);
        BigDecimal litrosCafe = CAFE_LITROS_POR_CONVIDADO.multiply(qtd).setScale(1, RoundingMode.HALF_UP);
        BigDecimal litrosAgua = AGUA_LITROS_POR_CONVIDADO.multiply(qtd).setScale(1, RoundingMode.HALF_UP);
        int salgados = SALGADOS_POR_CONVIDADO * convidados;

        BigDecimal custoCafe = litrosCafe.multiply(CAFE_PRECO_LITRO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal custoAgua = litrosAgua.multiply(AGUA_PRECO_LITRO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal custoSalgados = BigDecimal.valueOf(salgados)
                .multiply(SALGADOS_PRECO_CENTO)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return new Buffet(litrosCafe, litrosAgua, salgados, custoCafe, custoAgua, custoSalgados);
    }
}
