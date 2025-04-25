package be.vinci.ipl.gateway.repositories;

import be.vinci.ipl.gateway.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Repository
@FeignClient(name = "carts")
public interface CartProxy {

    @PostMapping("/carts/users/{pseudo}/products/{productId}")
    void addItem(@PathVariable String pseudo, @PathVariable int productId);


    @DeleteMapping("/carts/users/{pseudo}/products/{productId}")
    void removeItem(@PathVariable String pseudo, @PathVariable int productId);


    @GetMapping("/carts/users/{pseudo}")
    Iterable<Product> getAllItems(@PathVariable String pseudo);


    @DeleteMapping("/carts/users/{pseudo}")
    void removeItemByAuthor(@PathVariable String pseudo);

    @DeleteMapping("/carts/products/{productId}")
    void removeItemByProduct(@PathVariable int productId);


}
