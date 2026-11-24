package petvirtual.model;

/**
 * Representa o animal de estimação virtual, encapsulando seus atributos,
 * validações de estado e regras de evolução no ciclo de tempo.
 */
public class PetVirtual {

    public static final int VALOR_MINIMO = 0;
    public static final int VALOR_MAXIMO = 100;
    public static final int IDADE_OBJETIVO = 50;
    public static final int HORAS_DESCANSO_TOTAL = 8;

    private final String nome;
    private int fome;
    private int felicidade;
    private int cansaco;
    private int idade;
    private int vontadeBanheiro;
    private int sujeira;

    public PetVirtual(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do pet não pode ser vazio.");
        }
        this.nome = nome.trim();
        this.fome = 20;
        this.felicidade = 80;
        this.cansaco = 10;
        this.idade = 0;
        this.vontadeBanheiro = 10;
        this.sujeira = 10;
    }

    public void alimentar() {
        this.fome = limitarValor(this.fome - 25);
        this.vontadeBanheiro = limitarValor(this.vontadeBanheiro + 20);
        System.out.println("\n[AÇÃO] Você alimentou " + nome + ". A fome diminuiu, mas a vontade de ir ao banheiro aumentou!");
    }

    public void brincar() {
        this.felicidade = limitarValor(this.felicidade + 20);
        this.cansaco = limitarValor(this.cansaco + 15);
        this.sujeira = limitarValor(this.sujeira + 20);
        System.out.println("\n[AÇÃO] Você brincou com " + nome + "! Ele ficou mais feliz, porém mais cansado e sujo.");
    }

    public void descansar(int horas) {
        if (horas <= 0) {
            System.out.println("\n[AVISO] Tempo inválido! O pet precisa descansar pelo menos 1 hora.");
            return;
        }

        if (horas >= HORAS_DESCANSO_TOTAL) {
            this.cansaco = VALOR_MINIMO;
            System.out.println("\n[AÇÃO] " + nome + " dormiu por " + horas + " horas e está completamente descansado!");
        } else {
            int recuperacao = (int) Math.ceil((horas / (double) HORAS_DESCANSO_TOTAL) * VALOR_MAXIMO);
            this.cansaco = limitarValor(this.cansaco - recuperacao);
            System.out.println("\n[AÇÃO] " + nome + " descansou por " + horas + " hora(s) e recuperou " + recuperacao + " pontos de energia.");
        }
    }

    public void levarAoBanheiro() {
        this.vontadeBanheiro = VALOR_MINIMO;
        System.out.println("\n[AÇÃO] " + nome + " foi ao banheiro e está aliviado!");
    }

    public void darBanho() {
        this.sujeira = VALOR_MINIMO;
        System.out.println("\n[AÇÃO] " + nome + " tomou um belo banho e está limpinho!");
    }

    public void passarTempo() {
        this.fome = limitarValor(this.fome + 3);
        this.felicidade = limitarValor(this.felicidade - 3);
        this.cansaco = limitarValor(this.cansaco + 10);
        this.idade += 1;
    }

    public void verificarStatus() {
        System.out.println("\n==========================================");
        System.out.println("          STATUS DE " + nome.toUpperCase());
        System.out.println("==========================================");
        System.out.printf(" Idade:               %d / %d anos%n", idade, IDADE_OBJETIVO);
        System.out.printf(" Fome:                %d / %d (Derrota em 100)%n", fome, VALOR_MAXIMO);
        System.out.printf(" Felicidade:          %d / %d (Derrota em 0)%n", felicidade, VALOR_MAXIMO);
        System.out.printf(" Cansaço:             %d / %d (Derrota em 100)%n", cansaco, VALOR_MAXIMO);
        System.out.printf(" Vontade de Banheiro: %d / %d (Derrota em 100)%n", vontadeBanheiro, VALOR_MAXIMO);
        System.out.printf(" Sujeira:             %d / %d (Derrota em 100)%n", sujeira, VALOR_MAXIMO);
        System.out.println("==========================================");
    }

    public boolean atingiuObjetivo() {
        return this.idade >= IDADE_OBJETIVO && !isDerrotado();
    }

    public boolean isDerrotado() {
        return fome >= VALOR_MAXIMO
                || cansaco >= VALOR_MAXIMO
                || felicidade <= VALOR_MINIMO
                || vontadeBanheiro >= VALOR_MAXIMO
                || sujeira >= VALOR_MAXIMO;
    }

    public String obterMotivoDerrota() {
        if (fome >= VALOR_MAXIMO) {
            return nome + " atingiu 100 de fome e não resistiu!";
        }
        if (cansaco >= VALOR_MAXIMO) {
            return nome + " atingiu 100 de cansaço e desmaiou de exaustão!";
        }
        if (felicidade <= VALOR_MINIMO) {
            return nome + " chegou a 0 de felicidade e fugiu de tristeza!";
        }
        if (vontadeBanheiro >= VALOR_MAXIMO) {
            return nome + " não aguentou segurar a vontade de ir ao banheiro (100)!";
        }
        if (sujeira >= VALOR_MAXIMO) {
            return nome + " ficou sujo demais (100) e adoeceu!";
        }
        return "";
    }

    private int limitarValor(int valor) {
        return Math.max(VALOR_MINIMO, Math.min(VALOR_MAXIMO, valor));
    }

    public String getNome() { return nome; }
    public int getFome() { return fome; }
    public int getFelicidade() { return felicidade; }
    public int getCansaco() { return cansaco; }
    public int getIdade() { return idade; }
    public int getVontadeBanheiro() { return vontadeBanheiro; }
    public int getSujeira() { return sujeira; }
}
