package be.vinci.ipl.gateway.models;

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
}
