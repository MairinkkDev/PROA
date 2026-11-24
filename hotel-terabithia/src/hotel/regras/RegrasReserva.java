package hotel.regras;

import hotel.dominio.Reserva;
import hotel.dominio.TipoQuarto;

import java.math.BigDecimal;
import java.math.RoundingMode;


public final class RegrasReserva {

    public static final int MAX_DIARIAS = 30;
    private static final BigDecimal TAXA_SERVICO = new BigDecimal("0.10");

    private RegrasReserva() {
    }


    public static boolean diariaValida(BigDecimal valorDiaria) {
        return valorDiaria != null && valorDiaria.signum() > 0;
    }


    public static boolean quantidadeDiariasValida(int quantidade) {
        return quantidade >= 1 && quantidade <= MAX_DIARIAS;
    }


    public static BigDecimal calcularSubtotal(BigDecimal valorDiaria, int diarias, TipoQuarto tipo) {
        return valorDiaria
                .multiply(BigDecimal.valueOf(diarias))
                .multiply(tipo.fator())
                .setScale(2, RoundingMode.HALF_UP);
    }


    public static BigDecimal calcularTaxaServico(BigDecimal subtotal) {
        return subtotal.multiply(TAXA_SERVICO).setScale(2, RoundingMode.HALF_UP);
    }


    public static Reserva montarReserva(String hospede, int quarto, TipoQuarto tipo,
                                        BigDecimal valorDiaria, int diarias) {
        BigDecimal subtotal = calcularSubtotal(valorDiaria, diarias, tipo);
        BigDecimal taxa = calcularTaxaServico(subtotal);
        return new Reserva(hospede, quarto, tipo, diarias, subtotal, taxa, subtotal.add(taxa));
    }
}
