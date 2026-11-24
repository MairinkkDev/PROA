package hotel.regras;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public final class RegrasAbastecimento {

    public static final int LITROS_TANQUE = 42;
    private static final BigDecimal LIMITE_ALCOOL = new BigDecimal("0.70");

    private RegrasAbastecimento() {
    }

    public enum Combustivel {
        ALCOOL("Álcool"),
        GASOLINA("Gasolina");

        private final String nome;

        Combustivel(String nome) {
            this.nome = nome;
        }

        public String nome() {
            return nome;
        }
    }


    public record AnalisePosto(
            String posto,
            BigDecimal precoAlcool,
            BigDecimal precoGasolina,
            Combustivel melhorOpcao,
            BigDecimal custoAlcool,
            BigDecimal custoGasolina) {


        public BigDecimal custoMelhorOpcao() {
            return melhorOpcao == Combustivel.ALCOOL ? custoAlcool : custoGasolina;
        }
    }


    public static Combustivel escolherCombustivel(BigDecimal precoAlcool, BigDecimal precoGasolina) {
        BigDecimal limite = precoGasolina.multiply(LIMITE_ALCOOL);
        return precoAlcool.compareTo(limite) <= 0 ? Combustivel.ALCOOL : Combustivel.GASOLINA;
    }


    public static BigDecimal custoTanque(BigDecimal precoLitro) {
        return precoLitro.multiply(BigDecimal.valueOf(LITROS_TANQUE)).setScale(2, RoundingMode.HALF_UP);
    }

    public static AnalisePosto analisarPosto(String posto, BigDecimal precoAlcool, BigDecimal precoGasolina) {
        return new AnalisePosto(
                posto,
                precoAlcool,
                precoGasolina,
                escolherCombustivel(precoAlcool, precoGasolina),
                custoTanque(precoAlcool),
                custoTanque(precoGasolina));
    }


    public static List<AnalisePosto> ranking(List<AnalisePosto> analises) {
        List<AnalisePosto> ordenadas = new ArrayList<>(analises);
        ordenadas.sort(Comparator.comparing(AnalisePosto::custoMelhorOpcao)); // estável
        return ordenadas;
    }
}
