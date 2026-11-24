const contatos = [];
let encerrada = false;

const inputComando = document.getElementById("comando");
const inputNome = document.getElementById("nomeContato");
const inputTelefone = document.getElementById("telefoneContato");
const mensagem = document.getElementById("mensagem");

const btnExecutar = document.getElementById("btnExecutar");
const btnAdicionar = document.getElementById("btnAdicionar");
const btnBuscar = document.getElementById("btnBuscar");
const btnRemover = document.getElementById("btnRemover");
const btnListar = document.getElementById("btnListar");
const btnSair = document.getElementById("btnSair");

function executarComando(comandoManual) {
  if (encerrada) return;

  const cmd = (comandoManual || inputComando.value).trim().toUpperCase();
  const nome = inputNome.value.trim();
  const telefone = inputTelefone.value.trim();

  if (!cmd) {
    mensagem.textContent = "Digite um comando válido (ADICIONAR, BUSCAR, REMOVER, LISTAR, SAIR).";
    return;
  }

  if (cmd === "ADICIONAR") {
    if (!nome || !telefone) {
      mensagem.textContent = "Informe nome e telefone para adicionar.";
      return;
    }
    contatos.push({ nome, telefone });
    mensagem.textContent = "Contato adicionado!";
    inputNome.value = "";
    inputTelefone.value = "";
  } else if (cmd === "BUSCAR") {
    if (!nome) {
      mensagem.textContent = "Informe o nome do contato para buscar.";
      return;
    }
    const contato = contatos.find((c) => c.nome.toLowerCase() === nome.toLowerCase());
    if (contato) {
      mensagem.textContent = `Telefone: ${contato.telefone}`;
    } else {
      mensagem.textContent = "Contato não encontrado";
    }
  } else if (cmd === "REMOVER") {
    if (!nome) {
      mensagem.textContent = "Informe o nome do contato para remover.";
      return;
    }
    const indice = contatos.findIndex((c) => c.nome.toLowerCase() === nome.toLowerCase());
    if (indice !== -1) {
      contatos.splice(indice, 1);
      mensagem.textContent = "Contato removido!";
      inputNome.value = "";
    } else {
      mensagem.textContent = "Contato não encontrado";
    }
  } else if (cmd === "LISTAR") {
    if (contatos.length === 0) {
      mensagem.textContent = "Nenhum contato cadastrado.";
    } else {
     const lista = contatos.map((c) => `${c.nome} - ${c.telefone}`).join("\n");
    mensagem.textContent = lista;

    }
  } else if (cmd === "SAIR") {
    encerrada = true;
    inputComando.disabled = true;
    inputNome.disabled = true;
    inputTelefone.disabled = true;
    [btnExecutar, btnAdicionar, btnBuscar, btnRemover, btnListar, btnSair].forEach(
      (btn) => (btn.disabled = true)
    );
    mensagem.textContent = `Agenda encerrada. Total de contatos: ${contatos.length}`;
  } else {
    mensagem.textContent = "Comando inválido. Use: ADICIONAR, BUSCAR, REMOVER, LISTAR ou SAIR.";
  }

  inputComando.value = "";
}

btnExecutar.addEventListener("click", () => executarComando());
btnAdicionar.addEventListener("click", () => executarComando("ADICIONAR"));
btnBuscar.addEventListener("click", () => executarComando("BUSCAR"));
btnRemover.addEventListener("click", () => executarComando("REMOVER"));
btnListar.addEventListener("click", () => executarComando("LISTAR"));
btnSair.addEventListener("click", () => executarComando("SAIR"));

inputComando.addEventListener("keydown", (e) => {
  if (e.key === "Enter") executarComando();
});
