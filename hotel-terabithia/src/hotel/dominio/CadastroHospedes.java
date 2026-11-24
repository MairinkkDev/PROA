package hotel.dominio;

import hotel.util.Validacao;

import java.text.Collator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


public final class CadastroHospedes {

    public static final int CAPACIDADE_MAXIMA = 15;

    public enum Resultado { SUCESSO, LIMITE_ATINGIDO, DUPLICADO, INDICE_INVALIDO }

    private final List<Hospede> hospedes = new ArrayList<>();
    private final Collator ordenacao = criarOrdenacao();

    private static Collator criarOrdenacao() {
        Collator collator = Collator.getInstance(Locale.forLanguageTag("pt-BR"));
        collator.setStrength(Collator.SECONDARY); // ignora diferença de maiúsculas/minúsculas
        return collator;
    }

    public int total() {
        return hospedes.size();
    }

    public boolean estaCheio() {
        return hospedes.size() >= CAPACIDADE_MAXIMA;
    }


    public Resultado cadastrar(String nome, LocalDateTime quando) {
        String limpo = Validacao.normalizarEspacos(nome);
        if (estaCheio()) {
            return Resultado.LIMITE_ATINGIDO;
        }
        if (buscarExato(limpo).isPresent()) {
            return Resultado.DUPLICADO;
        }
        hospedes.add(new Hospede(limpo, quando));
        return Resultado.SUCESSO;
    }


    public Optional<Hospede> buscarExato(String nome) {
        String limpo = Validacao.normalizarEspacos(nome);
        return hospedes.stream().filter(h -> h.nome().equals(limpo)).findFirst();
    }


    public List<Hospede> buscarPorPrefixo(String prefixo) {
        String chave = Validacao.semAcentosMinusculo(Validacao.normalizarEspacos(prefixo));
        return listarOrdenado().stream()
                .filter(h -> Validacao.semAcentosMinusculo(h.nome()).startsWith(chave))
                .toList();
    }


    public List<Hospede> listarOrdenado() {
        List<Hospede> copia = new ArrayList<>(hospedes);
        copia.sort((a, b) -> ordenacao.compare(a.nome(), b.nome()));
        return copia;
    }


    public Resultado atualizar(int indice, String novoNome) {
        List<Hospede> lista = listarOrdenado();
        if (indice < 1 || indice > lista.size()) {
            return Resultado.INDICE_INVALIDO;
        }
        Hospede alvo = lista.get(indice - 1);
        String limpo = Validacao.normalizarEspacos(novoNome);
        for (Hospede h : hospedes) {
            if (h != alvo && h.nome().equals(limpo)) {
                return Resultado.DUPLICADO;
            }
        }
        alvo.renomear(limpo);
        return Resultado.SUCESSO;
    }


    public Resultado remover(int indice) {
        List<Hospede> lista = listarOrdenado();
        if (indice < 1 || indice > lista.size()) {
            return Resultado.INDICE_INVALIDO;
        }
        hospedes.remove(lista.get(indice - 1));
        return Resultado.SUCESSO;
    }
}
