using Repository;
using revision_api.Models;
using School.Repository;

namespace revision_api.UnitOfWork
{
    public class UniteOfWorkDB
    {
        private readonly ArticlesDbContext _context;

        private BaseRepositorySQL<Article> _articleRepo;
        private BaseRepositorySQL<Category> _categoryRepo;

        public UniteOfWorkDB(ArticlesDbContext context)
        {
            _context = context;
            _articleRepo = new BaseRepositorySQL<Article>(context);
            _categoryRepo = new BaseRepositorySQL<Category>(context);
        }

        public IRepository<Article> ArticleRepository
        {
            get { return _articleRepo; }
        }

        public IRepository<Category> CategoryRepository
        {
            get { return _categoryRepo; }
        }
    }
}
