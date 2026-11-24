package hotel.dominio;

import java.time.LocalDateTime;


public final class EstadoHotel {

    private final MapaQuartos quartos = new MapaQuartos();
    private final CadastroHospedes hospedes = new CadastroHospedes();
    private final AgendaEventos agenda = new AgendaEventos();

    public MapaQuartos quartos() {
        return quartos;
    }

    public CadastroHospedes hospedes() {
        return hospedes;
    }

    public AgendaEventos agenda() {
        return agenda;
    }

    /** Carrega os 10 hóspedes de exemplo do código-base (opção --demo). */
    public void carregarDemonstracao() {
        String[] nomes = {
            "Fernando Netto", "Gabriel Augusto Azevedo", "Fernanda Monteiro",
            "Eleanor Neves", "Gabriel Paiva", "Débora Menezes", "Michael B Jordan",
            "Priscila Gabriel", "Noelia Vasquez", "Carla Octaviano Azevedo"
        };
        LocalDateTime agora = LocalDateTime.now();
        for (String nome : nomes) {
            hospedes.cadastrar(nome, agora);
        }
    }
}
