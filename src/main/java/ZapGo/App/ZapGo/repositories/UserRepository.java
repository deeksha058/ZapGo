package ZapGo.App.ZapGo.repositories;

import ZapGo.App.ZapGo.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends MongoRepository<User, Long> {

    Optional<User> findByEmail(String email);

}