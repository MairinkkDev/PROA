package hotel.dominio;


public enum Auditorio {
    LARANJA("Laranja", 150, 70),
    COLORADO("Colorado", 350, 0);

    private final String nome;
    private final int lugares;
    private final int cadeirasExtrasMaximas;

    Auditorio(String nome, int lugares, int cadeirasExtrasMaximas) {
        this.nome = nome;
        this.lugares = lugares;
        this.cadeirasExtrasMaximas = cadeirasExtrasMaximas;
    }

    public String nome() {
        return nome;
    }

    public int lugares() {
        return lugares;
    }

    public int cadeirasExtrasMaximas() {
        return cadeirasExtrasMaximas;
    }

    public int capacidadeMaxima() {
        return lugares + cadeirasExtrasMaximas;
    }
}
