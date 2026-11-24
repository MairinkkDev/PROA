package hotel.dominio;

import java.math.BigDecimal;
import java.util.Optional;


public enum TipoQuarto {
    STANDARD('S', "Standard", new BigDecimal("1.00")),
    EXECUTIVO('E', "Executivo", new BigDecimal("1.35")),
    LUXO('L', "Luxo", new BigDecimal("1.65"));

    private final char codigo;
    private final String nome;
    private final BigDecimal fator;

    TipoQuarto(char codigo, String nome, BigDecimal fator) {
        this.codigo = codigo;
        this.nome = nome;
        this.fator = fator;
    }

    public char codigo() {
        return codigo;
    }

    public String nome() {
        return nome;
    }

    public BigDecimal fator() {
        return fator;
    }


    public static Optional<TipoQuarto> deCodigo(String texto) {
        if (texto == null || texto.length() != 1) {
            return Optional.empty();
        }
        char c = Character.toUpperCase(texto.charAt(0));
        for (TipoQuarto tipo : values()) {
            if (tipo.codigo == c) {
                return Optional.of(tipo);
            }
        }
        return Optional.empty();
    }
}
