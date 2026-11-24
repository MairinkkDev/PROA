package hotel.modulos;

import hotel.dominio.MapaQuartos;
import hotel.dominio.Reserva;
import hotel.dominio.TipoQuarto;
import hotel.regras.RegrasReserva;
import hotel.util.Formatacao;
import hotel.util.Terminal;
import hotel.util.Validacao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public final class ModuloReservas implements Modulo {

    @Override
    public String nome() {
        return "Reservas de Quartos";
    }

    @Override
    public void executar(Contexto contexto) {
        Terminal t = contexto.terminal();
        MapaQuartos mapa = contexto.estado().quartos();
        t.linha("[Reservas]");

        if (mapa.totalLivres() == 0) {
            t.linha("Não há quartos livres no momento.");
            return;
        }


        Optional<BigDecimal> diaria = Validacao.parseDecimal(t.lerLinha("Informe o valor da diária: "));
        if (diaria.isEmpty() || !RegrasReserva.diariaValida(diaria.get())) {
            valorInvalido(t, contexto.usuario());
            return;
        }
        Optional<Integer> diarias = Validacao.parseInteiro(
                t.lerLinha("Informe a quantidade de diárias (1-" + RegrasReserva.MAX_DIARIAS + "): "));
        if (diarias.isEmpty() || !RegrasReserva.quantidadeDiariasValida(diarias.get())) {
            valorInvalido(t, contexto.usuario());
            return;
        }

        String hospede = lerNomeHospede(t);
        TipoQuarto tipo = lerTipoQuarto(t);
        int quarto = lerQuartoLivre(t, mapa);

        Reserva reserva = RegrasReserva.montarReserva(hospede, quarto, tipo, diaria.get(), diarias.get());
        exibirResumo(t, reserva);

        if (t.lerSimNao("\n" + contexto.usuario() + ", confirma a reserva? (S/N): ")) {
            mapa.ocupar(reserva);
            t.linha("Reserva efetuada com sucesso.");
        } else {
            t.linha("Reserva não efetuada.");
        }
        exibirMapa(t, mapa);
    }

    private void valorInvalido(Terminal t, String usuario) {
        t.linha("Valor inválido, " + usuario);
    }

    private String lerNomeHospede(Terminal t) {
        while (true) {
            String nome = Validacao.normalizarEspacos(t.lerLinha("Informe o nome do hóspede: "));
            if (Validacao.nomePessoaValido(nome)) {
                return nome;
            }
            t.linha("Nome inválido. Informe o nome completo (apenas letras, espaços, ponto, hífen e apóstrofo).");
        }
    }

    private TipoQuarto lerTipoQuarto(Terminal t) {
        while (true) {
            Optional<TipoQuarto> tipo = TipoQuarto.deCodigo(t.lerLinha("Tipo de quarto (S/E/L): "));
            if (tipo.isPresent()) {
                return tipo.get();
            }
            t.linha("Tipo inválido. Use S (Standard), E (Executivo) ou L (Luxo).");
        }
    }

    private int lerQuartoLivre(Terminal t, MapaQuartos mapa) {
        while (true) {
            int quarto = t.lerInteiro(
                    "Escolha um quarto (1-" + MapaQuartos.TOTAL_QUARTOS + "): ",
                    MapaQuartos::numeroValido,
                    "Quarto inválido. Informe um número de 1 a " + MapaQuartos.TOTAL_QUARTOS + ".");
            if (!mapa.estaOcupado(quarto)) {
                return quarto;
            }
            t.linha("Quarto já está ocupado");
            t.linha("Quartos livres: " + formatarLista(mapa.quartosLivres()));
            t.linha("Por favor, escolha outro quarto.");
        }
    }

    private void exibirResumo(Terminal t, Reserva r) {
        t.linha();
        t.linha("Resumo:");
        t.linha("Hóspede: " + r.hospede());
        t.linha("Quarto: " + r.quarto() + " (" + r.tipo().nome() + ")");
        t.linha("Diárias: " + r.diarias());
        t.linha("Subtotal: " + Formatacao.moeda(r.subtotal()));
        t.linha("Taxa de serviço (10%): " + Formatacao.moeda(r.taxaServico()));
        t.linha("Total: " + Formatacao.moeda(r.total()));
    }


    private void exibirMapa(Terminal t, MapaQuartos mapa) {
        t.linha();
        t.linha("Mapa de quartos (L = livre | O = ocupado):");
        for (int numero = 1; numero <= MapaQuartos.TOTAL_QUARTOS; numero++) {
            String status = mapa.estaOcupado(numero) ? "O" : "L";
            t.imprimir(String.format(" [%02d:%s]", numero, status));
            if (numero % MapaQuartos.COLUNAS_DA_GRADE == 0) {
                t.linha();
            }
        }
    }

    private String formatarLista(List<Integer> numeros) {
        return numeros.stream().map(String::valueOf).collect(Collectors.joining(", "));
    }
}
