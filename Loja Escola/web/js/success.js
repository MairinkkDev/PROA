const painelSucesso = document.querySelector("#painelSucesso");
const painelBloqueado = document.querySelector("#painelBloqueado");

function carregarComprovante() {
  let dados = null;
  try {
    dados = JSON.parse(sessionStorage.getItem("compraConcluida"));
  } catch (e) {
    dados = null;
  }

  // Impede acesso direto caso não exista compra concluída na sessão
  if (!dados || !dados.numeroPedido) {
    painelSucesso.hidden = true;
    painelBloqueado.hidden = false;
    document.title = "Loja Absurdália | Nenhum pedido encontrado";
    return;
  }

  document.querySelector("#mensagemCliente").textContent =
    `${dados.clienteNome}, seu pedido impossível foi registrado com sucesso!`;
  document.querySelector("#numeroPedido").textContent = dados.numeroPedido;
  document.querySelector("#resumoItem").textContent = `${dados.quantidade}x ${dados.produtoNome}`;
  document.querySelector("#resumoTotalPago").textContent = dados.totalFormatado;
  document.querySelector("#resumoEntrega").textContent = `${dados.cidadeUf} (${dados.pagamento})`;
}

carregarComprovante();
