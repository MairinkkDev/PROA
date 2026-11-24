package hotel.dominio;

import java.time.LocalDateTime;


public final class Hospede {

    private String nome;
    private final LocalDateTime cadastradoEm;

    public Hospede(String nome, LocalDateTime cadastradoEm) {
        this.nome = nome;
        this.cadastradoEm = cadastradoEm;
    }

    public String nome() {
        return nome;
    }

    public LocalDateTime cadastradoEm() {
        return cadastradoEm;
    }

    void renomear(String novoNome) {
        this.nome = novoNome;
    }
}
