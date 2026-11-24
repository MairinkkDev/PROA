const params = new URLSearchParams(window.location.search);
const idParam = params.get("id");
const produtoAtual = buscarProdutoPorId(idParam);

const areaProduto = document.querySelector("#areaProduto");
const erroProduto = document.querySelector("#erroProduto");
const breadcrumbAtual = document.querySelector("#breadcrumbAtual");
const formProduto = document.querySelector("#formProduto");
const inputQuantidade = document.querySelector("#quantidade");
const feedbackProduto = document.querySelector("#produtoFeedback");

function carregarDetalhesProduto() {
  if (!produtoAtual) {
    areaProduto.hidden = true;
    erroProduto.hidden = false;
    breadcrumbAtual.textContent = "Produto não encontrado";
    document.title = "Loja Absurdália | Produto não encontrado";
    return;
  }

  document.title = `Loja Absurdália | ${produtoAtual.nome}`;
  breadcrumbAtual.textContent = produtoAtual.nome;

  document.querySelector("#produtoImagem").src = produtoAtual.imagem;
  document.querySelector("#produtoImagem").alt = produtoAtual.alt;
  document.querySelector("#produtoCategoria").textContent = produtoAtual.categoria;
  document.querySelector("#produtoRegra").textContent = `Regra atendida: ${produtoAtual.regra}`;
  document.querySelector("#produtoNome").textContent = produtoAtual.nome;
  document.querySelector("#produtoDescricaoCurta").textContent = produtoAtual.descricaoCurta;
  document.querySelector("#produtoDescricaoDetalhada").textContent = produtoAtual.descricaoDetalhada;
  document.querySelector("#produtoPreco").textContent = formatarMoeda(produtoAtual.preco);

  const listaEspecificacoes = document.querySelector("#produtoEspecificacoes");
  listaEspecificacoes.innerHTML = "";
  produtoAtual.especificacoes.forEach((item) => {
    const li = document.createElement("li");
    li.textContent = item;
    listaEspecificacoes.appendChild(li);
  });
}

formProduto?.addEventListener("submit", (event) => {
  event.preventDefault();
  if (!produtoAtual) return;

  const qtd = Number(inputQuantidade.value);

  if (!Number.isInteger(qtd) || qtd < 1 || qtd > 10) {
    feedbackProduto.textContent = "Por favor, informe uma quantidade válida entre 1 e 10 unidades.";
    feedbackProduto.className = "mt-3 text-danger fw-semibold";
    inputQuantidade.focus();
    return;
  }

  const itemPedido = {
    produtoId: produtoAtual.id,
    quantidade: qtd
  };

  sessionStorage.setItem("pedidoAtual", JSON.stringify(itemPedido));
  window.location.href = "checkout.html";
});

carregarDetalhesProduto();
