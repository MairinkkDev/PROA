package hotel.util;


public class EntradaEncerradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntradaEncerradaException() {
        super("Entrada de dados encerrada.");
    }
}
