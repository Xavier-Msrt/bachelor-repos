using System.ComponentModel;
using System.Diagnostics.Metrics;
using EXAMEN_DOTNET_2023.Models;
using Microsoft.EntityFrameworkCore;

namespace EXAMEN_DOTNET_2023.ViewModels
{
    public class ProductVM : INotifyPropertyChanged
    {
        private NorthwindContext _context = new NorthwindContext();



        // 1
        private List<ProductModel> _unDiscontinedProduct;

        public List<ProductModel> UnDiscontinedProductList
        {
            get { return _unDiscontinedProduct = _unDiscontinedProduct ?? loadingUnDiscontinedProduct(); }
        }

        public List<ProductModel> loadingUnDiscontinedProduct()
        {
            List<ProductModel> productsList = new List<ProductModel>();
            var products = _context.Products
                                   .Include(p => p.Category)
                                   .Include(p => p.Supplier)
                                   .Where(p => !p.Discontinued)
                                   .ToList();

            foreach (var p in products)
            {
                productsList.Add(new ProductModel(
                    p.ProductId,
                    p.ProductName,
                    p.SupplierId,
                    p.CategoryId,
                    p.QuantityPerUnit,
                    p.UnitPrice,
                    p.UnitsInStock,
                    p.UnitsOnOrder,
                    p.ReorderLevel,
                    p.Discontinued,
                    p.Category,
                    p.Supplier
                ));
            }

            return productsList;
        }



        // 2
        private ProductModel _selectedProduct;
        public event PropertyChangedEventHandler PropertyChanged;

        public void OnPropertyChanged(string propertyName)
        {
            PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
        }

        public ProductModel SelectedProduct
        {
            get { return _selectedProduct; }
            set
            {
                _selectedProduct = value;
                OnPropertyChanged("SelectedProduct");
            }

        }


        // 3
        public DelegateCommand SetDiscontinued
        {
            get; set;
        }

        private void SetDiscontinuedMethode()
        {
            if (_selectedProduct != null)
            {
                Product p = _context.Products.Where( p => p.ProductId.Equals(SelectedProduct.ProductId) ).First();
                p.Discontinued = true;
                _context.SaveChanges();

                // update/ remove the element
                _unDiscontinedProduct = loadingUnDiscontinedProduct();
                SelectedProduct = _unDiscontinedProduct.First();
                OnPropertyChanged("UnDiscontinedProductList");
            }

        }

        public ProductVM() => SetDiscontinued = new DelegateCommand(SetDiscontinuedMethode);


        // 4
        public List<ProductCountByCountry> NumberProductPerPays
        {
            get
            {
        return _context.Products
                       .Where( p => p.OrderDetails.Any())
                       .Include(p => p.Supplier)
                       .AsEnumerable() // Force client-side evaluation
                       .GroupBy(p => p.Supplier.Country)
                       .Select(g => new ProductCountByCountry(g.Key, g.Count()))
                       .OrderByDescending(p => p.ProductCount)
                       .ToList();
            }
        }



    }
}
