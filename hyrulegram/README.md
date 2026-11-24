# Hyrulegram — Rede Social de Notícias de The Legend of Zelda

Réplica do layout do Instagram transformada em uma **rede social dedicada a notícias, curiosidades, cinema e música da franquia The Legend of Zelda**, desenvolvida com **React**, **JavaScript**, **HTML**, **CSS** e **Bootstrap 5**.

## Funcionalidades

- **Identidade visual Hyrulegram**: paleta verde-esmeralda de Hyrule e dourado da Triforce, com tipografia `Cinzel` e `Plus Jakarta Sans`.
- **Feed de notícias (`src/data/posts.json`)**: matérias temáticas com manchete, resumo, texto completo expansível, categoria, canal autor e data de publicação.
- **Leitura completa na publicação**: botão **Ler matéria / Recolher** para expandir o artigo completo diretamente no card.
- **Lista de Notícias Salvas**: botão de marcador em cada publicação, contador no cabeçalho e modal dedicado para gerenciar matérias salvas para ler depois.
- **Filtro por canais (Stories) e categorias**: barra superior de canais oficiais/comunidade (`@zelda.oficial`, `@sheikah.news`, `@hylian.herald`, `@korok.daily`), filtro por categorias (`Lançamentos`, `Curiosidades`, `Cinema & TV`, `Música & Eventos`) e busca em tempo real.
- **Curtidas, comentários e compartilhamento**: curtidas com clique ou duplo clique na imagem, adição de comentários em tempo real e notificação de compartilhamento.

## Como executar

```bash
npm install
npm run dev
```
