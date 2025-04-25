package be.vinci.ipl.users.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserWithCredentials {
    private String pseudo;
    private String firstname;
    private String lastname;
    private String password;

    public User toUser() {
        return new User(pseudo, firstname, lastname);
    }
    public Credentials toCredentials() {
        return new Credentials(pseudo, password);
    }

    public boolean invalid() {
        return pseudo == null || pseudo.isBlank() ||
                firstname == null || firstname.isBlank() ||
                lastname == null || lastname.isBlank() ||
                password == null || password.isBlank();
    }
}
