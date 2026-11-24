# Arquitetura modular — Hotel Terabithia

## Visão geral

O sistema é dividido em quatro camadas. Cada camada só conhece as de baixo.

```
Main ──► Autenticacao
  └────► MenuPrincipal ──► Modulo (interface)
                              ├─ ModuloReservas
                              ├─ ModuloHospedes
                              ├─ ModuloEventos
                              ├─ ModuloArCondicionado
                              ├─ ModuloAbastecimento
                              └─ ModuloRelatorios
                                     │  (entrada/saída, via Terminal)
                              ───────▼────────────────────────────
                              regras/   cálculos e validações puros
                              dominio/  dados em memória
                              util/     Terminal, Validacao, Formatacao, Erros
```

| Camada | Pacote | Responsabilidade |
|---|---|---|
| Aplicação | `hotel` | Entrada do programa, login, menu principal, configuração |
| Subprogramas | `hotel.modulos` | Conversa com o usuário (perguntas, mensagens, fluxo) |
| Regras | `hotel.regras` | Cálculos e validações, sem nenhum `print`/`Scanner` |
| Domínio | `hotel.dominio` | Estruturas em memória: quartos, hóspedes, agenda |
| Utilitários | `hotel.util` | Entrada/saída, conversão, formatação, erro de menu |

## Como cada requisito é atendido

- **Subprogramas independentes:** cada módulo implementa `Modulo` (`nome()` e `executar(Contexto)`).
  O menu só conhece a interface; incluir um módulo é adicioná-lo à lista em `MenuPrincipal`.
- **Retorno automático ao menu:** `MenuPrincipal.executar()` é um laço. Quando `executar` de um módulo
  termina, o laço mostra o menu novamente.
- **Opções inválidas por função dedicada:** `Erros.opcaoInvalida`, usada pelo menu principal e pelo
  menu interno de hóspedes.
- **Separação entre entrada/saída e regras:** só `Terminal` e os módulos leem/escrevem. As classes de
  `regras` e `dominio` não importam `Scanner` nem imprimem, e por isso são testadas diretamente em
  `TestesRegras`.
- **Reaproveitamento de validações:** `Validacao.parseInteiro/parseDecimal`, e os leitores genéricos
  `Terminal.lerInteiro/lerDecimal/lerSimNao/lerTextoObrigatorio` recebem a condição de validade como
  parâmetro, então todos os módulos usam o mesmo código.
- **Sem variáveis globais mutáveis:** `EstadoHotel` (quartos, hóspedes, agenda) é criado em `Main` e
  entregue aos módulos por meio de `Contexto` (terminal + usuário + estado). Constantes fixas do
  enunciado ficam em `Configuracao` (`final`).
- **Controle de erros sem encerrar:** entradas inválidas são tratadas por quem lê. Qualquer exceção
  inesperada dentro de um módulo é capturada em `MenuPrincipal.executarModulo`, que informa o erro e
  volta ao menu. O fim da entrada de dados (`EntradaEncerradaException`) encerra de forma controlada.
- **Formatação monetária padronizada:** toda saída de dinheiro passa por `Formatacao.moeda`
  (`R$ 1.254,00`); os cálculos usam `BigDecimal`.

## Fluxo de dados

```
Terminal ─► Módulo ─► Regras (calcula) ─► Dominio (guarda) ─► Relatórios (consolida)
```

Exemplo: em *Reservas*, o módulo lê os dados no `Terminal`, pede a `RegrasReserva` para montar a
`Reserva` (subtotal, taxa de 10%, total), mostra o resumo e, se o usuário confirmar, grava no
`MapaQuartos`. O módulo *Relatórios* lê `MapaQuartos`, `CadastroHospedes` e `AgendaEventos` para
consolidar ocupação, contagens e receitas.

## Mapa dos requisitos para o código

| Seção do enunciado | Código |
|---|---|
| 3.1 Login, 3 tentativas | `Autenticacao`, `Configuracao` |
| 3.2 Menu e saída | `MenuPrincipal` |
| 4 Reservas | `ModuloReservas`, `RegrasReserva`, `MapaQuartos`, `TipoQuarto`, `Reserva` |
| 5 Hóspedes | `ModuloHospedes`, `CadastroHospedes`, `Hospede` |
| 6 Eventos (A–E) | `ModuloEventos`, `RegrasEventos`, `Auditorio`, `DiaSemana`, `AgendaEventos`, `EventoAgendado` |
| 7 Ar-condicionado | `ModuloArCondicionado`, `RegrasManutencao` |
| 8 Abastecimento | `ModuloAbastecimento`, `RegrasAbastecimento` |
| 9 Relatórios | `ModuloRelatorios` |
