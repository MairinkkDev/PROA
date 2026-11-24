package hotel.modulos;

import hotel.regras.RegrasManutencao;
import hotel.regras.RegrasManutencao.Proposta;
import hotel.util.Formatacao;
import hotel.util.Terminal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public final class ModuloArCondicionado implements Modulo {

    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    @Override
    public String nome() {
        return "Ar-Condicionado";
    }

    @Override
    public void executar(Contexto contexto) {
        Terminal t = contexto.terminal();
        t.linha("[Ar-Condicionado]");

        List<Proposta> propostas = new ArrayList<>();
        boolean continuar = true;
        while (continuar) {
            Proposta proposta = lerProposta(t);
            propostas.add(proposta);
            exibirProposta(t, proposta);
            t.linha();
            continuar = t.lerSimNao("Deseja informar novos dados, " + contexto.usuario() + "? (S/N): ");
            t.linha();
        }
        exibirComparativo(t, propostas);
    }

    private Proposta lerProposta(Terminal t) {
        String empresa = t.lerTextoObrigatorio("Empresa: ");
        BigDecimal valorAparelho = t.lerDecimal("Valor por aparelho: ",
                v -> v.signum() > 0, "Valor inválido. Informe um número maior que zero.");
        int quantidade = t.lerInteiro("Quantidade: ",
                q -> q >= 1, "Quantidade inválida. Informe um inteiro maior ou igual a 1.");
        BigDecimal desconto = t.lerDecimal("Desconto (%): ",
                d -> d.signum() >= 0 && d.compareTo(CEM) <= 0,
                "Desconto inválido. Informe um percentual de 0 a 100.");
        int minimo = t.lerInteiro("Mínimo para desconto: ",
                m -> m >= 0, "Quantidade mínima inválida. Informe um inteiro maior ou igual a 0.");
        BigDecimal deslocamento = t.lerDecimal("Deslocamento: ",
                d -> d.signum() >= 0, "Valor inválido. Informe um número maior ou igual a zero.");
        return RegrasManutencao.calcularProposta(empresa, valorAparelho, quantidade, desconto, minimo, deslocamento);
    }

    private void exibirProposta(Terminal t, Proposta p) {
        t.linha("Desconto aplicado: " + Formatacao.moeda(p.desconto()));
        t.linha("O serviço de " + p.empresa() + " custará " + Formatacao.moeda(p.total()));
    }

    private void exibirComparativo(Terminal t, List<Proposta> propostas) {
        Proposta menor = RegrasManutencao.menorOrcamento(propostas);
        Proposta maior = RegrasManutencao.maiorOrcamento(propostas);

        t.linha("Resumo dos orçamentos (" + propostas.size() + " empresa" + (propostas.size() == 1 ? "" : "s") + "):");
        for (Proposta p : propostas) {
            t.linha(String.format("  %-24s %s", p.empresa(), Formatacao.moeda(p.total())));
        }
        t.linha();
        t.linha("O orçamento de menor valor é o de " + menor.empresa() + " por " + Formatacao.moeda(menor.total()));
        t.linha("Melhor orçamento: " + menor.empresa() + " — " + Formatacao.moeda(menor.total()));

        if (propostas.size() < 2) {
            t.linha("Apenas uma empresa foi informada; não há outra proposta para comparar.");
            return;
        }
        t.linha("Pior orçamento: " + maior.empresa() + " — " + Formatacao.moeda(maior.total()));
        Optional<BigDecimal> diferenca = RegrasManutencao.diferencaPercentual(menor, maior);
        if (diferenca.isPresent()) {
            t.linha("Diferença percentual entre a melhor e a pior proposta: "
                    + Formatacao.percentual(diferenca.get(), 2) + " (a pior é mais cara que a melhor)");
        } else {
            t.linha("Diferença percentual: não aplicável (a melhor proposta custa R$ 0,00).");
        }
    }
}
