using System;
using System.Collections.Generic;

namespace EXAMEN_DOTNET_2023.Models;

public partial class ProductsAboveAveragePrice
{
    public string ProductName { get; set; } = null!;

    public decimal? UnitPrice { get; set; }
}
