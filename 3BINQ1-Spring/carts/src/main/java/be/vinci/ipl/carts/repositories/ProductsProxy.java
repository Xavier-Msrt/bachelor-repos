package be.vinci.ipl.carts.repositories;

import be.vinci.ipl.carts.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "products")
public interface ProductsProxy {
    @GetMapping("/product/{id}")
    Product readOne(@PathVariable int id);
}

