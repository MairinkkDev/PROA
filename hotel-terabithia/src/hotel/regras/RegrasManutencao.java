package hotel.regras;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;


public final class RegrasManutencao {

    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    private RegrasManutencao() {
    }


    public record Proposta(
            String empresa,
            BigDecimal bruto,
            BigDecimal desconto,
            BigDecimal deslocamento,
            BigDecimal total) {
    }


    public static Proposta calcularProposta(String empresa, BigDecimal valorAparelho, int quantidade,
                                            BigDecimal percentualDesconto, int quantidadeMinima,
                                            BigDecimal deslocamento) {
        BigDecimal bruto = valorAparelho.multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal desconto = BigDecimal.ZERO.setScale(2);
        if (quantidade >= quantidadeMinima) {
            desconto = bruto.multiply(percentualDesconto).divide(CEM, 2, RoundingMode.HALF_UP);
        }
        BigDecimal total = bruto.subtract(desconto).add(deslocamento).setScale(2, RoundingMode.HALF_UP);
        return new Proposta(empresa, bruto, desconto, deslocamento, total);
    }


    public static Proposta menorOrcamento(List<Proposta> propostas) {
        Proposta menor = propostas.get(0);
        for (Proposta p : propostas) {
            if (p.total().compareTo(menor.total()) < 0) {
                menor = p;
            }
        }
        return menor;
    }


    public static Proposta maiorOrcamento(List<Proposta> propostas) {
        Proposta maior = propostas.get(0);
        for (Proposta p : propostas) {
            if (p.total().compareTo(maior.total()) > 0) {
                maior = p;
            }
        }
        return maior;
    }


    public static Optional<BigDecimal> diferencaPercentual(Proposta melhor, Proposta pior) {
        if (melhor.total().signum() == 0) {
            return Optional.empty();
        }
        BigDecimal diferenca = pior.total().subtract(melhor.total());
        return Optional.of(diferenca.multiply(CEM).divide(melhor.total(), 2, RoundingMode.HALF_UP));
    }
}
