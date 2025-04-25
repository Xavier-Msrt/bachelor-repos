using Microsoft.AspNetCore.Mvc;
using revision_api.DTOs;
using revision_api.Models;
using revision_api.UnitOfWork;

namespace revision_api.Controllers
{
    [ApiController]
    [Route("/api/[controller]")]
    public class ArticleController : ControllerBase
    {
        private readonly ArticlesDbContext context;
        private readonly UniteOfWorkDB unitOfWork;

        public ArticleController()
        {
            context = new ArticlesDbContext();
            unitOfWork = new UniteOfWorkDB(context);
        }

        private ArticleDTO ArticleToArticleDTO(Article article) =>
            new ArticleDTO(
                article.Id,
                article.Title,
                article.Content,
                article.CategoryId
            );

        [HttpGet]
        public async Task<List<ArticleDTO>> GetAll()
        {
            IList<Article> articles = await unitOfWork.ArticleRepository.GetAllAsync();
            return articles.Select(a => ArticleToArticleDTO(a)).ToList();
        }

        [HttpPost]
        public async Task<IActionResult> Add(ArticleDTO articleDTO)
        {
            Category? category = await unitOfWork.CategoryRepository.GetByIdAsync(articleDTO.CategoryId);
            if (category == null)
            {
                return BadRequest("Category doesn't exist");
            }
            articleDTO.Id = 0;
            Article article = articleDTO.ArticleDTOToArticle();
            article.Category = category;
            await unitOfWork.ArticleRepository.InsertAsync(article);
            return Ok(ArticleToArticleDTO(article));
        }
    }
}
