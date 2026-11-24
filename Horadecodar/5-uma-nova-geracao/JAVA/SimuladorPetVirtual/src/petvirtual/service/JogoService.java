package petvirtual.service;

import petvirtual.model.PetVirtual;
import java.util.Scanner;

/**
 * Controla o ciclo de vida da simulação, leitura segura de entradas
 * e interação do usuário via console.
 */
public class JogoService {

    private final Scanner scanner;
    private PetVirtual pet;

    public JogoService() {
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        exibirBoasVindas();
        criarPet();

        boolean executando = true;
        while (executando && !pet.isDerrotado() && !pet.atingiuObjetivo()) {
            pet.verificarStatus();
            exibirMenu();
            int opcao = lerInteiro("Escolha uma opção: ");

            if (opcao == 0) {
                executando = false;
                System.out.println("\nVocê encerrou o simulador. Até a próxima!");
                break;
            }

            boolean acaoConsomeTempo = processarOpcao(opcao);

            if (acaoConsomeTempo) {
                pet.passarTempo();
                System.out.println("[TEMPO] Um ciclo passou! (+1 Idade, +3 Fome, -3 Felicidade, +10 Cansaço)");
            }
        }

        finalizarJogo();
        scanner.close();
    }

    private void exibirBoasVindas() {
        System.out.println("====================================================");
        System.out.println("   SIMULADOR DE ANIMAL DE ESTIMAÇÃO VIRTUAL (POO)   ");
        System.out.println("====================================================");
        System.out.println("Objetivo: Cuide do seu pet até ele completar 50 anos!");
        System.out.println("Atenção: Não deixe Fome, Cansaço, Banheiro ou Sujeira");
        System.out.println("chegarem a 100, nem a Felicidade cair para 0.\n");
    }

    private void criarPet() {
        String nome = "";
        while (nome.trim().isEmpty()) {
            System.out.print("Digite o nome do seu novo bichinho virtual: ");
            nome = scanner.nextLine();
            if (nome.trim().isEmpty()) {
                System.out.println("[ERRO] Por favor, informe um nome válido.");
            }
        }
        this.pet = new PetVirtual(nome);
        System.out.println("\nParabéns! Você adotou " + pet.getNome() + "!");
    }

    private void exibirMenu() {
        System.out.println("\nO que você deseja fazer?");
        System.out.println("1 - Alimentar");
        System.out.println("2 - Brincar");
        System.out.println("3 - Descansar");
        System.out.println("4 - Levar ao banheiro");
        System.out.println("5 - Dar banho");
        System.out.println("6 - Apenas observar (avançar ciclo de tempo)");
        System.out.println("0 - Sair do jogo");
    }

    private boolean processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                pet.alimentar();
                return true;
            case 2:
                pet.brincar();
                return true;
            case 3:
                int horas = lerInteiro("Por quantas horas " + pet.getNome() + " vai descansar? (8h = 100%): ");
                pet.descansar(horas);
                return horas > 0;
            case 4:
                pet.levarAoBanheiro();
                return true;
            case 5:
                pet.darBanho();
                return true;
            case 6:
                System.out.println("\n[AÇÃO] Você observou " + pet.getNome() + " explorando o ambiente.");
                return true;
            default:
                System.out.println("\n[AVISO] Opção inválida! Escolha um número do menu.");
                return false;
        }
    }

    private void finalizarJogo() {
        if (pet.isDerrotado()) {
            pet.verificarStatus();
            System.out.println("\n====================================================");
            System.out.println("                    FIM DE JOGO!                    ");
            System.out.println("====================================================");
            System.out.println("Motivo: " + pet.obterMotivoDerrota());
        } else if (pet.atingiuObjetivo()) {
            pet.verificarStatus();
            System.out.println("\n====================================================");
            System.out.println("               PARABÉNS! VOCÊ VENCEU!               ");
            System.out.println("====================================================");
            System.out.println(pet.getNome() + " chegou aos " + pet.getIdade() + " anos saudável e feliz!");
        }
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine();
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERRO] Entrada inválida. Digite apenas números inteiros.");
            }
        }
    }
}
