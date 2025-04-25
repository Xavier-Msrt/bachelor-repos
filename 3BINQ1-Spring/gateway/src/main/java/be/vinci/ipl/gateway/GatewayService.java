package be.vinci.ipl.gateway;

import be.vinci.ipl.gateway.exceptions.*;
import be.vinci.ipl.gateway.models.*;
import be.vinci.ipl.gateway.repositories.*;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {
    public final AuthenticationProxy authenticationProxy;
    public final CartProxy cartProxy;
    public final ProductProxy productProxy;
    public final UserProxy userProxy;

    public GatewayService(AuthenticationProxy authenticationProxy, CartProxy cartProxy, ProductProxy productProxy, UserProxy userProxy) {
        this.authenticationProxy = authenticationProxy;
        this.cartProxy = cartProxy;
        this.productProxy = productProxy;
        this.userProxy = userProxy;
    }

    //Authentication
    public String connect(UnsafeCredentials unsafeCredentials) throws BadRequestException {
        try {
            return authenticationProxy.connect(unsafeCredentials);
        }catch (FeignException e){
            if(e.status() == 400) throw new BadRequestException();
            else if (e.status() == 401) throw new UnauthorizedException();
            else throw e;
        }
    }

    public void verify(String token, String expectedPseudo) throws BadRequestException, ForbiddenException {
        try {
            String userPseudo = authenticationProxy.verify(token);
            if(!userPseudo.equals(expectedPseudo))throw new ForbiddenException();
        }catch (FeignException e){
            if(e.status() == 401) throw new UnauthorizedException();
            else throw e;
        }
    }



    //Carts
    public void addItem(String pseudo, int productId) throws BadRequestException, ForbiddenException {
        try {
            cartProxy.addItem(pseudo, productId);
        }catch (FeignException e){
            if(e.status() == 400) throw new BadRequestException();
            else if(e.status() == 409) throw new ConflictException();
            else throw e;
        }
    }

    public void removeItem(String pseudo, int productId) throws BadRequestException {
        try {
            cartProxy.removeItem(pseudo, productId);
        }catch (FeignException e){
            if(e.status() == 400) throw new BadRequestException();
            else throw e;
        }
    }

    public Iterable<Product> getAllItems(String pseudo) {
        return cartProxy.getAllItems(pseudo);
    }

    public void removeItemByAuthor(String pseudo) throws BadRequestException {
        try {
            cartProxy.removeItemByAuthor(pseudo);
        }catch (FeignException e){
            if(e.status() == 404) throw new BadRequestException();
            else throw e;
        }
    }

    public void removeItemByProduct(int productId) throws BadRequestException {
        try {
            cartProxy.removeItemByProduct(productId);
        }catch (FeignException e){
            if(e.status() == 404) throw new BadRequestException();
            else throw e;
        }
    }



    //Products
    public Iterable<Product> getAllProduct() {
        return productProxy.getAllProduct();
    }

    public Product getOneProduct(int id) {
        return productProxy.getOneProduct(id);
    }

    public void createOne(Product product) {
        try {
            productProxy.createOne(product);
        }catch (FeignException e){
            if(e.status() == 400) throw new BadRequestException();
            else throw e;
        }
    }

    public void removeAll() {
        productProxy.removeAll();
    }

    public void deleteOne(int id) {
        try {
            productProxy.deleteOne(id);
        }catch (FeignException e){
            if(e.status() == 404) throw new BadRequestException();
            else throw e;
        }
    }

    public void updateOne(int productId, Product product) {
        try {
            productProxy.updateOne(productId, product);
        }catch (FeignException e){
            if(e.status() == 404) throw new BadRequestException();
            else if (e.status() == 400) throw new BadRequestException();
            else throw e;
        }
    }


    //Users
    public void createOne(String pseudo, UserWithCredentials user) {
        try {
            userProxy.createOne(pseudo, user);
        }catch (FeignException e){
            if(e.status() == 400) throw new BadRequestException();
            else if (e.status() == 404) throw new BadRequestException();
            else if(e.status() == 409) throw new ConflictException();
            else throw e;
        }
    }

    public User readOne(String pseudo) {
        try {
            return userProxy.readOne(pseudo);
        }catch (FeignException e){
            if(e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    public void updateOne(String pseudo, UserWithCredentials user) {
        try {
            userProxy.updateOne(pseudo, user);
        }catch (FeignException e){
            if (e.status() == 400) throw new BadRequestException();
            else if(e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

    public void deleteOne(String pseudo) {
        try {
            userProxy.deleteOne(pseudo);
        }catch (FeignException e){
            if(e.status() == 404) throw new NotFoundException();
            else throw e;
        }
    }

}
