package be.vinci.ipl.users.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient( name = "carts")
public interface CartsProxy {

  @DeleteMapping("/carts/users/{pseudo}")
  void deleteAllItemOfUser( @PathVariable String pseudo);
}
