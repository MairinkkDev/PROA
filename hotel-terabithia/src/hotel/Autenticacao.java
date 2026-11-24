package hotel;

import hotel.util.Terminal;

import java.util.Optional;


public final class Autenticacao {


    public Optional<String> autenticar(Terminal terminal) {
        terminal.linha("Bem-vindo ao " + Configuracao.nomeCompletoDoHotel());
        String nome = terminal.lerTextoObrigatorio("Nome do usuário: ");

        for (int tentativa = 1; tentativa <= Configuracao.MAX_TENTATIVAS_SENHA; tentativa++) {
            String senha = terminal.lerLinha("Senha: ");
            if (senhaCorreta(senha)) {
                terminal.linha();
                terminal.linha("Bem-vindo ao " + Configuracao.nomeCompletoDoHotel() + ", " + nome
                        + ". É um imenso prazer ter você por aqui!");
                return Optional.of(nome);
            }
            int restantes = Configuracao.MAX_TENTATIVAS_SENHA - tentativa;
            if (restantes > 0) {
                terminal.linha("Senha incorreta. Tentativas restantes: " + restantes + ".");
            } else {
                terminal.linha("Senha incorreta.");
            }
        }
        terminal.linha("Acesso bloqueado: número máximo de tentativas excedido. Sistema encerrado.");
        return Optional.empty();
    }

    private boolean senhaCorreta(String senhaInformada) {
        return Configuracao.SENHA_OBRIGATORIA.equals(senhaInformada);
    }
}
