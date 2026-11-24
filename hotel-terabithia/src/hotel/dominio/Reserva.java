package hotel.dominio;

import java.math.BigDecimal;


public record Reserva(
        String hospede,
        int quarto,
        TipoQuarto tipo,
        int diarias,
        BigDecimal subtotal,
        BigDecimal taxaServico,
        BigDecimal total) {
}
