using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WPF.ViewModels
{
    public  class ArticleModel
    {
        public int Id { get; set; }

        public string Title { get; set; } = null!;

        public string Content { get; set; } = null!;

        public int CategoryId { get; set; }

        public ArticleModel(int id, string title, string content, int categoryId) 
        {
            Id = id;
            Title = title;
            Content = content;
            CategoryId = categoryId;
        }

    }
}
