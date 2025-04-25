package be.vinci.ipl.carts.repositories;

import be.vinci.ipl.carts.models.CartItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface CartsRepository extends CrudRepository<CartItem, String> {

  Iterable<CartItem> findCartItemsByAuthor(String author);

  @Transactional
  void deleteCartItemsByAuthor(String author);


  @Transactional
  void deleteCartItemsByProduct(int product);
}
