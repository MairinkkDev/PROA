import { useState } from 'react'
import { FiImage, FiPlusCircle, FiX } from 'react-icons/fi'

const PRESET_STORY_IMAGES = [
  { label: 'Triforce Dourada', url: '/images/stories/story-zelda-1.jpg' },
  { label: 'Tecnologia Sheikah', url: '/images/stories/story-sheikah-1.jpg' },
  { label: 'Rupee Esmeralda', url: '/images/stories/story-herald-1.jpg' },
  { label: 'Floresta Korok', url: '/images/stories/story-korok-1.jpg' },
]

function AddStoryModal({ isOpen, onClose, onPublishStory }) {
  const [selectedImage, setSelectedImage] = useState(PRESET_STORY_IMAGES[0].url)
  const [caption, setCaption] = useState('')
  const [error, setError] = useState('')

  if (!isOpen) return null

  function handleFileUpload(e) {
    const file = e.target.files?.[0]
    if (!file) return
    if (!file.type.startsWith('image/')) {
      setError('Selecione um arquivo de imagem válido.')
      return
    }
    const reader = new FileReader()
    reader.onload = () => {
      setSelectedImage(reader.result)
      setError('')
    }
    reader.readAsDataURL(file)
  }

  function handleSubmit(e) {
    e.preventDefault()
    if (!selectedImage) {
      setError('Escolha ou envie uma imagem para o story.')
      return
    }
    onPublishStory({
      image: selectedImage,
      caption: caption.trim(),
    })
    setCaption('')
    setError('')
    onClose()
  }

  return (
    <div className="modal-backdrop-custom" role="dialog" aria-modal="true" aria-label="Novo Story">
      <div className="card shadow-lg border-0 saved-modal-card">
        <div className="card-header bg-white d-flex justify-content-between align-items-center py-3">
          <h5 className="mb-0 d-flex align-items-center gap-2 fw-bold">
            <FiPlusCircle className="text-brand" />
            Adicionar ao seu Story
          </h5>
          <button type="button" className="btn btn-sm btn-light" onClick={onClose} aria-label="Fechar">
            <FiX size={18} />
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="card-body">
            {error && <div className="alert alert-danger py-2 small">{error}</div>}

            <div className="mb-3 text-center">
              <div className="story-preview-box mx-auto">
                <img src={selectedImage} alt="Prévia do story" className="w-100 h-100 object-fit-cover" />
                {caption.trim() && (
                  <div className="story-preview-caption small">{caption}</div>
                )}
              </div>
            </div>

            <div className="mb-3">
              <label className="form-label small fw-semibold d-flex align-items-center gap-1">
                <FiImage /> Enviar foto do dispositivo
              </label>
              <input
                type="file"
                accept="image/*"
                className="form-control form-control-sm"
                onChange={handleFileUpload}
              />
            </div>

            <div className="mb-3">
              <label className="form-label small fw-semibold">Ou escolha um fundo temático de Hyrule:</label>
              <div className="d-flex gap-2 flex-wrap">
                {PRESET_STORY_IMAGES.map((preset) => (
                  <button
                    key={preset.url}
                    type="button"
                    className={`btn btn-sm ${
                      selectedImage === preset.url ? 'btn-brand' : 'btn-outline-secondary'
                    }`}
                    onClick={() => setSelectedImage(preset.url)}
                  >
                    {preset.label}
                  </button>
                ))}
              </div>
            </div>

            <div>
              <label htmlFor="story-caption" className="form-label small fw-semibold">
                Legenda do story (opcional)
              </label>
              <input
                id="story-caption"
                type="text"
                maxLength={140}
                className="form-control form-control-sm"
                placeholder="Compartilhe uma novidade rápida de Hyrule..."
                value={caption}
                onChange={(e) => setCaption(e.target.value)}
              />
            </div>
          </div>

          <div className="card-footer bg-white py-3 d-flex justify-content-end gap-2">
            <button type="button" className="btn btn-sm btn-outline-secondary" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className="btn btn-sm btn-brand fw-semibold px-3">
              Publicar Story
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default AddStoryModal
