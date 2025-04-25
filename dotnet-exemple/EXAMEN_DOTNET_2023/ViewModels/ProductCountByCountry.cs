using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EXAMEN_DOTNET_2023.ViewModels
{
    public class ProductCountByCountry
    {
        public string Country { get; set; }
        public int ProductCount { get; set; }

        public ProductCountByCountry(string country, int productCount)
        {
            Country = country;
            ProductCount = productCount;
        }
    }
}
