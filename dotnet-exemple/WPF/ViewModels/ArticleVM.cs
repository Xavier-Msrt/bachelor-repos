using System.ComponentModel;
using Microsoft.EntityFrameworkCore.Metadata.Internal;
using WPF.Models;
using WpfApplication1.ViewModels;

namespace WPF.ViewModels
{
    public class ArticleVM : INotifyPropertyChanged
    {

         // Global
        private ArticlesDbContext _context = new ArticlesDbContext();

        public ArticleVM()
        {
            UpdateCommand = new DelegateCommand(UpdateCommandMethode);
        }
        


        // Get all
        private List<ArticleModel> _articles;
        public List<ArticleModel> ListeArticle
        {
            get { 
                return _articles = _articles ?? LoadArticle(); 
            }
        }

        public List<ArticleModel> LoadArticle()
        {
            List<ArticleModel> articleModels = new List<ArticleModel>();
            foreach (Article article in _context.Articles)
            {
                articleModels.Add(new ArticleModel(article.Id, article.Title, article.Content, article.CategoryId));
            }
            return articleModels;
        }


        // Selected
        private ArticleModel _selectedArticle;



        public event PropertyChangedEventHandler PropertyChanged;
        public void onPropertyChanged(string propertyName) // methode generique pour appeler 
        {
            PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
        }

        public ArticleModel SelectedArticle
        {
            get { return _selectedArticle; }
            set
            {
                _selectedArticle = value;
                onPropertyChanged("SelectedArticle"); // Fournir la valuer qui a chnager
            }
        }


        // Boutton
        private void UpdateCommandMethode()
        {
            if (_selectedArticle != null)
            {
                Article? article = (from Article in _context.Articles
                                    where Article.Id == _selectedArticle.Id
                                    select Article).FirstOrDefault();
                if (article != null)
                {
                    article.Title = _selectedArticle.Title;
                    article.Content = _selectedArticle.Content;
                    _context.SaveChanges();
                }
            }
        }
        public DelegateCommand UpdateCommand
        {
            get; set;
        }
    }
}
