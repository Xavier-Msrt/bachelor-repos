package be.vinci.ipl.users;

import be.vinci.ipl.users.models.User;
import be.vinci.ipl.users.models.UserWithCredentials;
import be.vinci.ipl.users.repositories.AuthenticationProxy;
import be.vinci.ipl.users.repositories.CartsProxy;
import be.vinci.ipl.users.repositories.UsersRepository;
import feign.FeignException.FeignClientException;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository repository;
    private final CartsProxy cartsProxy;
    private final AuthenticationProxy authenticationProxy;


    public UsersService(UsersRepository repository, CartsProxy cartsProxy, AuthenticationProxy authenticationProxy) {
        this.repository = repository;
        this.cartsProxy = cartsProxy;
        this.authenticationProxy = authenticationProxy;
    }

    /**
     * Creates a new user in repository
     * @param user the information of the user
     * @return true if the user was created, false if another user exists with the same pseudo
     */
    public boolean createOne(UserWithCredentials user) {
        if (repository.existsById(user.getPseudo())) return false;

        authenticationProxy.createOne(user.getPseudo(), user.toCredentials());

        repository.save(user.toUser());
        return true;
    }

    /**
     * Reads a user in repository
     * @param pseudo the pseudo of the user
     * @return the user, or null if the user couldn't be found
     */
    public User readOne(String pseudo) {
        return repository.findById(pseudo).orElse(null);
    }

    /**
     * Updates a user in repository
     * @param user New values of the user
     * @return true if the user was updated, or false if the user couldn't be found
     */
    public boolean updateOne(UserWithCredentials user) {
        if (!repository.existsById(user.getPseudo())) return false;

        authenticationProxy.updateOne(user.getPseudo(), user.toCredentials());

        repository.save(user.toUser());
        return true;
    }

    /**
     * Deletes a user from repository and all reviews associated with it
     * @param pseudo the pseudo of the user
     * @return true if the user was deleted, or false if the user couldn't be found
     */
    public boolean deleteOne(String pseudo) {
        if (!repository.existsById(pseudo)) return false;
        repository.deleteById(pseudo);
        try {
            authenticationProxy.deleteCredentials(pseudo);
            cartsProxy.deleteAllItemOfUser(pseudo);
        }catch (FeignClientException ignored){}
        return true;
    }

}
