import { useState } from 'react'
import {
  FiArrowLeft,
  FiBookmark,
  FiCheck,
  FiEdit2,
  FiGrid,
  FiList,
  FiMapPin,
  FiPlus,
  FiPlusSquare,
  FiTrash2,
  FiUserCheck,
  FiUserPlus,
} from 'react-icons/fi'
import Post from './Post'

function ProfileView({
  profile,
  currentUser,
  userPosts,
  savedPosts,
  hasActiveStory,
  isFollowing,
  onToggleFollow,
  onUpdateCurrentUser,
  onOpenStory,
  onOpenAddStory,
  onOpenCreatePost,
  onDeletePost,
  onToggleSave,
  onSharePost,
  onSelectProfile,
  onToggleLike,
  onAddComment,
  onBackToFeed,
}) {
  const isOwnProfile = profile.user === currentUser.user
  const [displayMode, setDisplayMode] = useState('grid') // 'grid' | 'feed' | 'saved'
  const [isEditing, setIsEditing] = useState(false)
  const [editFullName, setEditFullName] = useState(profile.fullName)
  const [editBio, setEditBio] = useState(profile.bio)
  const [editLocation, setEditLocation] = useState(profile.location || '')

  function handleStartEdit() {
    setEditFullName(currentUser.fullName)
    setEditBio(currentUser.bio)
    setEditLocation(currentUser.location || '')
    setIsEditing(true)
  }

  function handleSaveProfile(e) {
    e.preventDefault()
    if (!editFullName.trim()) return
    onUpdateCurrentUser({
      fullName: editFullName.trim(),
      bio: editBio.trim(),
      location: editLocation.trim(),
    })
    setIsEditing(false)
  }

  const displayedFollowers = (profile.followers || 0) + (isFollowing && !isOwnProfile ? 1 : 0)

  return (
    <section className="profile-page" aria-label={`Perfil de ${profile.user}`}>
      {/* Barra superior do perfil */}
      <div className="d-flex align-items-center justify-content-between mb-3">
        <button
          type="button"
          className="btn btn-sm btn-white bg-white border d-flex align-items-center gap-1 fw-semibold"
          onClick={onBackToFeed}
        >
          <FiArrowLeft /> Voltar ao Feed
        </button>
        <span className="small text-muted fw-semibold">@{profile.user}</span>
      </div>

      {/* Cabeçalho completo do Perfil */}
      <div className="card shadow-sm border mb-3 profile-header-card">
        <div className="card-body p-3 p-sm-4">
          <div className="d-flex align-items-center gap-3 gap-sm-4 mb-3">
            <button
              type="button"
              className="btn p-0 border-0 bg-transparent position-relative"
              onClick={() =>
                hasActiveStory
                  ? onOpenStory(profile.user)
                  : isOwnProfile
                  ? onOpenAddStory()
                  : null
              }
              title={hasActiveStory ? 'Ver story ativo' : profile.fullName}
            >
              <div className={`profile-avatar-ring ${hasActiveStory ? 'has-story' : ''}`}>
                <img
                  src={profile.avatar}
                  alt={profile.fullName}
                  className="profile-page__avatar"
                />
              </div>
            </button>

            <div className="flex-grow-1">
              <div className="d-flex justify-content-around text-center">
                <div>
                  <div className="fw-bold fs-5 lh-1">{userPosts.length}</div>
                  <small className="text-muted">Publicações</small>
                </div>
                <div>
                  <div className="fw-bold fs-5 lh-1">
                    {displayedFollowers.toLocaleString('pt-BR')}
                  </div>
                  <small className="text-muted">Seguidores</small>
                </div>
                <div>
                  <div className="fw-bold fs-5 lh-1">{profile.following || 0}</div>
                  <small className="text-muted">Seguindo</small>
                </div>
              </div>
            </div>
          </div>

          {/* Bio e detalhes */}
          {!isEditing ? (
            <div className="mb-3">
              <h5 className="fw-bold mb-0">{profile.fullName}</h5>
              {profile.location && (
                <div className="small text-brand fw-medium d-flex align-items-center gap-1 mt-1">
                  <FiMapPin size={13} />
                  <span>{profile.location}</span>
                </div>
              )}
              <p className="small text-secondary mt-2 mb-0">{profile.bio}</p>
            </div>
          ) : (
            <form onSubmit={handleSaveProfile} className="mb-3 p-3 bg-light rounded border">
              <div className="mb-2">
                <label className="form-label small fw-bold mb-1">Nome de exibição</label>
                <input
                  type="text"
                  className="form-control form-control-sm"
                  value={editFullName}
                  onChange={(e) => setEditFullName(e.target.value)}
                />
              </div>
              <div className="mb-2">
                <label className="form-label small fw-bold mb-1">Localização</label>
                <input
                  type="text"
                  className="form-control form-control-sm"
                  value={editLocation}
                  onChange={(e) => setEditLocation(e.target.value)}
                />
              </div>
              <div className="mb-2">
                <label className="form-label small fw-bold mb-1">Biografia</label>
                <textarea
                  rows={2}
                  className="form-control form-control-sm"
                  value={editBio}
                  onChange={(e) => setEditBio(e.target.value)}
                />
              </div>
              <div className="d-flex justify-content-end gap-2">
                <button
                  type="button"
                  className="btn btn-sm btn-outline-secondary"
                  onClick={() => setIsEditing(false)}
                >
                  Cancelar
                </button>
                <button type="submit" className="btn btn-sm btn-brand fw-semibold">
                  <FiCheck className="me-1" /> Salvar
                </button>
              </div>
            </form>
          )}

          {/* Botões de ação do perfil */}
          {isOwnProfile ? (
            <div className="d-flex gap-2 flex-wrap">
              <button
                type="button"
                className="btn btn-sm btn-brand flex-grow-1 fw-semibold d-flex align-items-center justify-content-center gap-1"
                onClick={onOpenCreatePost}
              >
                <FiPlusSquare /> Criar post
              </button>
              <button
                type="button"
                className="btn btn-sm btn-outline-success flex-grow-1 fw-semibold d-flex align-items-center justify-content-center gap-1"
                onClick={onOpenAddStory}
              >
                <FiPlus /> Novo story
              </button>
              {!isEditing && (
                <button
                  type="button"
                  className="btn btn-sm btn-outline-secondary fw-semibold d-flex align-items-center justify-content-center gap-1"
                  onClick={handleStartEdit}
                >
                  <FiEdit2 /> Editar perfil
                </button>
              )}
            </div>
          ) : (
            <div className="d-flex gap-2">
              <button
                type="button"
                className={`btn btn-sm flex-grow-1 fw-semibold d-flex align-items-center justify-content-center gap-1 ${
                  isFollowing ? 'btn-outline-secondary' : 'btn-brand'
                }`}
                onClick={() => onToggleFollow(profile.user)}
              >
                {isFollowing ? (
                  <>
                    <FiUserCheck /> Seguindo
                  </>
                ) : (
                  <>
                    <FiUserPlus /> Seguir
                  </>
                )}
              </button>
              {hasActiveStory && (
                <button
                  type="button"
                  className="btn btn-sm btn-outline-success fw-semibold"
                  onClick={() => onOpenStory(profile.user)}
                >
                  Ver story
                </button>
              )}
            </div>
          )}
        </div>

        {/* Abas de visualização dentro do perfil */}
        <div className="card-footer bg-white p-0 d-flex border-top">
          <button
            type="button"
            className={`profile-tab-btn flex-grow-1 ${displayMode === 'grid' ? 'active' : ''}`}
            onClick={() => setDisplayMode('grid')}
          >
            <FiGrid className="me-1" /> Grade ({userPosts.length})
          </button>
          <button
            type="button"
            className={`profile-tab-btn flex-grow-1 ${displayMode === 'feed' ? 'active' : ''}`}
            onClick={() => setDisplayMode('feed')}
          >
            <FiList className="me-1" /> Lista
          </button>
          {isOwnProfile && (
            <button
              type="button"
              className={`profile-tab-btn flex-grow-1 ${displayMode === 'saved' ? 'active' : ''}`}
              onClick={() => setDisplayMode('saved')}
            >
              <FiBookmark className="me-1" /> Salvos ({savedPosts.length})
            </button>
          )}
        </div>
      </div>

      {/* Conteúdo das abas do perfil */}
      {displayMode === 'saved' && isOwnProfile ? (
        savedPosts.length === 0 ? (
          <div className="card p-4 text-center text-muted small">
            Você ainda não salvou nenhuma publicação.
          </div>
        ) : (
          savedPosts.map((post) => (
            <Post
              key={post.id}
              post={post}
              currentUser={currentUser}
              isSaved={true}
              onToggleSave={onToggleSave}
              onSharePost={onSharePost}
              onSelectProfile={onSelectProfile}
              onDeletePost={onDeletePost}
              onToggleLike={onToggleLike}
              onAddComment={onAddComment}
            />
          ))
        )
      ) : userPosts.length === 0 ? (
        <div className="card p-4 text-center text-muted">
          <p className="mb-2 small">
            {isOwnProfile
              ? 'Você ainda não criou nenhuma publicação.'
              : 'Este perfil ainda não possui publicações.'}
          </p>
          {isOwnProfile && (
            <button
              type="button"
              className="btn btn-sm btn-brand mx-auto fw-semibold"
              onClick={onOpenCreatePost}
            >
              Criar minha primeira publicação
            </button>
          )}
        </div>
      ) : displayMode === 'grid' ? (
        <div className="profile-posts-grid">
          {userPosts.map((post) => (
            <div key={post.id} className="profile-grid-tile position-relative rounded overflow-hidden border bg-white">
              <img
                src={post.image}
                alt={post.headline}
                className="w-100 h-100 object-fit-cover"
                onClick={() => setDisplayMode('feed')}
                title="Clique para abrir em formato lista"
              />
              <div className="profile-grid-overlay" onClick={() => setDisplayMode('feed')}>
                <span className="small fw-bold text-white text-truncate px-2">
                  ♥ {post.likes}
                </span>
              </div>
              {isOwnProfile && (
                <button
                  type="button"
                  className="btn btn-danger btn-sm profile-grid-delete"
                  onClick={(e) => {
                    e.stopPropagation()
                    onDeletePost(post.id)
                  }}
                  title="Remover publicação"
                  aria-label={`Remover ${post.headline}`}
                >
                  <FiTrash2 size={13} />
                </button>
              )}
            </div>
          ))}
        </div>
      ) : (
        userPosts.map((post) => (
          <Post
            key={post.id}
            post={post}
            currentUser={currentUser}
            isSaved={savedPosts.some((item) => item.id === post.id)}
            onToggleSave={onToggleSave}
            onSharePost={onSharePost}
            onSelectProfile={onSelectProfile}
            onDeletePost={onDeletePost}
            onToggleLike={onToggleLike}
            onAddComment={onAddComment}
          />
        ))
      )}
    </section>
  )
}

export default ProfileView
