package be.vinci.ipl.products.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "product")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column( nullable = false)
    private String name;

    @Column( nullable = false)
    private String category;

    @Column( nullable = false)
    private float price;

    public boolean invalid(){
        return name == null || name.isBlank()
                || category == null || category.isBlank()
                || price <= 0;
    }
}
