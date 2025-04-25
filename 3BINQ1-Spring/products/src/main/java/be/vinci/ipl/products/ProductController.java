package be.vinci.ipl.products;

import be.vinci.ipl.products.models.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@RestController
public class ProductController {

    private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }


    @GetMapping("/product")
    public Iterable<Product> getAllProduct() {
        return service.getAll();
    }

    @GetMapping("/product/{id}")
    public Product getOneProduct(@PathVariable int id){
        Optional<Product> product = service.getOneById(id);
        if(product.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return product.orElse(null);
    }

    @PostMapping("/product")
    public ResponseEntity<Void> createOne( @RequestBody Product product){
        if(product.invalid()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        service.createOne(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/product")
    public void removeAll(){
        service.removeAll();
    }

    @DeleteMapping("/product/{id}")
    public void deleteOne(@PathVariable int id){
        if(!service.removeOne(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/product/{id}")
    public void updateOne(@PathVariable int id, @RequestBody Product product){
        if(!Objects.equals(product.getId(), id) || product.invalid()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(!service.updateOne(product)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
