package hotel;


public final class Configuracao {

    /** Nome do hotel, usado no lugar de {Hotel} em todas as mensagens. */
    public static final String NOME_HOTEL = "Terabithia";

    /** Senha */
    public static final String SENHA_OBRIGATORIA = "2678";

    /** Número máximo de tentativas de senha. */
    public static final int MAX_TENTATIVAS_SENHA = 3;

    private Configuracao() {
    }


    public static String nomeCompletoDoHotel() {
        return "Hotel " + NOME_HOTEL;
    }
}
