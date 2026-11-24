package hotel.dominio;

import hotel.util.Validacao;

import java.util.Optional;


public enum DiaSemana {
    SEGUNDA("segunda", false),
    TERCA("terça", false),
    QUARTA("quarta", false),
    QUINTA("quinta", false),
    SEXTA("sexta", false),
    SABADO("sábado", true),
    DOMINGO("domingo", true);


    public static final int HORA_ABERTURA = 7;

    private final String rotulo;
    private final boolean fimDeSemana;

    DiaSemana(String rotulo, boolean fimDeSemana) {
        this.rotulo = rotulo;
        this.fimDeSemana = fimDeSemana;
    }

    public String rotulo() {
        return rotulo;
    }

    public boolean fimDeSemana() {
        return fimDeSemana;
    }


    public int horaFechamento() {
        return fimDeSemana ? 15 : 23;
    }


    public static Optional<DiaSemana> deTexto(String texto) {
        String chave = Validacao.semAcentosMinusculo(texto).trim().replaceAll("[-\\s]*feira$", "");
        for (DiaSemana dia : values()) {
            if (Validacao.semAcentosMinusculo(dia.rotulo).equals(chave)) {
                return Optional.of(dia);
            }
        }
        return Optional.empty();
    }
}
