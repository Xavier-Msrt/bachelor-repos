package be.vinci.ipl.users.repositories;

import be.vinci.ipl.users.models.Credentials;
import be.vinci.ipl.users.models.UnsafeCredentials;
import be.vinci.ipl.users.models.UserWithCredentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient( name = "authentications")
public interface AuthenticationProxy {
    @PostMapping("/authentication/{pseudo}")
    ResponseEntity<Void> createOne(@PathVariable String pseudo, @RequestBody Credentials credentials);

    @PutMapping("/authentication/{pseudo}")
    void updateOne(@PathVariable String pseudo, @RequestBody Credentials credentials);

    @DeleteMapping("/authentication/{pseudo}")
    void deleteCredentials(@PathVariable String pseudo);
}
