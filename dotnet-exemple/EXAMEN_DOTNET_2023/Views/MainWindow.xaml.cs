using System.Windows;
using EXAMEN_DOTNET_2023.ViewModels;


namespace EXAMEN_DOTNET_2023
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            this.DataContext = new ProductVM();
        }
    }
}