package be.vinci.ipl.products;

import be.vinci.ipl.products.models.Product;
import be.vinci.ipl.products.repositories.CartsProxy;
import be.vinci.ipl.products.repositories.ProductRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CartsProxy cartsProxy;

    public ProductService(ProductRepository repository, CartsProxy cartsProxy) {
        this.repository = repository;
      this.cartsProxy = cartsProxy;
    }

    public Iterable<Product> getAll(){
        return repository.findAll();
    }

    public Optional<Product> getOneById(int id){
        return repository.findById(id);
    }

    public void createOne(Product product){
        repository.save(product);
    }

    public void removeAll(){
       Iterable<Product> products =  repository.findAll();
       for( Product p : products){
           try {
               cartsProxy.deleteAllByProductId(p.getId());
           }catch (FeignException.FeignClientException ignored){}
       }
       repository.deleteAll();
    }

    public boolean removeOne(int id){
        if(!repository.existsById(id)){
            return false;
        }
        repository.deleteById(id);
        try {
            cartsProxy.deleteAllByProductId(id);
        }catch (FeignException.FeignClientException ignored){}

        return true;
    }

    public boolean updateOne(Product product){
        if(!repository.existsById(product.getId())){
            return false;
        }
        repository.save(product);
        return true;
    }
}
