package hotel.dominio;

import java.math.BigDecimal;


public record EventoAgendado(
        Auditorio auditorio,
        int cadeirasAdicionais,
        DiaSemana dia,
        int horaInicio,
        int horaFim,
        String empresa,
        int convidados,
        int garcons,
        int duracaoHoras,
        BigDecimal custoGarcons,
        BigDecimal custoBuffet) {


    public BigDecimal total() {
        return custoGarcons.add(custoBuffet);
    }
}
