using System;
using System.Collections.Generic;

namespace EXAMEN_DOTNET_2023.Models;

public partial class OrderSubtotal
{
    public int OrderId { get; set; }

    public decimal? Subtotal { get; set; }
}
