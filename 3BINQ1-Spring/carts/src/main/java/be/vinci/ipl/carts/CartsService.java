package be.vinci.ipl.carts;

import be.vinci.ipl.carts.models.*;
import be.vinci.ipl.carts.repositories.*;
import feign.FeignException;
import java.util.Objects;
import java.util.stream.StreamSupport;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CartsService {

    private final CartsRepository cartsRepository;
    private final ProductsProxy productsProxy;
    private final UserProxy userProxy;

    public CartsService(CartsRepository cartsRepository, ProductsProxy productsProxy, UserProxy userProxy) {
        this.cartsRepository = cartsRepository;
        this.productsProxy = productsProxy;
        this.userProxy = userProxy;
    }


    public void addCartItem(String pseudo, int productId){

        // Product and User exist
        Product product;
        User user;
        try {
             product = productsProxy.readOne(productId);
             user = userProxy.readOneByPseudo(pseudo);
        }catch (FeignException.FeignClientException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String cartId = user.getPseudo()+product.getId();

        // CartIteam already exist
        Optional<CartItem> cartItemFound = cartsRepository.findById(cartId);
        if(cartItemFound.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }


        CartItem cartItem = new CartItem();
        cartItem.setId(cartId);
        cartItem.setAuthor(user.getPseudo());
        cartItem.setProduct(productId);
        cartsRepository.save(cartItem);
    }


    public boolean deleteCartItem(String pseudo, int productId){
        String cartId = pseudo+productId;
        if(!cartsRepository.existsById(cartId)){
            return false;
        }
        cartsRepository.deleteById(cartId);
        return true;
    }


    public Iterable<Product> getAllItemByAuthor(String pseudo){
        Iterable<CartItem> cartItems = cartsRepository.findCartItemsByAuthor(pseudo);
        return StreamSupport.stream(cartItems.spliterator(), false)
            .map(item -> {
                try {
                    int productId = item.getProduct();
                    return productsProxy.readOne(productId);
                }catch (FeignException.FeignClientException e){
                    return null;
                }
            })
            .filter(Objects::nonNull) // si erreur plus haut
            .toList();
    }

    public void deleteAllCarItemByAuthor(String pseudo){
        if (userProxy.readOneByPseudo(pseudo) == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        cartsRepository.deleteCartItemsByAuthor(pseudo);
    }


    public void deleteAllCarItemByProduct(int productId){
        if (productsProxy.readOne(productId) == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        cartsRepository.deleteCartItemsByProduct(productId);
    }

}
