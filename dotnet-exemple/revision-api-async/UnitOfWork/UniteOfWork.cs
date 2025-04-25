using Repository;
using revision_api.Models;

namespace revision_api.UnitOfWork
{
    public interface UniteOfWork
    {
        IRepository<Article> ArticleRepository { get; }
        IRepository<Category> CategoryRepository { get; }
    }
}
