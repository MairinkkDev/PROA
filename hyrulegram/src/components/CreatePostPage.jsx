import { useState } from 'react'
import { FiArrowLeft, FiImage, FiMapPin, FiPlusSquare, FiTag } from 'react-icons/fi'

const POST_CATEGORIES = ['Lançamentos', 'Curiosidades', 'Cinema & TV', 'Música & Eventos']

const GALLERY_PRESETS = [
  { label: 'Cajado Tri & Triforce', url: '/images/pre/cajado.jpg' },
  { label: 'Espada Mestra Zonai', url: '/pre/avatars/sword.jpg' },
  { label: 'Brasão Real de Hyrule', url: '/pre/avatars/brasao.jpg' },
  { label: 'Ocarina do Tempo', url: '/pre/avatars/ocarina.jpg' },
]

function CreatePostPage({ currentUser, onCreatePost, onCancel }) {
  const [headline, setHeadline] = useState('')
  const [summary, setSummary] = useState('')
  const [fullText, setFullText] = useState('')
  const [category, setCategory] = useState(POST_CATEGORIES[0])
  const [location, setLocation] = useState('Castelo de Hyrule • Comunidade')
  const [image, setImage] = useState(GALLERY_PRESETS[0].url)
  const [error, setError] = useState('')

  function handleFileUpload(e) {
    const file = e.target.files?.[0]
    if (!file) return
    if (!file.type.startsWith('image/')) {
      setError('Por favor, selecione um arquivo de imagem válido.')
      return
    }
    const reader = new FileReader()
    reader.onload = () => {
      setImage(reader.result)
      setError('')
    }
    reader.readAsDataURL(file)
  }

  function handleSubmit(e) {
    e.preventDefault()
    if (!headline.trim()) {
      setError('Informe a manchete da publicação.')
      return
    }
    if (!summary.trim()) {
      setError('Escreva uma legenda ou resumo para a publicação.')
      return
    }
    if (!image) {
      setError('Selecione ou envie uma imagem para a publicação.')
      return
    }

    onCreatePost({
      headline: headline.trim(),
      summary: summary.trim(),
      fullText: fullText.trim() || summary.trim(),
      category,
      location: location.trim() || 'Hyrule',
      image,
      alt: headline.trim(),
    })
  }

  return (
    <section className="create-post-page card shadow-sm border mb-4" aria-label="Criar nova publicação">
      <div className="card-header bg-white py-3 d-flex align-items-center justify-content-between">
        <div className="d-flex align-items-center gap-2">
          <button
            type="button"
            className="btn btn-sm btn-light"
            onClick={onCancel}
            aria-label="Voltar ao feed"
          >
            <FiArrowLeft />
          </button>
          <h5 className="mb-0 fw-bold d-flex align-items-center gap-2">
            <FiPlusSquare className="text-brand" />
            Nova Publicação
          </h5>
        </div>
        <span className="small text-muted">Postando como @{currentUser.user}</span>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="card-body">
          {error && <div className="alert alert-danger py-2 small">{error}</div>}

          {/* Pré-visualização da imagem */}
          <div className="mb-3">
            <label className="form-label small fw-bold">Prévia da Imagem</label>
            <div className="create-preview-frame rounded overflow-hidden border bg-light">
              <img src={image} alt="Prévia da publicação" className="w-100 h-100 object-fit-cover" />
            </div>
          </div>

          <div className="mb-3">
            <label htmlFor="post-file" className="form-label small fw-semibold d-flex align-items-center gap-1">
              <FiImage /> Carregar imagem do seu dispositivo
            </label>
            <input
              id="post-file"
              type="file"
              accept="image/*"
              className="form-control form-control-sm"
              onChange={handleFileUpload}
            />
          </div>

          <div className="mb-3">
            <label className="form-label small fw-semibold">Ou escolha uma arte de Hyrule:</label>
            <div className="d-flex gap-2 flex-wrap">
              {GALLERY_PRESETS.map((item) => (
                <button
                  key={item.url}
                  type="button"
                  className={`btn btn-sm ${
                    image === item.url ? 'btn-brand' : 'btn-outline-secondary'
                  }`}
                  onClick={() => setImage(item.url)}
                >
                  {item.label}
                </button>
              ))}
            </div>
          </div>

          <div className="mb-3">
            <label htmlFor="post-headline" className="form-label small fw-bold">
              Manchete / Título da Notícia *
            </label>
            <input
              id="post-headline"
              type="text"
              className="form-control"
              placeholder="Ex.: Nova teoria conecta as ruínas Zonai ao Templo do Tempo"
              value={headline}
              onChange={(e) => setHeadline(e.target.value)}
            />
          </div>

          <div className="mb-3">
            <label htmlFor="post-summary" className="form-label small fw-bold">
              Legenda / Resumo no Feed *
            </label>
            <textarea
              id="post-summary"
              rows={2}
              className="form-control"
              placeholder="Escreva o texto principal que aparecerá abaixo da foto..."
              value={summary}
              onChange={(e) => setSummary(e.target.value)}
            />
          </div>

          <div className="mb-3">
            <label htmlFor="post-fulltext" className="form-label small fw-bold">
              Matéria Completa (exibida ao clicar em &quot;Ler matéria&quot;)
            </label>
            <textarea
              id="post-fulltext"
              rows={3}
              className="form-control"
              placeholder="Detalhes adicionais, curiosidades ou análise completa (opcional)..."
              value={fullText}
              onChange={(e) => setFullText(e.target.value)}
            />
          </div>

          <div className="row g-2">
            <div className="col-sm-6">
              <label htmlFor="post-category" className="form-label small fw-semibold d-flex align-items-center gap-1">
                <FiTag /> Categoria
              </label>
              <select
                id="post-category"
                className="form-select form-select-sm"
                value={category}
                onChange={(e) => setCategory(e.target.value)}
              >
                {POST_CATEGORIES.map((cat) => (
                  <option key={cat} value={cat}>
                    {cat}
                  </option>
                ))}
              </select>
            </div>

            <div className="col-sm-6">
              <label htmlFor="post-location" className="form-label small fw-semibold d-flex align-items-center gap-1">
                <FiMapPin /> Localização
              </label>
              <input
                id="post-location"
                type="text"
                className="form-control form-control-sm"
                placeholder="Ex.: Vila Hateno"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
              />
            </div>
          </div>
        </div>

        <div className="card-footer bg-white py-3 d-flex justify-content-between align-items-center">
          <button type="button" className="btn btn-outline-secondary btn-sm" onClick={onCancel}>
            Cancelar
          </button>
          <button type="submit" className="btn btn-brand fw-semibold px-4">
            Publicar no Feed
          </button>
        </div>
      </form>
    </section>
  )
}

export default CreatePostPage
