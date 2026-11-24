const form = document.querySelector("#checkoutForm");
const telefoneInput = document.querySelector("#telefone");
const cepInput = document.querySelector("#cep");
const buscarCepButton = document.querySelector("#buscarCep");
const cepFeedback = document.querySelector("#cepFeedback");
const resumoQuantidade = document.querySelector("#resumoQuantidade");

// Recupera o pedido da sessão ou usa o produto 1 como padrão caso o usuário abra direto o checkout
function obterPedidoAtual() {
  try {
    const salvo = JSON.parse(sessionStorage.getItem("pedidoAtual"));
    if (salvo && buscarProdutoPorId(salvo.produtoId)) {
      return {
        produtoId: Number(salvo.produtoId),
        quantidade: Math.min(10, Math.max(1, Number(salvo.quantidade) || 1))
      };
    }
  } catch (e) {
    // Ignora erro de parse e usa padrão
  }
  return { produtoId: 1, quantidade: 1 };
}

let pedidoAtual = obterPedidoAtual();

function atualizarResumo() {
  const produto = buscarProdutoPorId(pedidoAtual.produtoId) || PRODUTOS[0];
  const qtd = pedidoAtual.quantidade;
  const subtotal = produto.preco * qtd;
  // Frete grátis acima de R$ 500,00 ou R$ 29,90 fixo interdimensional
  const frete = subtotal >= 500 ? 0 : 29.90;
  const total = subtotal + frete;

  document.querySelector("#resumoProdutoNome").textContent = produto.nome;
  document.querySelector("#resumoProdutoRegra").textContent = produto.regra;
  document.querySelector("#resumoUnitario").textContent = formatarMoeda(produto.preco);
  document.querySelector("#resumoSubtotal").textContent = formatarMoeda(subtotal);
  document.querySelector("#resumoFrete").textContent = frete === 0 ? "Grátis" : formatarMoeda(frete);
  document.querySelector("#resumoTotal").textContent = formatarMoeda(total);

  if (resumoQuantidade && Number(resumoQuantidade.value) !== qtd) {
    resumoQuantidade.value = qtd;
  }

  sessionStorage.setItem("pedidoAtual", JSON.stringify(pedidoAtual));
}

resumoQuantidade?.addEventListener("input", () => {
  const novaQtd = Math.min(10, Math.max(1, Number(resumoQuantidade.value) || 1));
  pedidoAtual.quantidade = novaQtd;
  atualizarResumo();
});

// Funções auxiliares de formatação
function somenteNumeros(valor) {
  return valor.replace(/\D/g, "");
}

function formatarTelefone(valor) {
  const nums = somenteNumeros(valor).slice(0, 11);
  if (nums.length === 0) return "";
  if (nums.length <= 2) return `(${nums}`;
  if (nums.length <= 6) return `(${nums.slice(0, 2)}) ${nums.slice(2)}`;
  if (nums.length <= 10) {
    return `(${nums.slice(0, 2)}) ${nums.slice(2, 6)}-${nums.slice(6)}`;
  }
  return `(${nums.slice(0, 2)}) ${nums.slice(2, 7)}-${nums.slice(7)}`;
}

function formatarCep(valor) {
  const nums = somenteNumeros(valor).slice(0, 8);
  if (nums.length <= 5) return nums;
  return `${nums.slice(0, 5)}-${nums.slice(5)}`;
}

telefoneInput?.addEventListener("input", (e) => {
  e.target.value = formatarTelefone(e.target.value);
});

cepInput?.addEventListener("input", (e) => {
  e.target.value = formatarCep(e.target.value);
  cepInput.setCustomValidity("");
});

// Integração com API pública ViaCEP
async function consultarViaCep() {
  const cepLimpo = somenteNumeros(cepInput.value);

  if (cepLimpo.length !== 8) {
    cepFeedback.textContent = "Digite um CEP válido com 8 números antes de buscar.";
    cepFeedback.className = "mt-2 small text-danger fw-semibold";
    cepInput.focus();
    return;
  }

  // Estado de carregamento
  buscarCepButton.disabled = true;
  buscarCepButton.textContent = "Buscando...";
  cepFeedback.textContent = "Consultando endereço nos Correios (ViaCEP)...";
  cepFeedback.className = "mt-2 small text-primary fw-semibold";

  try {
    const resposta = await fetch(`https://viacep.com.br/ws/${cepLimpo}/json/`);

    if (!resposta.ok) {
      throw new Error("Erro HTTP ao consultar ViaCEP");
    }

    const dados = await resposta.json();

    if (dados.erro) {
      cepFeedback.textContent = "CEP não encontrado. Verifique os números ou preencha o endereço manualmente.";
      cepFeedback.className = "mt-2 small text-danger fw-semibold";
      return;
    }

    document.querySelector("#logradouro").value = dados.logradouro || "";
    document.querySelector("#bairro").value = dados.bairro || "";
    document.querySelector("#cidade").value = dados.localidade || "";
    document.querySelector("#estado").value = dados.uf || "";

    cepFeedback.textContent = "Endereço preenchido automaticamente! Informe agora o número da residência.";
    cepFeedback.className = "mt-2 small text-success fw-semibold";
    document.querySelector("#numero").focus();
  } catch (erro) {
    cepFeedback.textContent = "Não foi possível consultar o CEP no momento (verifique sua conexão). Preencha o endereço manualmente.";
    cepFeedback.className = "mt-2 small text-danger fw-semibold";
  } finally {
    buscarCepButton.disabled = false;
    buscarCepButton.textContent = "Buscar CEP";
  }
}

buscarCepButton?.addEventListener("click", consultarViaCep);

// Submissão e validação do formulário
form?.addEventListener("submit", (event) => {
  event.preventDefault();

  const cepLimpo = somenteNumeros(cepInput.value);
  if (cepLimpo.length !== 8) {
    cepInput.setCustomValidity("Informe um CEP com 8 dígitos.");
  } else {
    cepInput.setCustomValidity("");
  }

  const telLimpo = somenteNumeros(telefoneInput.value);
  if (telLimpo.length > 0 && telLimpo.length < 10) {
    telefoneInput.setCustomValidity("Informe um telefone com DDD (10 ou 11 dígitos).");
  } else {
    telefoneInput.setCustomValidity("");
  }

  if (!form.checkValidity()) {
    form.classList.add("was-validated");
    const primeiroInvalido = form.querySelector(":invalid");
    primeiroInvalido?.focus();
    return;
  }

  const produto = buscarProdutoPorId(pedidoAtual.produtoId) || PRODUTOS[0];
  const qtd = pedidoAtual.quantidade;
  const subtotal = produto.preco * qtd;
  const frete = subtotal >= 500 ? 0 : 29.90;
  const total = subtotal + frete;

  const comprovante = {
    numeroPedido: `ABS-${Math.floor(100000 + Math.random() * 900000)}`,
    clienteNome: document.querySelector("#nome").value.trim(),
    clienteEmail: document.querySelector("#email").value.trim(),
    cidadeUf: `${document.querySelector("#cidade").value.trim()}/${document.querySelector("#estado").value.trim().toUpperCase()}`,
    pagamento: document.querySelector("#pagamento").selectedOptions[0].textContent,
    produtoNome: produto.nome,
    quantidade: qtd,
    totalFormatado: formatarMoeda(total)
  };

  sessionStorage.setItem("compraConcluida", JSON.stringify(comprovante));
  sessionStorage.removeItem("pedidoAtual");
  window.location.href = "sucesso.html";
});

atualizarResumo();
