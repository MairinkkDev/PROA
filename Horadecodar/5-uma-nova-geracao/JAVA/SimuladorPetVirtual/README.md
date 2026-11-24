# Simulador de Animal de Estimação Virtual (Java - POO)

Aplicação de console desenvolvida em **Java** aplicando os pilares da **Programação Orientada a Objetos** (Encapsulamento, Separação de Responsabilidades e Modelagem de Domínio), estruturada no padrão **IntelliJ IDEA** para versionamento em repositório GitHub.

## Estrutura do Repositório

```text
SimuladorPetVirtual/
├── .idea/
│   ├── misc.xml
│   └── modules.xml
├── out/
│   └── .gitkeep
├── src/
│   └── petvirtual/
│       ├── model/
│       │   └── PetVirtual.java
│       ├── service/
│       │   └── JogoService.java
│       └── Main.java
├── .gitignore
├── executar.bat
├── README.md
└── SimuladorPetVirtual.iml
```

## Como Executar

### Opção 1: Script Windows (`executar.bat`)
1. Certifique-se de ter o **JDK 17+** instalado e configurado no `PATH`.
2. Dê um duplo clique em **`executar.bat`** na raiz do projeto.
3. O script compilará automaticamente os arquivos de `src/` para `out/` e iniciará o simulador no terminal.

### Opção 2: IntelliJ IDEA
1. Clone o repositório e abra a pasta `SimuladorPetVirtual` no **IntelliJ IDEA** (`File > Open`).
2. Execute a classe `petvirtual.Main`.
