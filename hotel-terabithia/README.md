 Hotel Terabithia — Plataforma Interna de Operações Hoteleiras

Sistema de console, em **Java**, para uso dos colaboradores do hotel (não dos hóspedes).
Arquitetura modular: seis subprogramas independentes ligados por um menu principal.
(atividade escolar)

## Como executar

Requisito: **JDK 17 ou superior** (usei o jdk 21).

```cmd
# Windows
executar.bat
executar.bat --demo
```

Manualmente:

```bash
mkdir out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out hotel.Main
```

Se os acentos aparecerem errados, execute antes `chcp 65001`.

**Login:** qualquer nome de usuário e senha `2678` (máximo de 3 tentativas).

Opções de linha de comando: `--demo` (hóspedes de exemplo)

## Testes e evidências

```cmd
 testes/executar_testes.bat
```

O script compila tudo, roda **125 verificações automáticas** das regras de negócio
(`test/hotel/TestesRegras.java`) e executa 12 cenários ponta a ponta
(`testes/entradas/*.txt`), gravando a saída em `testes/logs/*.log`. Os logs já gerados estão incluídos:

| Log | O que demonstra |
|---|---|
| `00_testes_regras.log` | Regras de negócio (valores do enunciado, limites, bordas) |
| `01_login_bloqueado.log` | 3 senhas erradas → bloqueio |
| `02_login_e_menu.log` | Login com erro e acerto, opções inválidas, confirmação de saída |
| `03_reservas.log` | Exemplo 4.3 (R$ 891,00), reserva recusada, valores inválidos, quarto ocupado, mapa 4x5 |
| `04_hospedes.log` | Cadastrar, duplicado, pesquisa exata e por prefixo, listar A-Z, atualizar, remover |
| `05_hospedes_limite_15.log` | "Máximo de cadastros atingido" |
| `06_eventos.log` | Exemplo 6.6, conflito de agenda, janelas de horário, Colorado, validações |
| `07_ar_condicionado.log` | Três empresas, menor/maior orçamento e diferença percentual |
| `08_ar_condicionado_exemplo.log` | Exemplo 7.3 (R$ 1.254,00) |
| `09_abastecimento.log` | Exemplo 8.2, álcool vantajoso, empate |
| `10_sessao_completa_relatorio.log` | Sessão completa terminando no relatório operacional |
| `11_entrada_encerrada.log` | Fim da entrada de dados sem travar |
| `12_demo.log` | Opção `--demo` |

## Estrutura do projeto

```
src/hotel/
  Main.java, Autenticacao.java, MenuPrincipal.java, Configuracao.java
  modulos/    Subprogramas (entrada/saída): Reservas, Hóspedes, Eventos,
              Ar-Condicionado, Abastecimento, Relatórios
  regras/     Regras de negócio puras (cálculos e validações)
  dominio/    Dados em memória (quartos, hóspedes, agenda de eventos)
  util/       Terminal (entrada), Validacao, Formatacao, Erros
test/hotel/TestesRegras.java
testes/       entradas, logs e script de execução
ARQUITETURA.md
```

Veja `ARQUITETURA.md` para a documentação da arquitetura modular.

)




