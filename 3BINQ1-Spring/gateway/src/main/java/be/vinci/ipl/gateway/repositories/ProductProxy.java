package be.vinci.ipl.gateway.repositories;

import be.vinci.ipl.gateway.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;


@Repository
@FeignClient(name = "products")
public interface ProductProxy {

    @GetMapping("/product")
    Iterable<Product> getAllProduct();

    @GetMapping("/product/{id}")
    Product getOneProduct(@PathVariable int id);

    @PostMapping("/product")
    ResponseEntity<Void> createOne(@RequestBody Product product);

    @DeleteMapping("/product")
    void removeAll();

    @DeleteMapping("/product/{id}")
    void deleteOne(@PathVariable int id);

    @PutMapping("/product/{id}")
    void updateOne(@PathVariable int id, @RequestBody Product product);

}
