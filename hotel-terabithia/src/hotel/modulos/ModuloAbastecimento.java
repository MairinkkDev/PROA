package hotel.modulos;

import hotel.regras.RegrasAbastecimento;
import hotel.regras.RegrasAbastecimento.AnalisePosto;
import hotel.regras.RegrasAbastecimento.Combustivel;
import hotel.util.Formatacao;
import hotel.util.Terminal;

import java.math.BigDecimal;
import java.util.List;


public final class ModuloAbastecimento implements Modulo {

    private static final String POSTO_1 = "Wayne Oil";
    private static final String POSTO_2 = "Stark Petrol";

    @Override
    public String nome() {
        return "Abastecimento";
    }

    @Override
    public void executar(Contexto contexto) {
        Terminal t = contexto.terminal();
        t.linha("[Abastecimento]");

        AnalisePosto wayne = lerPosto(t, POSTO_1);
        AnalisePosto stark = lerPosto(t, POSTO_2);
        List<AnalisePosto> analises = List.of(wayne, stark);

        t.linha();
        for (AnalisePosto a : analises) {
            t.linha(a.posto() + " -> Álcool: " + preco(a.precoAlcool()) + " | Gasolina: " + preco(a.precoGasolina()));
        }

        t.linha();
        t.linha("Custo de " + RegrasAbastecimento.LITROS_TANQUE + " L em cada cenário:");
        for (AnalisePosto a : analises) {
            t.linha(String.format("  %-13s Álcool %s | Gasolina %s", a.posto() + ":",
                    Formatacao.moeda(a.custoAlcool()), Formatacao.moeda(a.custoGasolina())));
        }

        t.linha();
        for (AnalisePosto a : analises) {
            t.linha(a.posto() + ": melhor opção = " + a.melhorOpcao().nome()
                    + " | Total (" + RegrasAbastecimento.LITROS_TANQUE + "L) = " + Formatacao.moeda(a.custoMelhorOpcao()));
        }

        List<AnalisePosto> ranking = RegrasAbastecimento.ranking(analises);
        t.linha();
        t.linha("Ranking (menor para maior custo):");
        for (int i = 0; i < ranking.size(); i++) {
            AnalisePosto a = ranking.get(i);
            t.linha((i + 1) + "º " + a.posto() + " - " + a.melhorOpcao().nome() + " - "
                    + Formatacao.moeda(a.custoMelhorOpcao()));
        }

        t.linha();
        AnalisePosto melhor = ranking.get(0);
        AnalisePosto pior = ranking.get(1);
        if (melhor.custoMelhorOpcao().compareTo(pior.custoMelhorOpcao()) == 0) {
            t.linha(contexto.usuario() + ", o custo é o mesmo nos dois postos ("
                    + Formatacao.moeda(melhor.custoMelhorOpcao()) + ").");
        } else {
            t.linha(contexto.usuario() + ", é mais barato abastecer com "
                    + minusculo(melhor.melhorOpcao()) + " no posto " + melhor.posto() + ".");
        }
    }

    private AnalisePosto lerPosto(Terminal t, String posto) {
        BigDecimal alcool = t.lerDecimal(posto + " -> Álcool (R$/L): ",
                v -> v.signum() > 0, "Preço inválido. Informe um valor maior que zero (ex.: 4,20).");
        BigDecimal gasolina = t.lerDecimal(posto + " -> Gasolina (R$/L): ",
                v -> v.signum() > 0, "Preço inválido. Informe um valor maior que zero (ex.: 5,82).");
        return RegrasAbastecimento.analisarPosto(posto, alcool, gasolina);
    }

    private String preco(BigDecimal valor) {
        return Formatacao.numero(valor, 2);
    }

    private String minusculo(Combustivel combustivel) {
        return combustivel.nome().toLowerCase(java.util.Locale.ROOT);
    }
}
