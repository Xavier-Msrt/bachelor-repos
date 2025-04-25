package be.vinci.ipl.carts.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name= "cart")
public class CartItem {
    @Id
    @Column(nullable = false)
    private String id;


    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private int product;

}
