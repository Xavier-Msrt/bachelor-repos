using Repository;
using revision_api.Models;

namespace revision_api.Repositories
{
    public class ArticleRepositorySQL : BaseRepositorySQL<Article>
    {
        public ArticleRepositorySQL(ArticlesDbContext context) : base(context) { }

        public async Task<IList<Article>> GetByCategoryIdAsync(int categoryId)
        {
            return await SearchForAsync(a => a.CategoryId == categoryId);
        }
    }
}
