import { useState } from 'react'
import { FaBookmark, FaHeart } from 'react-icons/fa'
import {
  FiBookOpen,
  FiBookmark,
  FiHeart,
  FiMessageCircle,
  FiSend,
  FiTrash2,
} from 'react-icons/fi'

function Post({
  post,
  currentUser,
  isSaved,
  onToggleSave,
  onSharePost,
  onSelectProfile,
  onDeletePost,
  onToggleLike,
  onAddComment,
}) {
  const [expanded, setExpanded] = useState(false)
  const [commentText, setCommentText] = useState('')
  const [showCommentInput, setShowCommentInput] = useState(true)

  const isOwnPost = post.user === currentUser.user || post.isUserCreated

  function handleCommentSubmit(e) {
    e.preventDefault()
    const trimmed = commentText.trim()
    if (!trimmed) return
    onAddComment(post.id, trimmed)
    setCommentText('')
  }

  function handleConfirmDelete() {
    onDeletePost(post.id)
  }

  return (
    <article className="post card shadow-sm border">
      <header className="post__header">
        <button
          type="button"
          className="btn p-0 border-0 d-flex align-items-center gap-2 text-start"
          onClick={() => onSelectProfile(post.user)}
        >
          <img className="post__avatar" src={post.avatar} alt={`Avatar de ${post.user}`} />
          <div className="post__user">
            <div className="d-flex align-items-center gap-1">
              <strong>{post.user}</strong>
              {isOwnPost && (
                <span className="badge bg-warning-subtle text-dark border border-warning-subtle" style={{ fontSize: '10px' }}>
                  Seu post
                </span>
              )}
            </div>
            <span>{post.location}</span>
          </div>
        </button>

        <div className="ms-auto d-flex align-items-center gap-2">
          <span className="badge bg-success-subtle text-success-emphasis border border-success-subtle">
            {post.category}
          </span>

          {isOwnPost && (
            <button
              type="button"
              className="btn btn-sm btn-outline-danger py-1 px-2 d-flex align-items-center gap-1"
              onClick={handleConfirmDelete}
              title="Remover publicação"
              aria-label="Remover publicação"
            >
              <FiTrash2 size={14} />
              <span className="d-none d-sm-inline small">Excluir</span>
            </button>
          )}
        </div>
      </header>

      <div className="position-relative">
        <img
          className="post__image"
          src={post.image}
          alt={post.alt || post.headline}
          onDoubleClick={() => onToggleLike(post.id)}
        />
      </div>

      <div className="news-strip d-flex justify-content-between align-items-center gap-2 px-3 py-2">
        <div className="flex-grow-1">
          <div className="fw-bold small text-dark">{post.headline}</div>
          <small className="text-muted">{post.publishedAt}</small>
        </div>
        {post.fullText && (
          <button
            type="button"
            className="btn btn-brand btn-sm d-flex align-items-center gap-1 fw-semibold px-3 text-nowrap"
            onClick={() => setExpanded(!expanded)}
          >
            <FiBookOpen />
            {expanded ? 'Recolher' : 'Ler matéria'}
          </button>
        )}
      </div>

      <div className="post__content">
        <div className="post__actions">
          <button
            className="icon-button"
            type="button"
            aria-label="Curtir notícia"
            onClick={() => onToggleLike(post.id)}
          >
            {post.likedByMe ? <FaHeart className="text-danger" /> : <FiHeart />}
          </button>
          <button
            className="icon-button"
            type="button"
            aria-label="Comentar notícia"
            onClick={() => setShowCommentInput(!showCommentInput)}
          >
            <FiMessageCircle />
          </button>
          <button
            className="icon-button"
            type="button"
            aria-label="Compartilhar notícia"
            onClick={() => onSharePost(post)}
          >
            <FiSend />
          </button>
          <button
            className="icon-button"
            type="button"
            aria-label="Salvar na lista de leitura"
            onClick={() => onToggleSave(post)}
          >
            {isSaved ? <FaBookmark className="text-brand" /> : <FiBookmark />}
          </button>
        </div>

        <p className="post__likes">
          <strong>{post.likes} curtidas</strong>
        </p>

        <p className="post__caption mb-2">
          <button
            type="button"
            className="btn btn-link p-0 border-0 text-dark fw-bold text-decoration-none me-1 align-baseline"
            onClick={() => onSelectProfile(post.user)}
          >
            {post.user}
          </button>
          {post.summary}
        </p>

        {expanded && post.fullText && (
          <div className="p-3 mb-2 rounded bg-light border-start border-4 border-success small text-dark">
            {post.fullText}
          </div>
        )}

        {post.comments && post.comments.length > 0 && (
          <div className="post__comments mb-2">
            {post.comments.map((comment) => (
              <p key={comment.id} className="small mb-1">
                <button
                  type="button"
                  className="btn btn-link p-0 border-0 text-dark fw-bold text-decoration-none me-1 align-baseline small"
                  onClick={() => onSelectProfile(comment.author)}
                >
                  {comment.author}
                </button>
                <span>{comment.text}</span>
              </p>
            ))}
          </div>
        )}

        {showCommentInput && (
          <form onSubmit={handleCommentSubmit} className="d-flex gap-2 pt-2 border-top mt-2">
            <input
              type="text"
              className="form-control form-control-sm border-0 bg-light"
              placeholder="Comente esta notícia..."
              value={commentText}
              onChange={(e) => setCommentText(e.target.value)}
            />
            <button type="submit" className="btn btn-sm btn-link text-brand text-decoration-none fw-bold px-2">
              Publicar
            </button>
          </form>
        )}
      </div>
    </article>
  )
}

export default Post
