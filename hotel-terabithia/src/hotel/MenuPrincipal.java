package hotel;

import hotel.modulos.Contexto;
import hotel.modulos.Modulo;
import hotel.modulos.ModuloAbastecimento;
import hotel.modulos.ModuloArCondicionado;
import hotel.modulos.ModuloEventos;
import hotel.modulos.ModuloHospedes;
import hotel.modulos.ModuloRelatorios;
import hotel.modulos.ModuloReservas;
import hotel.util.EntradaEncerradaException;
import hotel.util.Erros;
import hotel.util.Terminal;

import java.util.List;
import java.util.Optional;


public final class MenuPrincipal {

    private final Contexto contexto;
    private final List<Modulo> modulos = List.of(
            new ModuloReservas(),
            new ModuloHospedes(),
            new ModuloEventos(),
            new ModuloArCondicionado(),
            new ModuloAbastecimento(),
            new ModuloRelatorios());

    public MenuPrincipal(Contexto contexto) {
        this.contexto = contexto;
    }

    /**  não esquecer de sair ser a última opção */
    private int opcaoSair() {
        return modulos.size() + 1;
    }

    public void executar() {
        Terminal terminal = contexto.terminal();
        while (true) {
            exibirMenu(terminal);
            Optional<Integer> opcao = terminal.lerOpcao("Opção: ");

            if (opcao.isEmpty() || opcao.get() < 1 || opcao.get() > opcaoSair()) {
                Erros.opcaoInvalida(terminal, opcaoSair());
                continue;
            }
            if (opcao.get() == opcaoSair()) {
                if (terminal.lerSimNao("Você deseja sair? (S/N): ")) {
                    terminal.linha("Muito obrigado e até logo, " + contexto.usuario() + ".");
                    return;
                }
                continue;
            }
            executarModulo(modulos.get(opcao.get() - 1));
        }
    }

    private void exibirMenu(Terminal terminal) {
        terminal.linha();
        terminal.linha("=== " + Configuracao.nomeCompletoDoHotel() + " - Menu Principal ===");
        terminal.linha("Selecione uma opção:");
        for (int i = 0; i < modulos.size(); i++) {
            terminal.linha((i + 1) + ". " + modulos.get(i).nome());
        }
        terminal.linha(opcaoSair() + ". Sair");
    }


    private void executarModulo(Modulo modulo) {
        try {
            modulo.executar(contexto);
        } catch (EntradaEncerradaException e) {
            throw e; // fim da entrada: encerra de forma controlada em Main
        } catch (RuntimeException e) {
            contexto.terminal().linha("Ocorreu um erro inesperado em \"" + modulo.nome() + "\": " + e.getMessage());
            contexto.terminal().linha("Retornando ao menu principal.");
        }
    }
}
