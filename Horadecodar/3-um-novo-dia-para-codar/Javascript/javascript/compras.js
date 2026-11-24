const frutas = ["Maçã", "Banana", "Uva", "Morango", "Laranja", "Abacaxi"];
let finalizado = false;

const listaFrutas = document.getElementById("listaFrutas");
const inputFruta = document.getElementById("nomeFruta");
const btnRemover = document.getElementById("btnRemover");
const btnPare = document.getElementById("btnPare");
const mensagem = document.getElementById("mensagem");

function atualizarExibicaoFrutas() {
  if (frutas.length > 0) {
    listaFrutas.textContent = `Frutas disponíveis: ${frutas.join(", ")}`;
  } else {
    listaFrutas.textContent = "Frutas disponíveis: Nenhuma";
  }
}

function encerrarPrograma(texto) {
  finalizado = true;
  inputFruta.disabled = true;
  btnRemover.disabled = true;
  btnPare.disabled = true;
  mensagem.textContent = texto;
}

function processarFruta() {
  if (finalizado) return;

  const digitado = inputFruta.value.trim();
  if (!digitado) {
    mensagem.textContent = "Digite o nome de uma fruta.";
    return;
  }

  if (digitado.toUpperCase() === "PARE") {
    inputFruta.value = "";
    const restantes = frutas.length > 0 ? frutas.join(", ") : "Nenhuma";
    encerrarPrograma(`Lista de frutas restantes: ${restantes}`);
    return;
  }

  const indice = frutas.findIndex(
    (fruta) => fruta.toLowerCase() === digitado.toLowerCase()
  );

  if (indice !== -1) {
    frutas.splice(indice, 1);
    atualizarExibicaoFrutas();

    if (frutas.length === 0) {
      inputFruta.value = "";
      encerrarPrograma("Lista de compras finalizada");
      return;
    }

    mensagem.textContent = "Fruta foi retirada da lista";
  } else {
    mensagem.textContent = "Fruta indisponível no nosso mercado";
  }

  inputFruta.value = "";
  inputFruta.focus();
}

atualizarExibicaoFrutas();

btnRemover.addEventListener("click", processarFruta);
btnPare.addEventListener("click", () => {
  if (finalizado) return;
  const restantes = frutas.length > 0 ? frutas.join(", ") : "Nenhuma";
  encerrarPrograma(`Lista de frutas restantes: ${restantes}`);
});
inputFruta.addEventListener("keydown", (e) => {
  if (e.key === "Enter") processarFruta();
});
