package be.vinci.ipl.gateway.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @Column(nullable = false)
    private String pseudo;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;
}