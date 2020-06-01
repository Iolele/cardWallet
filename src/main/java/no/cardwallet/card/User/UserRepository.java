package no.cardwallet.card.User;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

    void deleteUserByEmail(String email);

    User findByLoginToken(String loginToken);
}
