package hotel.modulos;

import hotel.dominio.CadastroHospedes;
import hotel.dominio.CadastroHospedes.Resultado;
import hotel.dominio.Hospede;
import hotel.util.Erros;
import hotel.util.Formatacao;
import hotel.util.Terminal;
import hotel.util.Validacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public final class ModuloHospedes implements Modulo {

    private static final int ULTIMA_OPCAO = 7;
    private static final String NOME_INVALIDO =
            "Nome inválido. Use apenas letras, espaços, ponto, hífen e apóstrofo (mínimo de 2 caracteres).";

    @Override
    public String nome() {
        return "Cadastro de Hóspedes";
    }

    @Override
    public void executar(Contexto contexto) {
        Terminal t = contexto.terminal();
        CadastroHospedes cadastro = contexto.estado().hospedes();

        boolean voltar = false;
        while (!voltar) {
            t.linha();
            t.linha("[Cadastro de Hóspedes]");
            t.linha("1-Cadastrar 2-Pesquisar exato 3-Pesquisar prefixo 4-Listar 5-Atualizar 6-Remover 7-Voltar");
            Optional<Integer> opcao = t.lerOpcao("Opção: ");

            if (opcao.isEmpty() || opcao.get() < 1 || opcao.get() > ULTIMA_OPCAO) {
                Erros.opcaoInvalida(t, ULTIMA_OPCAO);
                continue;
            }
            switch (opcao.get()) {
                case 1 -> cadastrar(t, cadastro);
                case 2 -> pesquisarExato(t, cadastro);
                case 3 -> pesquisarPorPrefixo(t, cadastro);
                case 4 -> listar(t, cadastro);
                case 5 -> atualizar(t, cadastro);
                case 6 -> remover(t, cadastro);
                default -> voltar = true;
            }
        }
    }

    private void cadastrar(Terminal t, CadastroHospedes cadastro) {
        if (cadastro.estaCheio()) {
            t.linha("Máximo de cadastros atingido");
            return;
        }
        String nome = Validacao.normalizarEspacos(t.lerLinha("Nome do hóspede: "));
        if (!Validacao.nomePessoaValido(nome)) {
            t.linha(NOME_INVALIDO);
            return;
        }
        switch (cadastro.cadastrar(nome, LocalDateTime.now())) {
            case SUCESSO -> t.linha("Hóspede cadastrado com sucesso.");
            case DUPLICADO -> t.linha("Hóspede já cadastrado");
            case LIMITE_ATINGIDO -> t.linha("Máximo de cadastros atingido");
            default -> t.linha("Não foi possível concluir o cadastro.");
        }
    }

    private void pesquisarExato(Terminal t, CadastroHospedes cadastro) {
        String nome = t.lerTextoObrigatorio("Nome do hóspede: ");
        Optional<Hospede> encontrado = cadastro.buscarExato(nome);
        if (encontrado.isPresent()) {
            t.linha("Hóspede " + encontrado.get().nome() + " foi encontrado");
        } else {
            t.linha("Hóspede não encontrado");
        }
    }

    private void pesquisarPorPrefixo(Terminal t, CadastroHospedes cadastro) {
        String prefixo = t.lerTextoObrigatorio("Prefixo: ");
        List<Hospede> resultados = cadastro.buscarPorPrefixo(prefixo);
        if (resultados.isEmpty()) {
            t.linha("Hóspede não encontrado");
            return;
        }
        t.linha("Resultados:");
        for (int i = 0; i < resultados.size(); i++) {
            t.linha("[" + (i + 1) + "] " + resultados.get(i).nome());
        }
    }

    private void listar(Terminal t, CadastroHospedes cadastro) {
        List<Hospede> lista = cadastro.listarOrdenado();
        if (lista.isEmpty()) {
            t.linha("Nenhum hóspede cadastrado.");
            return;
        }
        t.linha("Hóspedes cadastrados (A-Z): " + lista.size() + "/" + CadastroHospedes.CAPACIDADE_MAXIMA);
        for (int i = 0; i < lista.size(); i++) {
            Hospede h = lista.get(i);
            t.linha(String.format("[%d] %s - cadastrado em %s", i + 1, h.nome(), Formatacao.dataHora(h.cadastradoEm())));
        }
    }

    private void atualizar(Terminal t, CadastroHospedes cadastro) {
        if (cadastro.total() == 0) {
            t.linha("Nenhum hóspede cadastrado.");
            return;
        }
        listar(t, cadastro);
        Optional<Integer> indice = Validacao.parseInteiro(t.lerLinha("Índice do hóspede a atualizar: "));
        if (indice.isEmpty() || indice.get() < 1 || indice.get() > cadastro.total()) {
            t.linha("Hóspede não encontrado (índice inválido).");
            return;
        }
        String novoNome = Validacao.normalizarEspacos(t.lerLinha("Novo nome: "));
        if (!Validacao.nomePessoaValido(novoNome)) {
            t.linha(NOME_INVALIDO);
            return;
        }
        Resultado resultado = cadastro.atualizar(indice.get(), novoNome);
        switch (resultado) {
            case SUCESSO -> t.linha("Operação realizada com sucesso");
            case DUPLICADO -> t.linha("Hóspede já cadastrado");
            default -> t.linha("Hóspede não encontrado (índice inválido).");
        }
    }

    private void remover(Terminal t, CadastroHospedes cadastro) {
        if (cadastro.total() == 0) {
            t.linha("Nenhum hóspede cadastrado.");
            return;
        }
        listar(t, cadastro);
        Optional<Integer> indice = Validacao.parseInteiro(t.lerLinha("Índice do hóspede a remover: "));
        if (indice.isEmpty() || indice.get() < 1 || indice.get() > cadastro.total()) {
            t.linha("Hóspede não encontrado (índice inválido).");
            return;
        }
        if (cadastro.remover(indice.get()) == Resultado.SUCESSO) {
            t.linha("Operação realizada com sucesso");
        } else {
            t.linha("Hóspede não encontrado (índice inválido).");
        }
    }
}
