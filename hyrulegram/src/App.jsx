import { useEffect, useState } from 'react'
import { FiBookmark, FiHome, FiPlusSquare, FiSearch, FiUser } from 'react-icons/fi'
import Header from './components/Header'
import Stories from './components/Stories'
import StoryViewerModal from './components/StoryViewerModal'
import AddStoryModal from './components/AddStoryModal'
import Post from './components/Post'
import CreatePostPage from './components/CreatePostPage'
import ProfileView from './components/ProfileView'
import SavedNewsModal from './components/SavedNewsModal'
import {
  CURRENT_USER,
  INITIAL_POSTS,
  INITIAL_PROFILES,
  INITIAL_STORIES,
} from './data/initialData'
import './App.css'

const CATEGORIES = ['Todas', 'Lançamentos', 'Curiosidades', 'Cinema & TV', 'Música & Eventos']
const STORAGE_KEY = 'hyrulegram_state_v2'

function loadInitialState() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (!raw) return null
    return JSON.parse(raw)
  } catch {
    return null
  }
}

function App() {
  const savedState = loadInitialState()

  const [currentUser, setCurrentUser] = useState(savedState?.currentUser || CURRENT_USER)
  const [profiles, setProfiles] = useState(savedState?.profiles || INITIAL_PROFILES)
  const [posts, setPosts] = useState(savedState?.posts || INITIAL_POSTS)
  const [storyGroups, setStoryGroups] = useState(savedState?.storyGroups || INITIAL_STORIES)
  const [viewedStoryIds, setViewedStoryIds] = useState(savedState?.viewedStoryIds || [])
  const [followingUsers, setFollowingUsers] = useState(
    savedState?.followingUsers || ['zelda.oficial', 'sheikah.news']
  )
  const [savedPostIds, setSavedPostIds] = useState(savedState?.savedPostIds || [])

  // Navegação: 'feed' | 'create' | 'profile'
  const [activeView, setActiveView] = useState('feed')
  const [profileUsername, setProfileUsername] = useState(CURRENT_USER.user)

  // Filtros do feed
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedCategory, setSelectedCategory] = useState('Todas')

  // Modais
  const [activeStoryUser, setActiveStoryUser] = useState(null)
  const [isAddStoryOpen, setIsAddStoryOpen] = useState(false)
  const [isSavedOpen, setIsSavedOpen] = useState(false)
  const [toastMessage, setToastMessage] = useState('')

  useEffect(() => {
    try {
      localStorage.setItem(
        STORAGE_KEY,
        JSON.stringify({
          currentUser,
          profiles,
          posts,
          storyGroups,
          viewedStoryIds,
          followingUsers,
          savedPostIds,
        })
      )
    } catch {
      // Ignora erro de quota do localStorage caso imagens base64 sejam grandes
    }
  }, [currentUser, profiles, posts, storyGroups, viewedStoryIds, followingUsers, savedPostIds])

  function showNotification(message) {
    setToastMessage(message)
    setTimeout(() => {
      setToastMessage((curr) => (curr === message ? '' : curr))
    }, 3000)
  }

  // Posts ordenados: posts criados pelo usuário sempre primeiro no feed
  const orderedPosts = [
    ...posts.filter((p) => p.isUserCreated || p.user === currentUser.user),
    ...posts.filter((p) => !p.isUserCreated && p.user !== currentUser.user),
  ]

  const filteredPosts = orderedPosts.filter((post) => {
    const matchesCategory =
      selectedCategory === 'Todas' || post.category === selectedCategory
    const term = searchTerm.toLowerCase()
    const matchesSearch =
      post.headline.toLowerCase().includes(term) ||
      post.user.toLowerCase().includes(term) ||
      post.summary.toLowerCase().includes(term) ||
      post.category.toLowerCase().includes(term)
    return matchesCategory && matchesSearch
  })

  const savedPosts = orderedPosts.filter((p) => savedPostIds.includes(p.id))

  // Navegação
  function handleNavigateFeed() {
    setActiveView('feed')
    setSelectedCategory('Todas')
    setSearchTerm('')
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  function handleNavigateCreate() {
    setActiveView('create')
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  function handleOpenProfile(username) {
    setProfileUsername(username)
    setActiveView('profile')
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  // CRUD de Posts
  function handleCreatePost(newPostData) {
    const newPost = {
      id: Date.now(),
      user: currentUser.user,
      fullName: currentUser.fullName,
      bio: currentUser.bio,
      location: newPostData.location,
      avatar: currentUser.avatar,
      image: newPostData.image,
      alt: newPostData.alt,
      headline: newPostData.headline,
      summary: newPostData.summary,
      fullText: newPostData.fullText,
      publishedAt: 'Agora mesmo',
      category: newPostData.category,
      likes: 1,
      likedByMe: true,
      isUserCreated: true,
      comments: [],
    }

    setPosts((prev) => [newPost, ...prev])
    setActiveView('feed')
    setSelectedCategory('Todas')
    setSearchTerm('')
    showNotification('Publicação criada! Ela aparece no topo do feed.')
  }

  function handleDeletePost(postId) {
    setPosts((prev) => prev.filter((p) => p.id !== postId))
    setSavedPostIds((prev) => prev.filter((id) => id !== postId))
    showNotification('Publicação removida.')
  }

  function handleToggleLike(postId) {
    setPosts((prev) =>
      prev.map((post) => {
        if (post.id !== postId) return post
        const nextLiked = !post.likedByMe
        return {
          ...post,
          likedByMe: nextLiked,
          likes: nextLiked ? post.likes + 1 : Math.max(0, post.likes - 1),
        }
      })
    )
  }

  function handleAddComment(postId, text) {
    setPosts((prev) =>
      prev.map((post) => {
        if (post.id !== postId) return post
        return {
          ...post,
          comments: [
            ...(post.comments || []),
            {
              id: Date.now(),
              author: currentUser.user,
              text,
            },
          ],
        }
      })
    )
  }

  function handleToggleSave(post) {
    setSavedPostIds((prev) => {
      const exists = prev.includes(post.id)
      if (exists) {
        showNotification('Notícia removida dos salvos.')
        return prev.filter((id) => id !== post.id)
      }
      showNotification(`Matéria salva: "${post.headline}"`)
      return [...prev, post.id]
    })
  }

  function handleSharePost(post) {
    showNotification(`Link copiado para compartilhar: "${post.headline}"`)
  }

  // Stories
  function handlePublishStory({ image, caption }) {
    const newItem = {
      id: `st-user-${Date.now()}`,
      image,
      caption,
      publishedAt: 'Agora mesmo',
    }

    setStoryGroups((prev) => {
      const existingIdx = prev.findIndex((g) => g.user === currentUser.user)
      if (existingIdx >= 0) {
        const updated = [...prev]
        updated[existingIdx] = {
          ...updated[existingIdx],
          fullName: currentUser.fullName,
          avatar: currentUser.avatar,
          items: [...updated[existingIdx].items, newItem],
        }
        return updated
      }
      return [
        {
          user: currentUser.user,
          fullName: currentUser.fullName,
          avatar: currentUser.avatar,
          items: [newItem],
        },
        ...prev,
      ]
    })

    showNotification('Seu story foi publicado!')
  }

  function handleDeleteStoryItem(username, storyItemId) {
    setStoryGroups((prev) =>
      prev
        .map((group) => {
          if (group.user !== username) return group
          return {
            ...group,
            items: group.items.filter((item) => item.id !== storyItemId),
          }
        })
        .filter((group) => group.items.length > 0)
    )
    showNotification('Story removido.')
  }

  function handleMarkStoryViewed(storyItemId) {
    setViewedStoryIds((prev) => (prev.includes(storyItemId) ? prev : [...prev, storyItemId]))
  }

  // Perfil
  function handleToggleFollow(username) {
    setFollowingUsers((prev) => {
      const exists = prev.includes(username)
      if (exists) {
        showNotification(`Você deixou de seguir @${username}.`)
        return prev.filter((u) => u !== username)
      }
      showNotification(`Agora você está seguindo @${username}!`)
      return [...prev, username]
    })
  }

  function handleUpdateCurrentUser(updatedFields) {
    const updatedUser = { ...currentUser, ...updatedFields }
    setCurrentUser(updatedUser)
    setProfiles((prev) => ({
      ...prev,
      [currentUser.user]: updatedUser,
    }))
    setPosts((prev) =>
      prev.map((post) =>
        post.user === currentUser.user
          ? { ...post, fullName: updatedUser.fullName, bio: updatedUser.bio }
          : post
      )
    )
    showNotification('Perfil atualizado com sucesso!')
  }

  // Dados para a tela de perfil selecionada
  const targetProfile =
    profileUsername === currentUser.user
      ? currentUser
      : profiles[profileUsername] || {
          user: profileUsername,
          fullName: profileUsername,
          bio: 'Membro da comunidade Hyrulegram.',
          location: 'Hyrule',
          avatar: '/images/avatars/zelda-oficial.jpg',
          followers: 120,
          following: 45,
        }

  const profilePosts = orderedPosts.filter((p) => p.user === targetProfile.user)
  const userStoryGroup = storyGroups.find((g) => g.user === currentUser.user)
  const targetStoryGroup = storyGroups.find(
    (g) => g.user === targetProfile.user && g.items.length > 0
  )

  return (
    <div className="app">
      <Header
        searchTerm={searchTerm}
        onSearchChange={(val) => {
          setSearchTerm(val)
          if (activeView !== 'feed') setActiveView('feed')
        }}
        savedCount={savedPosts.length}
        onOpenSaved={() => setIsSavedOpen(true)}
        activeView={activeView}
        onNavigateFeed={handleNavigateFeed}
        onNavigateCreate={handleNavigateCreate}
        onNavigateMyProfile={() => handleOpenProfile(currentUser.user)}
        currentUser={currentUser}
      />

      {toastMessage && (
        <div className="alert alert-success shadow-sm toast-banner py-2 px-3 small fw-semibold" role="status">
          {toastMessage}
        </div>
      )}

      <main className="feed container px-2 px-sm-0" aria-label="Conteúdo principal do Hyrulegram">
        {activeView === 'create' && (
          <CreatePostPage
            currentUser={currentUser}
            onCreatePost={handleCreatePost}
            onCancel={() => setActiveView('feed')}
          />
        )}

        {activeView === 'profile' && (
          <ProfileView
            profile={targetProfile}
            currentUser={currentUser}
            userPosts={profilePosts}
            savedPosts={savedPosts}
            hasActiveStory={Boolean(targetStoryGroup)}
            isFollowing={followingUsers.includes(targetProfile.user)}
            onToggleFollow={handleToggleFollow}
            onUpdateCurrentUser={handleUpdateCurrentUser}
            onOpenStory={(user) => setActiveStoryUser(user)}
            onOpenAddStory={() => setIsAddStoryOpen(true)}
            onOpenCreatePost={handleNavigateCreate}
            onDeletePost={handleDeletePost}
            onToggleSave={handleToggleSave}
            onSharePost={handleSharePost}
            onSelectProfile={handleOpenProfile}
            onToggleLike={handleToggleLike}
            onAddComment={handleAddComment}
            onBackToFeed={() => setActiveView('feed')}
          />
        )}

        {activeView === 'feed' && (
          <>
            <Stories
              currentUser={currentUser}
              userStoryGroup={userStoryGroup}
              storyGroups={storyGroups}
              viewedStoryIds={viewedStoryIds}
              onOpenStory={(user) => setActiveStoryUser(user)}
              onOpenAddStory={() => setIsAddStoryOpen(true)}
            />

            {/* Atalho rápido para criar post no topo do feed */}
            <div className="card shadow-sm border mb-3 create-prompt-card">
              <div className="card-body py-2 px-3 d-flex align-items-center gap-2">
                <button
                  type="button"
                  className="btn p-0 border-0 bg-transparent"
                  onClick={() => handleOpenProfile(currentUser.user)}
                  title="Ver meu perfil"
                >
                  <img
                    src={currentUser.avatar}
                    alt={currentUser.fullName}
                    className="create-prompt-avatar"
                  />
                </button>
                <button
                  type="button"
                  className="btn btn-light btn-sm rounded-pill flex-grow-1 text-start text-muted px-3"
                  onClick={handleNavigateCreate}
                >
                  Compartilhe uma novidade ou descoberta em Hyrule...
                </button>
                <button
                  type="button"
                  className="btn btn-brand btn-sm rounded-pill fw-semibold px-3 d-flex align-items-center gap-1"
                  onClick={handleNavigateCreate}
                >
                  <FiPlusSquare /> Postar
                </button>
              </div>
            </div>

            <div className="d-flex gap-2 overflow-auto mb-3 pb-1" aria-label="Filtro por categoria">
              {CATEGORIES.map((category) => (
                <button
                  key={category}
                  type="button"
                  className={`btn btn-sm rounded-pill text-nowrap ${
                    selectedCategory === category
                      ? 'btn-brand fw-semibold'
                      : 'btn-outline-secondary bg-white'
                  }`}
                  onClick={() => setSelectedCategory(category)}
                >
                  {category}
                </button>
              ))}
            </div>

            <div className="d-sm-none mb-3">
              <input
                type="search"
                className="form-control form-control-sm"
                placeholder="Buscar notícia ou canal..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>

            {filteredPosts.length === 0 ? (
              <div className="card p-4 text-center text-muted">
                Nenhuma notícia encontrada para esse filtro.
              </div>
            ) : (
              filteredPosts.map((post) => (
                <Post
                  key={post.id}
                  post={post}
                  currentUser={currentUser}
                  isSaved={savedPostIds.includes(post.id)}
                  onToggleSave={handleToggleSave}
                  onSharePost={handleSharePost}
                  onSelectProfile={handleOpenProfile}
                  onDeletePost={handleDeletePost}
                  onToggleLike={handleToggleLike}
                  onAddComment={handleAddComment}
                />
              ))
            )}
          </>
        )}
      </main>

      {/* Modais */}
      <StoryViewerModal
        isOpen={Boolean(activeStoryUser)}
        storyGroups={storyGroups}
        initialUser={activeStoryUser}
        currentUser={currentUser}
        onClose={() => setActiveStoryUser(null)}
        onMarkViewed={handleMarkStoryViewed}
        onDeleteStoryItem={handleDeleteStoryItem}
        onOpenAddStory={() => setIsAddStoryOpen(true)}
        onSelectProfile={handleOpenProfile}
        onSendReply={(user, text) => showNotification(`Resposta enviada para @${user}: "${text}"`)}
      />

      <AddStoryModal
        isOpen={isAddStoryOpen}
        onClose={() => setIsAddStoryOpen(false)}
        onPublishStory={handlePublishStory}
      />

      <SavedNewsModal
        isOpen={isSavedOpen}
        onClose={() => setIsSavedOpen(false)}
        savedPosts={savedPosts}
        onRemoveSaved={(id) =>
          setSavedPostIds((prev) => prev.filter((item) => item !== id))
        }
        onClearSaved={() => setSavedPostIds([])}
      />

      {/* Navegação Mobile */}
      <nav className="mobile-nav" aria-label="Navegação móvel">
        <button
          type="button"
          className={activeView === 'feed' ? 'active-nav-icon' : ''}
          aria-label="Início"
          onClick={handleNavigateFeed}
        >
          <FiHome />
        </button>
        <button
          type="button"
          aria-label="Pesquisar"
          onClick={() => {
            setActiveView('feed')
            window.scrollTo({ top: 0, behavior: 'smooth' })
          }}
        >
          <FiSearch />
        </button>
        <button
          type="button"
          className={activeView === 'create' ? 'active-nav-icon' : ''}
          aria-label="Criar publicação"
          onClick={handleNavigateCreate}
        >
          <FiPlusSquare />
        </button>
        <button
          type="button"
          className="position-relative"
          aria-label="Notícias salvas"
          onClick={() => setIsSavedOpen(true)}
        >
          <FiBookmark />
          {savedPosts.length > 0 && (
            <span className="badge rounded-pill saved-badge">
              {savedPosts.length}
            </span>
          )}
        </button>
        <button
          type="button"
          className={
            activeView === 'profile' && profileUsername === currentUser.user
              ? 'active-nav-icon'
              : ''
          }
          aria-label="Meu perfil"
          onClick={() => handleOpenProfile(currentUser.user)}
        >
          <FiUser />
        </button>
      </nav>
    </div>
  )
}

export default App
