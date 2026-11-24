package hotel;

import hotel.dominio.EstadoHotel;
import hotel.modulos.Contexto;
import hotel.util.EntradaEncerradaException;
import hotel.util.Terminal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**

 Opções de linha de comando:
 --eco   repete na saída cada linha digitada (útil pros logs)
 --demo  carrega os 10 hóspedes do exemplo
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        List<String> opcoes = Arrays.asList(args);
        Terminal terminal = Terminal.padrao(opcoes.contains("--eco"));

        EstadoHotel estado = new EstadoHotel();
        if (opcoes.contains("--demo")) {
            estado.carregarDemonstracao();
        }

        try {
            Optional<String> usuario = new Autenticacao().autenticar(terminal);
            if (usuario.isEmpty()) {
                return; // acesso bloqueado
            }
            new MenuPrincipal(new Contexto(terminal, usuario.get(), estado)).executar();
        } catch (EntradaEncerradaException e) {
            terminal.linha();
            terminal.linha("Entrada de dados encerrada. Sistema finalizado.");
        }
    }
}
