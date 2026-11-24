const planetas = [
  "Mercúrio",
  "Vênus",
  "Terra",
  "Marte",
  "Júpiter",
  "Saturno",
  "Urano",
  "Netuno",
  "Plutão"
];

const exibicaoPlanetas = document.getElementById("exibicaoPlanetas");
const inputPlaneta = document.getElementById("nomePlaneta");
const btnVerificar = document.getElementById("btnVerificar");
const mensagem = document.getElementById("mensagem");

exibicaoPlanetas.textContent = `Planetas: ${planetas.join(", ")}.`;

function verificarPlaneta() {
  const digitado = inputPlaneta.value.trim();

  if (!digitado) {
    mensagem.textContent = "Por favor, digite o nome de um planeta.";
    return;
  }

  const encontrado = planetas.find(
    (planeta) => planeta.toLowerCase() === digitado.toLowerCase()
  );

  if (encontrado) {
    mensagem.textContent = `O planeta "${encontrado}" está na lista!`;
  } else {
    mensagem.textContent = `O planeta "${digitado}" não está na lista.`;
  }

  inputPlaneta.value = "";
  inputPlaneta.focus();
}

btnVerificar.addEventListener("click", verificarPlaneta);
inputPlaneta.addEventListener("keydown", (e) => {
  if (e.key === "Enter") verificarPlaneta();
});
