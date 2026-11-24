import postsJson from './posts.json'

export const CURRENT_USER = {
  user: 'heroi.de.hyrule',
  fullName: 'Herói de Hyrule',
  bio: 'Explorador das terras de Hyrule, colecionador de Koroks e repórter da comunidade.',
  location: 'Santuário da Vida • Hyrule',
  avatar: '/images/avatars/heroi-hyrule.jpg',
  followers: 1280,
  following: 142,
  isCurrentUser: true,
}

export const INITIAL_PROFILES = {
  'heroi.de.hyrule': CURRENT_USER,
  'zelda.oficial': {
    user: 'zelda.oficial',
    fullName: 'Crônicas de Hyrule',
    bio: 'Portal oficial de novidades, trailers e bastidores da franquia The Legend of Zelda.',
    location: 'Castelo de Hyrule • Nintendo',
    avatar: '/images/avatars/zelda-oficial.jpg',
    followers: 48200,
    following: 18,
  },
  'sheikah.news': {
    user: 'sheikah.news',
    fullName: 'Arquivo Sheikah',
    bio: 'Análises técnicas, descobertas de lore, segredos Zonai e teorias da linha do tempo.',
    location: 'Vila Kakariko • Laboratório Ancestral',
    avatar: '/images/avatars/sheikah-news.jpg',
    followers: 19450,
    following: 64,
  },
  'hylian.herald': {
    user: 'hylian.herald',
    fullName: 'Arauto de Hyrule',
    bio: 'Cobertura completa sobre adaptações, cinema, eventos oficiais e cultura gamer.',
    location: 'Planície de Hyrule • Redação Central',
    avatar: '/images/avatars/hylian-herald.jpg',
    followers: 31800,
    following: 41,
  },
  'korok.daily': {
    user: 'korok.daily',
    fullName: 'Diário da Floresta Korok',
    bio: 'Trilhas sonoras, concertos sinfônicos, artes de fãs e segredos escondidos sob cada pedra.',
    location: 'Floresta Perdida • Grande Árvore Deku',
    avatar: '/images/avatars/korok-daily.jpg',
    followers: 14900,
    following: 900,
  },
}

export const INITIAL_POSTS = postsJson.map((post) => ({
  ...post,
  isUserCreated: false,
}))

export const INITIAL_STORIES = [
  {
    user: 'zelda.oficial',
    fullName: 'Crônicas de Hyrule',
    avatar: '/images/avatars/zelda-oficial.jpg',
    items: [
      {
        id: 'st-zelda-1',
        image: '/images/stories/story-zelda-1.jpg',
        caption: 'Bastidores exclusivos: veja como foram criadas as fendas do Mundo Parado em Echoes of Wisdom!',
        publishedAt: 'Há 1 h',
      },
      {
        id: 'st-zelda-2',
        image: '/images/stories/story-zelda-2.jpg',
        caption: 'Qual Eco você mais usou na sua jornada? Responda nos comentários do nosso último post!',
        publishedAt: 'Há 45 min',
      },
    ],
  },
  {
    user: 'sheikah.news',
    fullName: 'Arquivo Sheikah',
    avatar: '/images/avatars/sheikah-news.jpg',
    items: [
      {
        id: 'st-sheikah-1',
        image: '/images/stories/story-sheikah-1.jpg',
        caption: 'Alerta de descoberta Zonai: novo glitch de voo infinito testado no Laboratório de Kakariko.',
        publishedAt: 'Há 3 h',
      },
    ],
  },
  {
    user: 'hylian.herald',
    fullName: 'Arauto de Hyrule',
    avatar: '/images/avatars/hylian-herald.jpg',
    items: [
      {
        id: 'st-herald-1',
        image: '/images/stories/story-herald-1.jpg',
        caption: 'Direto da redação: novidades sobre o elenco e as locações naturais do filme de Zelda.',
        publishedAt: 'Há 4 h',
      },
    ],
  },
  {
    user: 'korok.daily',
    fullName: 'Diário da Floresta Korok',
    avatar: '/images/avatars/korok-daily.jpg',
    items: [
      {
        id: 'st-korok-1',
        image: '/images/stories/story-korok-1.jpg',
        caption: 'Yahaha! Você encontrou o story secreto de hoje na Floresta Perdida!',
        publishedAt: 'Há 6 h',
      },
    ],
  },
]
