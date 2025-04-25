using Repository;
using revision_api.Models;

namespace revision_api.Repositories
{
    public class ArticleRepositorySQL : BaseRepositorySQL<Article>
    {
        public ArticleRepositorySQL(ArticlesDbContext context) : base(context) { }
    }
}
