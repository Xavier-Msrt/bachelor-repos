using revision_api.Models;
using School.Repository;

namespace revision_api.UnitOfWork
{
    public interface UniteOfWork
    {
        IRepository<Article> ArticleRepository { get; }
        IRepository<Category> CategoryRepository { get; }
    }
}
