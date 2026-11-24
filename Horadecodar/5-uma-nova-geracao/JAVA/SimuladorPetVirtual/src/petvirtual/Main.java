package petvirtual;

import petvirtual.service.JogoService;

/**
 * Ponto de entrada da aplicação Simulador de Animal de Estimação Virtual.
 */
public class Main {
    public static void main(String[] args) {
        JogoService jogo = new JogoService();
        jogo.iniciar();
    }
}
