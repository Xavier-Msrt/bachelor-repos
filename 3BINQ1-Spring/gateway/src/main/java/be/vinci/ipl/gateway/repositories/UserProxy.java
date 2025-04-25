package be.vinci.ipl.gateway.repositories;

import be.vinci.ipl.gateway.models.User;
import be.vinci.ipl.gateway.models.UserWithCredentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;


@Repository
@FeignClient(name = "users")
public interface UserProxy {

    @PostMapping("/users/{pseudo}")
    ResponseEntity<Void> createOne(@PathVariable String pseudo, @RequestBody UserWithCredentials user);

    @GetMapping("/users/{pseudo}")
    User readOne(@PathVariable String pseudo);

    @PutMapping("/users/{pseudo}")
    void updateOne(@PathVariable String pseudo, @RequestBody UserWithCredentials user);

    @DeleteMapping("/users/{pseudo}")
    void deleteOne(@PathVariable String pseudo);

}
