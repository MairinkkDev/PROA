package hotel.util;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;


public final class Terminal {

    private final Scanner entrada;
    private final PrintStream saida;
    private final boolean ecoarEntrada;

    public Terminal(InputStream in, PrintStream out, Charset charsetEntrada, boolean ecoarEntrada) {
        this.entrada = new Scanner(in, charsetEntrada);
        this.saida = out;
        this.ecoarEntrada = ecoarEntrada;
    }

    /**
     * Terminal ligado ao console padrão.
     *
     * @param ecoarEntrada quando verdadeiro, repete na saída cada linha lida;
     *                     útil para gerar logs quando a entrada é redirecionada.
     */
    public static Terminal padrao(boolean ecoarEntrada) {
        Charset charset = StandardCharsets.UTF_8;
        String nome = System.getProperty("stdout.encoding");
        if (nome != null) {
            try {
                charset = Charset.forName(nome);
            } catch (RuntimeException e) {
                charset = StandardCharsets.UTF_8;
            }
        }
        if (!charset.newEncoder().canEncode('ã')) {
            // Ambiente com charset sem acentos (ex.: locale POSIX/ASCII): usa UTF-8.
            charset = StandardCharsets.UTF_8;
        }
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, charset);
        return new Terminal(System.in, out, charset, ecoarEntrada);
    }

    // ------------------------------------------------------------------ saída

    public void imprimir(String texto) {
        saida.print(texto);
        saida.flush();
    }

    public void linha(String texto) {
        saida.println(texto);
    }

    public void linha() {
        saida.println();
    }

    // ---------------------------------------------------------------- entrada

    /** Lê uma linha (sem espaços nas pontas). Lança exceção se a entrada acabou. */
    public String lerLinha(String prompt) {
        imprimir(prompt);
        if (!entrada.hasNextLine()) {
            throw new EntradaEncerradaException();
        }
        String texto = entrada.nextLine().trim();
        if (ecoarEntrada) {
            saida.println(texto);
        }
        return texto;
    }

    /** Repete a pergunta até o usuário informar algum texto. */
    public String lerTextoObrigatorio(String prompt) {
        while (true) {
            String texto = Validacao.normalizarEspacos(lerLinha(prompt));
            if (!texto.isEmpty()) {
                return texto;
            }
            linha("Entrada não pode ser vazia.");
        }
    }

    /** Repete a pergunta até receber S ou N (maiúsculo ou minúsculo). */
    public boolean lerSimNao(String prompt) {
        while (true) {
            String resposta = lerLinha(prompt);
            if (resposta.equalsIgnoreCase("S")) {
                return true;
            }
            if (resposta.equalsIgnoreCase("N")) {
                return false;
            }
            linha("Desculpe, mas não compreendi. Responda apenas S ou N.");
        }
    }

    /** Lê um número de opção de menu; vazio se o texto não for um inteiro. */
    public Optional<Integer> lerOpcao(String prompt) {
        return Validacao.parseInteiro(lerLinha(prompt));
    }

    /** Repete a pergunta até receber um inteiro que satisfaça a condição. */
    public int lerInteiro(String prompt, Predicate<Integer> valido, String mensagemErro) {
        while (true) {
            Optional<Integer> valor = Validacao.parseInteiro(lerLinha(prompt));
            if (valor.isPresent() && valido.test(valor.get())) {
                return valor.get();
            }
            linha(mensagemErro);
        }
    }

    /** Repete a pergunta até receber um decimal que satisfaça a condição. */
    public BigDecimal lerDecimal(String prompt, Predicate<BigDecimal> valido, String mensagemErro) {
        while (true) {
            Optional<BigDecimal> valor = Validacao.parseDecimal(lerLinha(prompt));
            if (valor.isPresent() && valido.test(valor.get())) {
                return valor.get();
            }
            linha(mensagemErro);
        }
    }
}
