package hotel;

import hotel.dominio.Auditorio;
import hotel.dominio.CadastroHospedes;
import hotel.dominio.CadastroHospedes.Resultado;
import hotel.dominio.DiaSemana;
import hotel.dominio.MapaQuartos;
import hotel.dominio.Reserva;
import hotel.dominio.TipoQuarto;
import hotel.regras.RegrasAbastecimento;
import hotel.regras.RegrasAbastecimento.Combustivel;
import hotel.regras.RegrasEventos;
import hotel.regras.RegrasManutencao;
import hotel.regras.RegrasManutencao.Proposta;
import hotel.regras.RegrasReserva;
import hotel.util.Formatacao;
import hotel.util.Validacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class TestesRegras {

    private static int total = 0;
    private static int falhas = 0;

    public static void main(String[] args) {
        formatacao();
        validacao();
        reservas();
        hospedes();
        eventos();
        arCondicionado();
        abastecimento();
        System.out.println();
        System.out.println(total + " verificações, " + falhas + " falha(s).");
        if (falhas > 0) {
            System.exit(1);
        }
    }

    private static void verifica(String descricao, Object obtido, Object esperado) {
        total++;
        boolean ok = obtido == null ? esperado == null : obtido.equals(esperado);
        if (!ok) {
            falhas++;
        }
        System.out.println((ok ? "[OK]    " : "[FALHA] ") + descricao
                + (ok ? "" : "  -> obtido: " + obtido + " | esperado: " + esperado));
    }

    private static BigDecimal d(String valor) {
        return new BigDecimal(valor);
    }

    // ------------------------------------------------------------------ util

    private static void formatacao() {
        System.out.println("== Formatação e validação ==");
        verifica("moeda 1254", Formatacao.moeda(d("1254")), "R$ 1.254,00");
        verifica("moeda 2206.08", Formatacao.moeda(d("2206.08")), "R$ 2.206,08");
        verifica("moeda 0,5", Formatacao.moeda(d("0.5")), "R$ 0,50");
        verifica("numero 38.4 (1 casa)", Formatacao.numero(d("38.4"), 1), "38,4");
        verifica("percentual 15.0", Formatacao.percentual(d("15.0"), 1), "15,0%");
    }

    private static void validacao() {
        verifica("parseDecimal '200'", Validacao.parseDecimal("200").get(), d("200"));
        verifica("parseDecimal '4,20' (vírgula)", Validacao.parseDecimal("4,20").get(), d("4.20"));
        verifica("parseDecimal 'abc' é vazio", Validacao.parseDecimal("abc").isPresent(), false);
        verifica("parseDecimal '1e3' é vazio", Validacao.parseDecimal("1e3").isPresent(), false);
        verifica("parseInteiro '12'", Validacao.parseInteiro("12").get(), 12);
        verifica("parseInteiro '1.5' é vazio", Validacao.parseInteiro("1.5").isPresent(), false);
        verifica("parseInteiro 99999999999 é vazio", Validacao.parseInteiro("99999999999").isPresent(), false);
        verifica("nome válido 'Carla Souza'", Validacao.nomePessoaValido("Carla Souza"), true);
        verifica("nome válido com acento 'Débora Menezes'", Validacao.nomePessoaValido("Débora Menezes"), true);
        verifica("nome inválido '123'", Validacao.nomePessoaValido("123"), false);
    }

    // -------------------------------------------------------------- reservas

    private static void reservas() {
        System.out.println("== Reservas de quartos ==");
        Reserva r = RegrasReserva.montarReserva("Carla Souza", 7, TipoQuarto.EXECUTIVO, d("200"), 3);
        verifica("subtotal 200x3 executivo", r.subtotal(), d("810.00"));
        verifica("taxa de serviço 10%", r.taxaServico(), d("81.00"));
        verifica("total final", r.total(), d("891.00"));
        verifica("standard 100x2", RegrasReserva.montarReserva("A B", 1, TipoQuarto.STANDARD, d("100"), 2).total(), d("220.00"));
        verifica("luxo 100x1", RegrasReserva.montarReserva("A B", 1, TipoQuarto.LUXO, d("100"), 1).total(), d("181.50"));
        verifica("diária 0 inválida", RegrasReserva.diariaValida(d("0")), false);
        verifica("diária negativa inválida", RegrasReserva.diariaValida(d("-5")), false);
        verifica("diária 0,01 válida", RegrasReserva.diariaValida(d("0.01")), true);
        verifica("diárias 0 inválida", RegrasReserva.quantidadeDiariasValida(0), false);
        verifica("diárias 1 válida", RegrasReserva.quantidadeDiariasValida(1), true);
        verifica("diárias 30 válida", RegrasReserva.quantidadeDiariasValida(30), true);
        verifica("diárias 31 inválida", RegrasReserva.quantidadeDiariasValida(31), false);
        verifica("tipo 'e' minúsculo", TipoQuarto.deCodigo("e").get(), TipoQuarto.EXECUTIVO);
        verifica("tipo 'X' inválido", TipoQuarto.deCodigo("X").isPresent(), false);

        MapaQuartos mapa = new MapaQuartos();
        verifica("20 quartos livres no início", mapa.totalLivres(), 20);
        mapa.ocupar(r);
        verifica("quarto 7 ocupado", mapa.estaOcupado(7), true);
        verifica("19 livres após reserva", mapa.totalLivres(), 19);
        verifica("receita hospedagem", mapa.receitaHospedagem(), d("891.00"));
        boolean lancou = false;
        try {
            mapa.ocupar(r);
        } catch (IllegalStateException e) {
            lancou = true;
        }
        verifica("ocupar quarto ocupado lança exceção", lancou, true);
        verifica("quarto 21 inválido", MapaQuartos.numeroValido(21), false);
        verifica("quarto 0 inválido", MapaQuartos.numeroValido(0), false);
    }

    // -------------------------------------------------------------- hóspedes

    private static void hospedes() {
        System.out.println("== Cadastro de hóspedes ==");
        CadastroHospedes c = new CadastroHospedes();
        LocalDateTime agora = LocalDateTime.of(2026, 11, 22, 15, 49, 0);
        verifica("cadastrar Rosani", c.cadastrar("Rosani Albuquerque", agora), Resultado.SUCESSO);
        verifica("duplicado", c.cadastrar("Rosani Albuquerque", agora), Resultado.DUPLICADO);
        verifica("duplicado com espaços extras", c.cadastrar("  Rosani   Albuquerque ", agora), Resultado.DUPLICADO);
        verifica("busca exata encontrada", c.buscarExato("Rosani Albuquerque").isPresent(), true);
        verifica("busca exata (outra caixa) não encontra", c.buscarExato("rosani albuquerque").isPresent(), false);
        c.cadastrar("Rodrigo Lima", agora);
        c.cadastrar("Ana Paula", agora);
        c.cadastrar("Débora Menezes", agora);
        verifica("prefixo 'Ro' retorna 2", c.buscarPorPrefixo("Ro").size(), 2);
        verifica("prefixo 'ro' (minúsculo) retorna 2", c.buscarPorPrefixo("ro").size(), 2);
        verifica("prefixo 'deb' casa 'Débora'", c.buscarPorPrefixo("deb").size(), 1);
        verifica("prefixo 'xyz' retorna 0", c.buscarPorPrefixo("xyz").size(), 0);
        verifica("ordenação A-Z: 1º", c.listarOrdenado().get(0).nome(), "Ana Paula");
        verifica("ordenação A-Z: 2º (acento)", c.listarOrdenado().get(1).nome(), "Débora Menezes");
        verifica("ordenação A-Z: último", c.listarOrdenado().get(3).nome(), "Rosani Albuquerque");
        verifica("atualizar índice 1", c.atualizar(1, "Ana Paula Souza"), Resultado.SUCESSO);
        verifica("nome atualizado", c.listarOrdenado().get(0).nome(), "Ana Paula Souza");
        verifica("atualizar para nome existente", c.atualizar(1, "Rodrigo Lima"), Resultado.DUPLICADO);
        verifica("atualizar índice 99", c.atualizar(99, "Fulano"), Resultado.INDICE_INVALIDO);
        verifica("remover índice 1", c.remover(1), Resultado.SUCESSO);
        verifica("total após remover", c.total(), 3);
        verifica("remover índice 0", c.remover(0), Resultado.INDICE_INVALIDO);

        CadastroHospedes cheio = new CadastroHospedes();
        for (int i = 1; i <= 15; i++) {
            cheio.cadastrar("Hospede " + (char) ('A' + i), agora);
        }
        verifica("15 cadastros aceitos", cheio.total(), 15);
        verifica("16º recusado", cheio.cadastrar("Mais Um", agora), Resultado.LIMITE_ATINGIDO);
        cheio.remover(1);
        verifica("após remover, volta a aceitar", cheio.cadastrar("Mais Um", agora), Resultado.SUCESSO);
    }

    // ---------------------------------------------------------------- eventos

    private static void eventos() {
        System.out.println("== Eventos ==");
        verifica("192 convidados -> Laranja", RegrasEventos.selecionarAuditorio(192).auditorio(), Auditorio.LARANJA);
        verifica("192 convidados -> 42 cadeiras", RegrasEventos.selecionarAuditorio(192).cadeirasAdicionais(), 42);
        verifica("150 convidados -> Laranja sem extras", RegrasEventos.selecionarAuditorio(150).cadeirasAdicionais(), 0);
        verifica("151 convidados -> 1 cadeira", RegrasEventos.selecionarAuditorio(151).cadeirasAdicionais(), 1);
        verifica("220 convidados -> Laranja 70 extras", RegrasEventos.selecionarAuditorio(220).cadeirasAdicionais(), 70);
        verifica("220 convidados -> Laranja", RegrasEventos.selecionarAuditorio(220).auditorio(), Auditorio.LARANJA);
        verifica("221 convidados -> Colorado", RegrasEventos.selecionarAuditorio(221).auditorio(), Auditorio.COLORADO);
        verifica("350 convidados -> Colorado", RegrasEventos.selecionarAuditorio(350).auditorio(), Auditorio.COLORADO);
        verifica("351 inválido", RegrasEventos.convidadosValido(351), false);
        verifica("-1 inválido", RegrasEventos.convidadosValido(-1), false);
        verifica("0 inválido", RegrasEventos.convidadosValido(0), false);
        verifica("1 válido", RegrasEventos.convidadosValido(1), true);

        verifica("dia 'terça-feira'", DiaSemana.deTexto("terça-feira").get(), DiaSemana.TERCA);
        verifica("dia 'TERCA'", DiaSemana.deTexto("TERCA").get(), DiaSemana.TERCA);
        verifica("dia 'sabado'", DiaSemana.deTexto("sabado").get(), DiaSemana.SABADO);
        verifica("dia 'Segunda feira'", DiaSemana.deTexto("Segunda feira").get(), DiaSemana.SEGUNDA);
        verifica("dia 'feira' inválido", DiaSemana.deTexto("feira").isPresent(), false);
        verifica("dia 'xyz' inválido", DiaSemana.deTexto("xyz").isPresent(), false);

        verifica("segunda 13h por 8h disponível", RegrasEventos.verificarJanela(DiaSemana.SEGUNDA, 13, 8).isEmpty(), true);
        verifica("segunda 7h por 12h disponível (até 19h)", RegrasEventos.verificarJanela(DiaSemana.SEGUNDA, 7, 12).isEmpty(), true);
        verifica("sexta 12h por 12h indisponível (terminaria às 24h)", RegrasEventos.verificarJanela(DiaSemana.SEXTA, 12, 12).isEmpty(), false);
        verifica("segunda 11h por 12h termina às 23h", RegrasEventos.verificarJanela(DiaSemana.SEGUNDA, 11, 12).isEmpty(), true);
        verifica("segunda 22h por 2h indisponível", RegrasEventos.verificarJanela(DiaSemana.SEGUNDA, 22, 2).isEmpty(), false);
        verifica("segunda 6h indisponível", RegrasEventos.verificarJanela(DiaSemana.SEGUNDA, 6, 1).isEmpty(), false);
        verifica("segunda 23h indisponível", RegrasEventos.verificarJanela(DiaSemana.SEGUNDA, 23, 1).isEmpty(), false);
        verifica("sábado 7h por 8h disponível (até 15h)", RegrasEventos.verificarJanela(DiaSemana.SABADO, 7, 8).isEmpty(), true);
        verifica("sábado 14h por 2h indisponível", RegrasEventos.verificarJanela(DiaSemana.SABADO, 14, 2).isEmpty(), false);
        verifica("sábado 15h indisponível", RegrasEventos.verificarJanela(DiaSemana.SABADO, 15, 1).isEmpty(), false);
        verifica("domingo 14h por 1h disponível", RegrasEventos.verificarJanela(DiaSemana.DOMINGO, 14, 1).isEmpty(), true);
        verifica("domingo 16h indisponível", RegrasEventos.verificarJanela(DiaSemana.DOMINGO, 16, 1).isEmpty(), false);
        verifica("duração 0 inválida", RegrasEventos.duracaoValida(0), false);
        verifica("duração 1 válida", RegrasEventos.duracaoValida(1), true);
        verifica("duração 12 válida", RegrasEventos.duracaoValida(12), true);
        verifica("duração 13 inválida", RegrasEventos.duracaoValida(13), false);

        verifica("garçons base 192/12", RegrasEventos.garconsBase(192), 16);
        verifica("garçons base 193/12 (arredonda p/ cima)", RegrasEventos.garconsBase(193), 17);
        verifica("garçons reforço 8h", RegrasEventos.garconsReforco(8), 4);
        verifica("garçons reforço 7h (piso)", RegrasEventos.garconsReforco(7), 3);
        verifica("garçons reforço 1h", RegrasEventos.garconsReforco(1), 0);
        verifica("total garçons 192 convidados, 8h", RegrasEventos.totalGarcons(192, 8), 20);
        verifica("custo garçons 20 x 8h", RegrasEventos.custoGarcons(20, 8), d("1680.00"));

        RegrasEventos.Buffet b = RegrasEventos.calcularBuffet(192);
        verifica("buffet café L", b.litrosCafe(), d("38.4"));
        verifica("buffet água L", b.litrosAgua(), d("96.0"));
        verifica("buffet salgados", b.salgados(), 1344);
        verifica("buffet custo café", b.custoCafe(), d("30.72"));
        verifica("buffet custo água", b.custoAgua(), d("38.40"));
        verifica("buffet custo salgados", b.custoSalgados(), d("456.96"));
        verifica("buffet custo total", b.custoTotal(), d("526.08"));
    }

    // -------------------------------------------------------- ar-condicionado

    private static void arCondicionado() {
        System.out.println("== Ar-condicionado ==");
        Proposta clima = RegrasManutencao.calcularProposta("Clima Forte", d("120"), 10, d("8"), 8, d("150"));
        verifica("Clima Forte: desconto", clima.desconto(), d("96.00"));
        verifica("Clima Forte: total", clima.total(), d("1254.00"));
        Proposta semDesconto = RegrasManutencao.calcularProposta("X", d("120"), 7, d("8"), 8, d("150"));
        verifica("abaixo do mínimo: sem desconto", semDesconto.total(), d("990.00"));
        Proposta noLimite = RegrasManutencao.calcularProposta("X", d("120"), 8, d("8"), 8, d("150"));
        verifica("exatamente no mínimo: com desconto", noLimite.total(), d("1033.20"));
        Proposta zeroPct = RegrasManutencao.calcularProposta("X", d("100"), 5, d("0"), 1, d("0"));
        verifica("desconto 0%", zeroPct.total(), d("500.00"));

        Proposta cara = RegrasManutencao.calcularProposta("Cara", d("200"), 10, d("0"), 99, d("0"));
        List<Proposta> lista = List.of(cara, clima);
        verifica("menor orçamento", RegrasManutencao.menorOrcamento(lista).empresa(), "Clima Forte");
        verifica("maior orçamento", RegrasManutencao.maiorOrcamento(lista).empresa(), "Cara");
        verifica("diferença % (2000 vs 1254)",
                RegrasManutencao.diferencaPercentual(clima, cara).get(), d("59.49"));
        Proposta gratis = RegrasManutencao.calcularProposta("Grátis", d("100"), 2, d("100"), 1, d("0"));
        verifica("melhor proposta zero: diferença não definida",
                RegrasManutencao.diferencaPercentual(gratis, cara).isPresent(), false);
    }

    // ----------------------------------------------------------- abastecimento

    private static void abastecimento() {
        System.out.println("== Abastecimento ==");
        verifica("Wayne: gasolina", RegrasAbastecimento.escolherCombustivel(d("4.20"), d("5.82")), Combustivel.GASOLINA);
        verifica("Wayne: custo gasolina 42L", RegrasAbastecimento.custoTanque(d("5.82")), d("244.44"));
        verifica("Stark: gasolina", RegrasAbastecimento.escolherCombustivel(d("4.35"), d("6.17")), Combustivel.GASOLINA);
        verifica("Stark: custo gasolina 42L", RegrasAbastecimento.custoTanque(d("6.17")), d("259.14"));
        verifica("álcool exatamente a 70%: álcool", RegrasAbastecimento.escolherCombustivel(d("4.20"), d("6.00")), Combustivel.ALCOOL);
        verifica("álcool logo acima de 70%: gasolina", RegrasAbastecimento.escolherCombustivel(d("4.21"), d("6.00")), Combustivel.GASOLINA);
        verifica("álcool bem barato: álcool", RegrasAbastecimento.escolherCombustivel(d("3.00"), d("6.00")), Combustivel.ALCOOL);

        var wayne = RegrasAbastecimento.analisarPosto("Wayne Oil", d("4.20"), d("5.82"));
        var stark = RegrasAbastecimento.analisarPosto("Stark Petrol", d("4.35"), d("6.17"));
        verifica("custo álcool Wayne 42L", wayne.custoAlcool(), d("176.40"));
        verifica("ranking: 1º Wayne", RegrasAbastecimento.ranking(List.of(stark, wayne)).get(0).posto(), "Wayne Oil");
        verifica("ranking: 2º Stark", RegrasAbastecimento.ranking(List.of(stark, wayne)).get(1).posto(), "Stark Petrol");
    }
}
