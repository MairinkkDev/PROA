import { FiPlus } from 'react-icons/fi'

function Stories({
  currentUser,
  userStoryGroup,
  storyGroups,
  viewedStoryIds,
  onOpenStory,
  onOpenAddStory,
}) {
  const hasUserStories = userStoryGroup && userStoryGroup.items.length > 0
  const allUserViewed =
    hasUserStories && userStoryGroup.items.every((item) => viewedStoryIds.includes(item.id))

  return (
    <section className="stories-bar card shadow-sm mb-4" aria-label="Stories de Hyrule">
      <div className="card-body py-3 px-3 d-flex gap-3 overflow-auto align-items-center">
        {/* Story do Usuário Atual */}
        <div className="story-item position-relative">
          <button
            type="button"
            className="btn p-0 border-0 d-flex flex-column align-items-center gap-1 bg-transparent"
            onClick={() => (hasUserStories ? onOpenStory(currentUser.user) : onOpenAddStory())}
            aria-label={hasUserStories ? 'Ver seu story' : 'Adicionar ao seu story'}
          >
            <div
              className={`story-ring ${
                !hasUserStories ? 'no-story-ring' : allUserViewed ? 'viewed' : ''
              }`}
            >
              <img
                src={currentUser.avatar}
                alt={currentUser.fullName}
                className="story-avatar"
              />
            </div>
            <small className="story-name fw-medium">Seu story</small>
          </button>

          <button
            type="button"
            className="story-add-badge"
            onClick={onOpenAddStory}
            title="Publicar novo story"
            aria-label="Publicar novo story"
          >
            <FiPlus size={12} strokeWidth={3} />
          </button>
        </div>

        {/* Stories dos Canais / Perfis */}
        {storyGroups
          .filter((group) => group.user !== currentUser.user && group.items.length > 0)
          .map((group) => {
            const allViewed = group.items.every((item) => viewedStoryIds.includes(item.id))
            return (
              <button
                key={group.user}
                type="button"
                className="story-item"
                onClick={() => onOpenStory(group.user)}
              >
                <div className={`story-ring ${allViewed ? 'viewed' : ''}`}>
                  <img
                    src={group.avatar}
                    alt={`Story de ${group.user}`}
                    className="story-avatar"
                  />
                </div>
                <small className="story-name">{group.user}</small>
              </button>
            )
          })}
      </div>
    </section>
  )
}

export default Stories
