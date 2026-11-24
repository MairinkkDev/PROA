package hotel.util;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;


public final class Validacao {

    private static final Pattern DECIMAL = Pattern.compile("-?\\d+([.,]\\d+)?");
    private static final Pattern INTEIRO = Pattern.compile("-?\\d+");
    private static final Pattern NOME_PESSOA = Pattern.compile("\\p{L}[\\p{L} .'\\-]*");

    private Validacao() {
    }

    public static Optional<Integer> parseInteiro(String texto) {
        if (texto == null) {
            return Optional.empty();
        }
        String limpo = texto.trim();
        if (!INTEIRO.matcher(limpo).matches()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(limpo));
        } catch (NumberFormatException e) {
            return Optional.empty(); // fora do intervalo de int
        }
    }


    public static Optional<BigDecimal> parseDecimal(String texto) {
        if (texto == null) {
            return Optional.empty();
        }
        String limpo = texto.trim();
        if (!DECIMAL.matcher(limpo).matches()) {
            return Optional.empty();
        }
        return Optional.of(new BigDecimal(limpo.replace(',', '.')));
    }


    public static boolean nomePessoaValido(String nome) {
        return nome != null
                && nome.length() >= 2
                && nome.length() <= 80
                && NOME_PESSOA.matcher(nome).matches();
    }

    /** Remove espaços nas pontas e colapsa espaços repetidos. */
    public static String normalizarEspacos(String texto) {
        return texto == null ? "" : texto.trim().replaceAll("\\s+", " ");
    }


    public static String semAcentosMinusculo(String texto) {
        if (texto == null) {
            return "";
        }
        String decomposto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return decomposto.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }
}
