package be.vinci.ipl.authentication;

import be.vinci.ipl.authentication.models.UnsafeCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }


    @PostMapping("/authentication/connect")
    public String connect(@RequestBody UnsafeCredentials credentials) {
        if (credentials.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        String token =  service.connect(credentials);
        if (token == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return token;
    }


    @PostMapping("/authentication/verify")
    public String verify(@RequestBody String token) {
        String pseudo = service.verify(token);
        if (pseudo == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return pseudo;
    }


    @PostMapping("/authentication/{pseudo}")
    public ResponseEntity<Void> createOne(@PathVariable String pseudo, @RequestBody UnsafeCredentials credentials) {
        if (!Objects.equals(credentials.getPseudo(), pseudo)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (credentials.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        boolean created = service.createOne(credentials);
        if (!created) throw new ResponseStatusException(HttpStatus.CONFLICT);
        else return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/authentication/{pseudo}")
    public void updateOne(@PathVariable String pseudo, @RequestBody UnsafeCredentials credentials) {
        if (!Objects.equals(credentials.getPseudo(), pseudo)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (credentials.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        boolean found = service.updateOne(credentials);
        if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/authentication/{pseudo}")
    public void deleteCredentials(@PathVariable String pseudo) {
        boolean found = service.deleteOne(pseudo);
        if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
