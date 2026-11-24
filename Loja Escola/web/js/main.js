const listaProdutos = document.querySelector("#listaProdutos");
const inputBusca = document.querySelector("#filtroProduto");
const selectRegra = document.querySelector("#filtroRegra");
const selectOrdenacao = document.querySelector("#ordenarProdutos");
const mensagemFiltro = document.querySelector("#mensagemFiltro");

function renderizarVitrine(itens) {
  if (!listaProdutos) return;

  listaProdutos.innerHTML = "";

  if (itens.length === 0) {
    mensagemFiltro.textContent = "Nenhum produto impossível corresponde aos filtros selecionados.";
    return;
  }

  mensagemFiltro.textContent = `${itens.length} produto(s) exibido(s).`;

  itens.forEach((produto) => {
    const coluna = document.createElement("article");
    coluna.className = "col-sm-6 col-lg-4";

    coluna.innerHTML = `
      <div class="card h-100 product-card">
        <img
          src="${produto.imagem}"
          alt="${produto.alt}"
          class="product-thumb"
          width="400"
          height="300"
          loading="lazy"
        >
        <div class="card-body d-flex flex-column">
          <div class="d-flex justify-content-between align-items-center gap-2 mb-2">
            <span class="small text-secondary">${produto.categoria}</span>
            <span class="regra-badge">${produto.regra}</span>
          </div>
          <h3 class="h5">${produto.nome}</h3>
          <p class="text-secondary flex-grow-1">${produto.descricaoCurta}</p>
          <p class="price mb-3">${formatarMoeda(produto.preco)}</p>
          <a class="btn btn-primario w-100" href="produto.html?id=${produto.id}">
            Ver detalhes de ${produto.nome}
          </a>
        </div>
      </div>
    `;

    listaProdutos.appendChild(coluna);
  });
}

function atualizarVitrine() {
  const termo = inputBusca.value.trim().toLowerCase();
  const regraSelecionada = selectRegra.value;
  const ordenacao = selectOrdenacao.value;

  let filtrados = PRODUTOS.filter((produto) => {
    const bateTexto =
      produto.nome.toLowerCase().includes(termo) ||
      produto.descricaoCurta.toLowerCase().includes(termo) ||
      produto.categoria.toLowerCase().includes(termo);

    const bateRegra = regraSelecionada === "todas" || produto.regra === regraSelecionada;
    return bateTexto && bateRegra;
  });

  if (ordenacao === "nome-asc") {
    filtrados.sort((a, b) => a.nome.localeCompare(b.nome, "pt-BR"));
  } else if (ordenacao === "preco-asc") {
    filtrados.sort((a, b) => a.preco - b.preco);
  } else if (ordenacao === "preco-desc") {
    filtrados.sort((a, b) => b.preco - a.preco);
  }

  renderizarVitrine(filtrados);
}

inputBusca?.addEventListener("input", atualizarVitrine);
selectRegra?.addEventListener("change", atualizarVitrine);
selectOrdenacao?.addEventListener("change", atualizarVitrine);

// Carrega vitrine inicial
atualizarVitrine();
