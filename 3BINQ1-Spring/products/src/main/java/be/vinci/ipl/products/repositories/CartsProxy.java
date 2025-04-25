package be.vinci.ipl.products.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "carts")
public interface CartsProxy {
  @GetMapping("/carts/products/{productId}")
   void deleteAllByProductId(@PathVariable int productId);
}
