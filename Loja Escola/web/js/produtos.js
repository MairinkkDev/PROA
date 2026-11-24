// Catálogo único da Loja Absurdália compartilhado entre todas as páginas
const PRODUTOS = [
  {
    id: 1,
    nome: "Guarda-Chuva Oráculo de Bolso",
    categoria: "Clima & Bolso",
    regra: "Cabe em um bolso",
    preco: 149.90,
    imagem: "img/guarda-chuva.svg",
    alt: "Ilustração de um guarda-chuva azul compacto com gotas de chuva e um sensor dourado no topo",
    descricaoCurta: "Cabe no bolso da calça e vibra 5 minutos antes de cair a primeira gota exatamente onde você está.",
    descricaoDetalhada: "Dobrável até atingir o tamanho de uma caixa de fósforos, o Guarda-Chuva Oráculo analisa a umidade atmosférica local e avisa com precisão cirúrgica onde e quando a chuva vai cair, abrindo automaticamente uma cúpula impermeável.",
    especificacoes: [
      "Material: Fibra de carbono flexível e tecido hidrofóbico",
      "Dimensões fechado: 9 cm x 4 cm x 3 cm (aberto: 1,05 m)",
      "Modo de uso: Guarde no bolso e aguarde o bip meteorológico"
    ]
  },
  {
    id: 2,
    nome: "Estação Orbital Formigário Zero-G",
    categoria: "Micro-Espaço",
    regra: "Serve em uma formiga",
    preco: 320.00,
    imagem: "img/estacao-formiga.svg",
    alt: "Ilustração de uma redoma espacial transparente flutuando no espaço com uma pequena formiga de capacete dentro",
    descricaoCurta: "Cápsula de descanso pressurizada feita sob medida para formigas exploradoras em missão.",
    descricaoDetalhada: "Projetada com microgravidade simulada e néctar sintético de longa duração, esta estação oferece conforto total para formigas que precisam descansar entre expedições interplanetárias ou longas jornadas no jardim.",
    especificacoes: [
      "Material: Policarbonato aeroespacial e liga de titânio leve",
      "Dimensões: 1,8 cm de diâmetro por 1,1 cm de altura",
      "Modo de uso: Posicionar próximo ao formigueiro com a escotilha aberta"
    ]
  },
  {
    id: 3,
    nome: "Jardim Flutuante Arquipélago",
    categoria: "Paisagismo Aéreo",
    regra: "Não cabe em um avião",
    preco: 4850.00,
    imagem: "img/jardim-flutuante.svg",
    alt: "Ilustração de uma ilha de terra e grama com árvores flutuando nas nuvens ao lado de um pequeno avião",
    descricaoCurta: "Uma ilha verde suspensa nas nuvens com árvores frutíferas e lago próprio, maior que qualquer avião.",
    descricaoDetalhada: "Ideal para quem quer fugir do trânsito urbano. Este ecossistema autossustentável flutua a 400 metros de altitude graças a cristais de hélio sólido e não cabe em nenhum hangar ou aeronave comercial.",
    especificacoes: [
      "Material: Rocha porosa flutuante, solo fértil e flora nativa",
      "Dimensões: 95 metros de comprimento por 60 metros de largura",
      "Modo de uso: Acesso exclusivo por balão, escada de corda ou teletransporte"
    ]
  },
  {
    id: 4,
    nome: "Zumbilume Onírico",
    categoria: "Sonhos & Luz",
    regra: "Começa com a letra Z",
    preco: 89.50,
    imagem: "img/zumbilume.svg",
    alt: "Ilustração de um frasco de vidro brilhando com luz amarela e estrelas em um fundo noturno roxo",
    descricaoCurta: "Frasco luminoso que projeta sonhos lúcidos coloridos no quarto durante uma noite inteira.",
    descricaoDetalhada: "Ao abrir a rolha do Zumbilume antes de dormir, uma névoa dourada preenche o ambiente e transforma pesadelos comuns em aventuras tranquilas. Começa com Z e ilumina a imaginação.",
    especificacoes: [
      "Material: Vidro soprado artesanal e poeira de vaga-lume estelar",
      "Dimensões: 14 cm de altura por 7 cm de largura",
      "Modo de uso: Destampar no criado-mudo 10 minutos antes de deitar"
    ]
  },
  {
    id: 5,
    nome: "Botas Magnéticas de Órbita Baixa",
    categoria: "Micro-Espaço",
    regra: "Usado no espaço sideral",
    preco: 790.00,
    imagem: "img/botas-gravidade.svg",
    alt: "Ilustração de um par de botas espaciais cinzas com solado azul brilhante flutuando no espaço",
    descricaoCurta: "Calçado desenvolvido para caminhar com firmeza no casco externo de naves e asteroides.",
    descricaoDetalhada: "Equipadas com solado eletromagnético adaptativo e isolamento térmico contra o vácuo, estas botas garantem passos seguros fora da atmosfera terrestre sem precisar de cabos de segurança.",
    especificacoes: [
      "Material: Kevlar térmico, alumínio escovado e ímãs de neodímio",
      "Dimensões: Numeração ajustável do 35 ao 44",
      "Modo de uso: Ativar o botão lateral ao sair da câmara de descompressão"
    ]
  },
  {
    id: 6,
    nome: "Zelo Instantâneo de Bolso",
    categoria: "Utilidades Efêmeras",
    regra: "Uso único",
    preco: 45.00,
    imagem: "img/zelo-bolso.svg",
    alt: "Ilustração de um pequeno relógio de bolso rosa com ponteiros dourados",
    descricaoCurta: "Amuleto mecânico de uso único que congela o tempo por 10 minutos para você terminar uma tarefa.",
    descricaoDetalhada: "Atrasado para entregar um trabalho ou pegar um ônibus? Aperte o pino superior do Zelo Instantâneo uma única vez para ganhar 10 minutos extras exclusivos antes que o mecanismo se desfaça em confete.",
    especificacoes: [
      "Material: Latão biodegradável e engrenagens de açúcar cristalizado",
      "Dimensões: 4,5 cm de diâmetro",
      "Modo de uso: Pressionar o botão central uma única vez (não recarregável)"
    ]
  }
];

function formatarMoeda(valor) {
  return valor.toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL"
  });
}

function buscarProdutoPorId(id) {
  return PRODUTOS.find((item) => item.id === Number(id));
}
