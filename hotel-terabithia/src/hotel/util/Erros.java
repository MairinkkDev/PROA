package hotel.util;


public final class Erros {

    private Erros() {
    }

    public static void opcaoInvalida(Terminal terminal, int ultimaOpcao) {
        terminal.linha("Por favor, informe um número entre 1 e " + ultimaOpcao);
    }
}
