using System;
using System.Collections.Generic;

namespace EXAMEN_DOTNET_2023.Models;

public partial class Category
{
    public int CategoryId { get; set; }

    public string CategoryName { get; set; } = null!;

    public string? Description { get; set; }

    public byte[]? Picture { get; set; }
}
