package hotel.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public final class AgendaEventos {

    private final List<EventoAgendado> eventos = new ArrayList<>();


    public Optional<EventoAgendado> buscarConflito(Auditorio auditorio, DiaSemana dia, int inicio, int fim) {
        for (EventoAgendado e : eventos) {
            boolean mesmoLugar = e.auditorio() == auditorio && e.dia() == dia;
            boolean sobrepoe = inicio < e.horaFim() && e.horaInicio() < fim;
            if (mesmoLugar && sobrepoe) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public void registrar(EventoAgendado evento) {
        eventos.add(evento);
    }

    public int totalConfirmados() {
        return eventos.size();
    }

    public List<EventoAgendado> eventos() {
        return Collections.unmodifiableList(eventos);
    }


    public BigDecimal receitaEventos() {
        BigDecimal soma = BigDecimal.ZERO;
        for (EventoAgendado e : eventos) {
            soma = soma.add(e.total());
        }
        return soma;
    }
}
