package hotel.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public final class Formatacao {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private Formatacao() {
    }

    /** Formata um número com separador de milhar "." e decimal ",". */
    public static String numero(BigDecimal valor, int casasDecimais) {
        String padrao = casasDecimais > 0 ? "#,##0." + "0".repeat(casasDecimais) : "#,##0";
        DecimalFormat formato = new DecimalFormat(padrao, DecimalFormatSymbols.getInstance(PT_BR));
        formato.setRoundingMode(RoundingMode.HALF_UP);
        return formato.format(valor);
    }

    /** Formata valor monetário: R$ 1.254,00. */
    public static String moeda(BigDecimal valor) {
        return "R$ " + numero(valor, 2);
    }

    /** Formata percentual: 15,0%. */
    public static String percentual(BigDecimal valor, int casasDecimais) {
        return numero(valor, casasDecimais) + "%";
    }

    /** Formata data e hora: 22/11/2026 15:49:00. */
    public static String dataHora(LocalDateTime dataHora) {
        return DATA_HORA.format(dataHora);
    }
}
