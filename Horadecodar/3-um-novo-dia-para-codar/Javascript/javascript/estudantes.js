const estudantes = [];
let encerrado = false;

const inputNome = document.getElementById("nomeEstudante");
const btnCadastrar = document.getElementById("btnCadastrar");
const btnPare = document.getElementById("btnPare");
const mensagem = document.getElementById("mensagem");
const listaEstudantes = document.getElementById("listaEstudantes");

function finalizarCadastro() {
  encerrado = true;
  inputNome.disabled = true;
  btnCadastrar.disabled = true;
  btnPare.disabled = true;

  mensagem.textContent = `Quantidade de estudantes cadastrados: ${estudantes.length}`;
  listaEstudantes.innerHTML = "";

  estudantes.forEach((estudante) => {
    const li = document.createElement("li");
    li.textContent = estudante;
    listaEstudantes.appendChild(li);
  });
}

function processarEntrada() {
  if (encerrado) return;

  const valor = inputNome.value.trim();
  if (!valor) {
    mensagem.textContent = "Digite um nome válido.";
    return;
  }

  if (valor.toUpperCase() === "PARE") {
    inputNome.value = "";
    finalizarCadastro();
    return;
  }

  estudantes.push(valor);
  mensagem.textContent = `Estudante "${valor}" cadastrado!`;
  inputNome.value = "";
  inputNome.focus();
}

btnCadastrar.addEventListener("click", processarEntrada);
btnPare.addEventListener("click", finalizarCadastro);
inputNome.addEventListener("keydown", (e) => {
  if (e.key === "Enter") processarEntrada();
});
