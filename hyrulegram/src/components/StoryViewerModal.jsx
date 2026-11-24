import { useEffect, useState } from 'react'
import {
  FiChevronLeft,
  FiChevronRight,
  FiHeart,
  FiPause,
  FiPlay,
  FiPlus,
  FiSend,
  FiTrash2,
  FiX,
} from 'react-icons/fi'
import { FaHeart } from 'react-icons/fa'

const STORY_DURATION_MS = 5000

function StoryViewerModal({
  isOpen,
  storyGroups,
  initialUser,
  currentUser,
  onClose,
  onMarkViewed,
  onDeleteStoryItem,
  onOpenAddStory,
  onSelectProfile,
  onSendReply,
}) {
  const activeGroups = storyGroups.filter((g) => g.items && g.items.length > 0)
  const initialGroupIdx = Math.max(
    0,
    activeGroups.findIndex((g) => g.user === initialUser)
  )

  const [groupIndex, setGroupIndex] = useState(initialGroupIdx)
  const [itemIndex, setItemIndex] = useState(0)
  const [progress, setProgress] = useState(0)
  const [isPaused, setIsPaused] = useState(false)
  const [likedIds, setLikedIds] = useState([])
  const [replyText, setReplyText] = useState('')

  useEffect(() => {
    if (isOpen) {
      const idx = Math.max(
        0,
        activeGroups.findIndex((g) => g.user === initialUser)
      )
      setGroupIndex(idx)
      setItemIndex(0)
      setProgress(0)
      setIsPaused(false)
      setReplyText('')
    }
  }, [isOpen, initialUser])

  const currentGroup = activeGroups[groupIndex]
  const currentItem = currentGroup?.items?.[itemIndex]

  useEffect(() => {
    if (isOpen && currentItem) {
      onMarkViewed(currentItem.id)
    }
  }, [isOpen, currentItem?.id])

  function handleNext() {
    if (!currentGroup) {
      onClose()
      return
    }
    if (itemIndex < currentGroup.items.length - 1) {
      setItemIndex((prev) => prev + 1)
      setProgress(0)
    } else if (groupIndex < activeGroups.length - 1) {
      setGroupIndex((prev) => prev + 1)
      setItemIndex(0)
      setProgress(0)
    } else {
      onClose()
    }
  }

  function handlePrev() {
    if (itemIndex > 0) {
      setItemIndex((prev) => prev - 1)
      setProgress(0)
    } else if (groupIndex > 0) {
      const prevGroup = activeGroups[groupIndex - 1]
      setGroupIndex((prev) => prev - 1)
      setItemIndex(Math.max(0, prevGroup.items.length - 1))
      setProgress(0)
    } else {
      setProgress(0)
    }
  }

  useEffect(() => {
    if (!isOpen || !currentItem || isPaused) return

    const intervalMs = 50
    const step = (intervalMs / STORY_DURATION_MS) * 100

    const timer = setInterval(() => {
      setProgress((prev) => {
        if (prev + step >= 100) {
          handleNext()
          return 0
        }
        return prev + step
      })
    }, intervalMs)

    return () => clearInterval(timer)
  }, [isOpen, currentItem?.id, groupIndex, itemIndex, isPaused, activeGroups.length])

  useEffect(() => {
    if (!isOpen) return
    function handleKeyDown(e) {
      if (e.key === 'Escape') onClose()
      if (e.key === 'ArrowRight') handleNext()
      if (e.key === 'ArrowLeft') handlePrev()
    }
    window.addEventListener('keydown', handleKeyDown)
    return () => window.removeEventListener('keydown', handleKeyDown)
  })

  if (!isOpen || !currentGroup || !currentItem) return null

  const isOwnStory = currentGroup.user === currentUser.user
  const isLiked = likedIds.includes(currentItem.id)

  function toggleStoryLike() {
    setLikedIds((prev) =>
      prev.includes(currentItem.id)
        ? prev.filter((id) => id !== currentItem.id)
        : [...prev, currentItem.id]
    )
  }

  function handleReplySubmit(e) {
    e.preventDefault()
    const trimmed = replyText.trim()
    if (!trimmed) return
    onSendReply(currentGroup.user, trimmed)
    setReplyText('')
  }

  function handleDeleteCurrent() {
    const idToDelete = currentItem.id
    const remainingInGroup = currentGroup.items.length - 1
    onDeleteStoryItem(currentGroup.user, idToDelete)
    if (remainingInGroup <= 0) {
      if (activeGroups.length <= 1) {
        onClose()
      } else if (groupIndex >= activeGroups.length - 1) {
        setGroupIndex(Math.max(0, groupIndex - 1))
        setItemIndex(0)
      } else {
        setItemIndex(0)
      }
    } else if (itemIndex >= remainingInGroup) {
      setItemIndex(remainingInGroup - 1)
    }
    setProgress(0)
  }

  return (
    <div className="story-viewer-backdrop" role="dialog" aria-modal="true" aria-label="Visualizador de Stories">
      <button
        type="button"
        className="story-nav-arrow d-none d-md-flex"
        onClick={handlePrev}
        aria-label="Story anterior"
      >
        <FiChevronLeft size={28} />
      </button>

      <div className="story-viewer-card shadow-lg">
        {/* Barras de progresso */}
        <div className="story-progress-row">
          {currentGroup.items.map((item, idx) => {
            let widthPercent = 0
            if (idx < itemIndex) widthPercent = 100
            else if (idx === itemIndex) widthPercent = progress
            return (
              <div key={item.id} className="story-progress-track">
                <div
                  className="story-progress-fill"
                  style={{ width: `${widthPercent}%` }}
                />
              </div>
            )
          })}
        </div>

        {/* Cabeçalho do story */}
        <div className="story-viewer-header">
          <button
            type="button"
            className="btn p-0 border-0 d-flex align-items-center gap-2 text-white text-start"
            onClick={() => {
              onClose()
              onSelectProfile(currentGroup.user)
            }}
          >
            <img
              src={currentGroup.avatar}
              alt={currentGroup.fullName}
              className="story-viewer-avatar"
            />
            <div>
              <div className="fw-bold small lh-1">{currentGroup.user}</div>
              <small className="opacity-75" style={{ fontSize: '11px' }}>
                {currentItem.publishedAt}
              </small>
            </div>
          </button>

          <div className="d-flex align-items-center gap-2 ms-auto">
            {isOwnStory && (
              <>
                <button
                  type="button"
                  className="story-header-btn"
                  title="Adicionar mais um story"
                  onClick={() => {
                    onClose()
                    onOpenAddStory()
                  }}
                >
                  <FiPlus size={18} />
                </button>
                <button
                  type="button"
                  className="story-header-btn text-warning"
                  title="Excluir este story"
                  onClick={handleDeleteCurrent}
                >
                  <FiTrash2 size={16} />
                </button>
              </>
            )}
            <button
              type="button"
              className="story-header-btn"
              onClick={() => setIsPaused(!isPaused)}
              aria-label={isPaused ? 'Continuar' : 'Pausar'}
            >
              {isPaused ? <FiPlay size={16} /> : <FiPause size={16} />}
            </button>
            <button
              type="button"
              className="story-header-btn"
              onClick={onClose}
              aria-label="Fechar story"
            >
              <FiX size={20} />
            </button>
          </div>
        </div>

        {/* Conteúdo visual + áreas de clique esquerda/direita */}
        <div className="story-media-wrapper">
          <img
            src={currentItem.image}
            alt={currentItem.caption || `Story de ${currentGroup.user}`}
            className="story-media-img"
          />
          <div className="story-tap-left" onClick={handlePrev} aria-hidden="true" />
          <div className="story-tap-right" onClick={handleNext} aria-hidden="true" />

          {currentItem.caption && (
            <div className="story-caption-overlay">
              <p className="mb-0 small text-white">{currentItem.caption}</p>
            </div>
          )}
        </div>

        {/* Rodapé de interação */}
        <div className="story-viewer-footer">
          <form onSubmit={handleReplySubmit} className="d-flex align-items-center gap-2 flex-grow-1">
            <input
              type="text"
              className="form-control form-control-sm story-reply-input"
              placeholder={`Responder a ${currentGroup.user}...`}
              value={replyText}
              onFocus={() => setIsPaused(true)}
              onBlur={() => setIsPaused(false)}
              onChange={(e) => setReplyText(e.target.value)}
            />
            <button
              type="submit"
              className="story-header-btn"
              aria-label="Enviar resposta"
            >
              <FiSend size={18} />
            </button>
          </form>

          <button
            type="button"
            className="story-header-btn"
            onClick={toggleStoryLike}
            aria-label="Curtir story"
          >
            {isLiked ? <FaHeart className="text-danger" size={20} /> : <FiHeart size={20} />}
          </button>
        </div>
      </div>

      <button
        type="button"
        className="story-nav-arrow d-none d-md-flex"
        onClick={handleNext}
        aria-label="Próximo story"
      >
        <FiChevronRight size={28} />
      </button>
    </div>
  )
}

export default StoryViewerModal
