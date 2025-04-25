package be.vinci.ipl.carts.repositories;

import be.vinci.ipl.carts.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient( name = "users")
public interface UserProxy {
    @GetMapping("/users/{pseudo}")
    User readOneByPseudo(@PathVariable String pseudo);
}
