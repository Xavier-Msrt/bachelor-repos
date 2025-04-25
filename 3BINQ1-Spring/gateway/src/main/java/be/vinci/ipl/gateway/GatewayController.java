package be.vinci.ipl.gateway;


import be.vinci.ipl.gateway.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class GatewayController {
    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }


    @PostMapping("/authentication/connect")
    public String connect(@RequestBody UnsafeCredentials credentials) {
        return service.connect(credentials);
    }


// Carts

    @PostMapping("/carts/users/{pseudo}/products/{productId}")
    void addItem(@PathVariable String pseudo, @PathVariable int productId, @RequestHeader("Authorization") String token){
        service.verify(token, pseudo);
        service.addItem(pseudo, productId);
    }


    @DeleteMapping("/carts/users/{pseudo}/products/{productId}")
    void removeItem(@PathVariable String pseudo, @PathVariable int productId, @RequestHeader("Authorization") String token){
        service.verify(token, pseudo);
        service.removeItem(pseudo, productId);
    }


    @GetMapping("/carts/users/{pseudo}")
    Iterable<Product> getAllItems(@PathVariable String pseudo, @RequestHeader("Authorization") String token){
        service.verify(token, pseudo);
        return service.getAllItems(pseudo);
    }


   @DeleteMapping("/carts/users/{pseudo}")
    void removeItemByAuthor(@PathVariable String pseudo){
        service.removeItemByAuthor(pseudo);
   }

    @DeleteMapping("/carts/products/{productId}")
    void removeItemByProduct(@PathVariable int productId){
        service.removeItemByProduct(productId);
    }



// products
     @GetMapping("/product")
    Iterable<Product> getAllProduct(){
        return service.getAllProduct();
     }

    @GetMapping("/product/{id}")
    Product getOneProduct(@PathVariable int id){
        return service.getOneProduct(id);
    }

    @PostMapping("/product")
    ResponseEntity<Void> createOne(@RequestBody Product product){
        service.createOne(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/product")
    void removeAll(){
        service.removeAll();
    }

    @DeleteMapping("/product/{id}")
    void deleteOne(@PathVariable int id){
        service.deleteOne(id);
    }

    @PutMapping("/product/{id}")
    void updateOne(@PathVariable int id, @RequestBody Product product){
        service.updateOne(id, product);
    }

// Users
    @PostMapping("/users/{pseudo}")
    ResponseEntity<Void> createOne(@PathVariable String pseudo, @RequestBody UserWithCredentials user){
        service.createOne(pseudo, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/users/{pseudo}")
    User readOne(@PathVariable String pseudo){
        return service.readOne(pseudo);
    }

    @PutMapping("/users/{pseudo}")
    void updateOne(@PathVariable String pseudo, @RequestBody UserWithCredentials user, @RequestHeader("Authorization") String token){
        service.verify(token, pseudo);
        service.updateOne(pseudo, user);
    }

    @DeleteMapping("/users/{pseudo}")
    void deleteOne(@PathVariable String pseudo, @RequestHeader("Authorization") String token){
        service.verify(token, pseudo);
        service.deleteOne(pseudo);
    }


}
