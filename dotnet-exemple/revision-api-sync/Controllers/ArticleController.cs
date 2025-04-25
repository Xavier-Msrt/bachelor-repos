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
        public List<ArticleDTO> GetAll()
        {
            IList<Article> articles = unitOfWork.ArticleRepository.GetAll();
            return articles.Select(a => ArticleToArticleDTO(a)).ToList();
        }

        [HttpPost]
        public IActionResult Add(ArticleDTO articleDTO)
        {
            Category category = unitOfWork.CategoryRepository.GetById(articleDTO.CategoryId);
            if (category == null)
            {
                return BadRequest("Category doesn't exist");
            }
            articleDTO.Id = 0;
            Article article = articleDTO.ArticleDTOToArticle();
            article.Category = category;
            unitOfWork.ArticleRepository.Insert(article);
            return Ok(ArticleToArticleDTO(article));
        }
    }
}
