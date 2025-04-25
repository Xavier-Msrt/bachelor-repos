package be.vinci.ipl.carts;

import be.vinci.ipl.carts.models.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CartController {

    private final CartsService service;

    public CartController(CartsService service) {
        this.service = service;
    }


    @PostMapping("/carts/users/{pseudo}/products/{productId}")
    public void addItem(@PathVariable String pseudo, @PathVariable int productId){
        if(pseudo == null || pseudo.isEmpty() || productId < 1 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        service.addCartItem(pseudo, productId);
    }


    @DeleteMapping("/carts/users/{pseudo}/products/{productId}")
    public void removeItem(@PathVariable String pseudo, @PathVariable int productId){
        if(!service.deleteCartItem(pseudo, productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/carts/users/{pseudo}")
    public Iterable<Product> getAllItems(@PathVariable String pseudo){
        return service.getAllItemByAuthor(pseudo);
    }


    @DeleteMapping("/carts/users/{pseudo}")
    public void removeItemByAuthor(@PathVariable String pseudo){
        service.deleteAllCarItemByAuthor(pseudo);
    }

    @DeleteMapping("/carts/products/{productId}")
    public void removeItemByProduct(@PathVariable int productId){
        service.deleteAllCarItemByProduct(productId);
    }

}
