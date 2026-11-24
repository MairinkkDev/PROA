import { FiBookmark, FiHome, FiPlusSquare, FiSearch, FiUser } from 'react-icons/fi'

function Header({
  searchTerm,
  onSearchChange,
  savedCount,
  onOpenSaved,
  activeView,
  onNavigateFeed,
  onNavigateCreate,
  onNavigateMyProfile,
  currentUser,
}) {
  return (
    <header className="header shadow-sm">
      <button type="button" className="header__logo btn p-0 border-0" onClick={onNavigateFeed}>
        <span>▲</span>
        <span>Hyrulegram</span>
      </button>

      <div className="header__search d-none d-sm-flex align-items-center">
        <FiSearch className="text-muted me-2" />
        <input
          type="search"
          className="form-control form-control-sm border-0 bg-transparent shadow-none"
          placeholder="Buscar notícia ou canal..."
          value={searchTerm}
          onChange={(e) => onSearchChange(e.target.value)}
          aria-label="Buscar notícia ou canal"
        />
      </div>

      <nav className="header__actions align-items-center" aria-label="Navegação principal">
        <button
          className={`icon-button ${activeView === 'feed' ? 'active-nav-icon' : ''}`}
          type="button"
          aria-label="Feed inicial"
          title="Feed inicial"
          onClick={onNavigateFeed}
        >
          <FiHome />
        </button>

        <button
          className={`icon-button ${activeView === 'create' ? 'active-nav-icon' : ''}`}
          type="button"
          aria-label="Criar publicação"
          title="Criar publicação"
          onClick={onNavigateCreate}
        >
          <FiPlusSquare />
        </button>

        <button
          className="icon-button position-relative"
          type="button"
          aria-label="Notícias salvas"
          title="Notícias salvas"
          onClick={onOpenSaved}
        >
          <FiBookmark />
          {savedCount > 0 && (
            <span className="badge rounded-pill saved-badge fw-bold">
              {savedCount}
            </span>
          )}
        </button>

        <button
          className={`icon-button profile-nav-btn ${activeView === 'profile' ? 'active-nav-icon' : ''}`}
          type="button"
          aria-label="Meu perfil"
          title={`Perfil de ${currentUser.user}`}
          onClick={onNavigateMyProfile}
        >
          {currentUser.avatar ? (
            <img
              src={currentUser.avatar}
              alt={currentUser.fullName}
              className="header__avatar"
            />
          ) : (
            <FiUser />
          )}
        </button>
      </nav>
    </header>
  )
}

export default Header
