package hotel.modulos;

/**
 * Contrato de um subprograma independente. Ao terminar {@link #executar},
 * o controle volta automaticamente ao menu principal.
 */
public interface Modulo {

    /** Nome exibido no menu principal. */
    String nome();

    /** Executa o subprograma até o fim e retorna ao chamador. */
    void executar(Contexto contexto);
}
