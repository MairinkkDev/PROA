import { FiBookmark, FiTrash2, FiX } from 'react-icons/fi'

function SavedNewsModal({ isOpen, onClose, savedPosts, onRemoveSaved, onClearSaved }) {
  if (!isOpen) return null

  return (
    <div className="modal-backdrop-custom" role="dialog" aria-modal="true" aria-label="Notícias salvas">
      <div className="card shadow-lg border-0 saved-modal-card">
        <div className="card-header bg-white d-flex justify-content-between align-items-center py-3">
          <h5 className="mb-0 d-flex align-items-center gap-2 fw-bold">
            <FiBookmark className="text-brand" />
            Notícias Salvas
          </h5>
          <button type="button" className="btn btn-sm btn-light" onClick={onClose} aria-label="Fechar">
            <FiX size={18} />
          </button>
        </div>

        <div className="card-body" style={{ maxHeight: '60vh', overflowY: 'auto' }}>
          {savedPosts.length === 0 ? (
            <p className="text-muted text-center my-4">
              Nenhuma notícia salva ainda. Clique no ícone de marcador em qualquer publicação para ler mais tarde!
            </p>
          ) : (
            <ul className="list-group list-group-flush">
              {savedPosts.map((item) => (
                <li key={item.id} className="list-group-item px-0 d-flex align-items-center gap-3">
                  <img
                    src={item.image}
                    alt={item.headline}
                    className="rounded"
                    style={{ width: '56px', height: '56px', objectFit: 'cover' }}
                  />
                  <div className="flex-grow-1">
                    <div className="fw-semibold small">{item.headline}</div>
                    <div className="text-muted small">
                      @{item.user} • {item.category}
                    </div>
                  </div>
                  <button
                    type="button"
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => onRemoveSaved(item.id)}
                    aria-label={`Remover ${item.headline}`}
                  >
                    <FiTrash2 />
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>

        {savedPosts.length > 0 && (
          <div className="card-footer bg-white py-3 d-flex justify-content-between align-items-center">
            <span className="text-muted small">
              {savedPosts.length} {savedPosts.length === 1 ? 'matéria salva' : 'matérias salvas'}
            </span>
            <button type="button" className="btn btn-sm btn-outline-secondary" onClick={onClearSaved}>
              Limpar lista
            </button>
          </div>
        )}
      </div>
    </div>
  )
}

export default SavedNewsModal
