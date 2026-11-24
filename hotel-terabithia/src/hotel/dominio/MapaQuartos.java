package hotel.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class MapaQuartos {

    public static final int TOTAL_QUARTOS = 20;
    public static final int COLUNAS_DA_GRADE = 5; // grade 4x5

    private final Reserva[] ocupacao = new Reserva[TOTAL_QUARTOS + 1]; // índice 0 não usado
    private final List<Reserva> reservas = new ArrayList<>();

    public static boolean numeroValido(int quarto) {
        return quarto >= 1 && quarto <= TOTAL_QUARTOS;
    }

    public boolean estaOcupado(int quarto) {
        return ocupacao[quarto] != null;
    }


    public void ocupar(Reserva reserva) {
        if (!numeroValido(reserva.quarto()) || estaOcupado(reserva.quarto())) {
            throw new IllegalStateException("Quarto indisponível: " + reserva.quarto());
        }
        ocupacao[reserva.quarto()] = reserva;
        reservas.add(reserva);
    }

    public List<Integer> quartosLivres() {
        List<Integer> livres = new ArrayList<>();
        for (int n = 1; n <= TOTAL_QUARTOS; n++) {
            if (!estaOcupado(n)) {
                livres.add(n);
            }
        }
        return livres;
    }

    public int totalOcupados() {
        return TOTAL_QUARTOS - quartosLivres().size();
    }

    public int totalLivres() {
        return quartosLivres().size();
    }

    public int totalReservasConfirmadas() {
        return reservas.size();
    }

    public List<Reserva> reservas() {
        return Collections.unmodifiableList(reservas);
    }


    public BigDecimal receitaHospedagem() {
        BigDecimal soma = BigDecimal.ZERO;
        for (Reserva r : reservas) {
            soma = soma.add(r.total());
        }
        return soma;
    }
}
