using revision_api.Models;

namespace revision_api.DTOs
{
    public class ArticleDTO
    {
        public int Id { get; set; }
        public string Title { get; set; }
        public string Content { get; set; }
        public int CategoryId { get; set; }

        public ArticleDTO(int id, string title, string content, int categoryId)
        {
            Id = id;
            Title = title;
            Content = content;
            CategoryId = categoryId;
        }

        public Article ArticleDTOToArticle()
        {
            return new Article
            {
                Id = Id,
                Title = Title,
                Content = Content,
                CategoryId = CategoryId
            };
        }
    }
}
