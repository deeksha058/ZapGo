package ZapGo.App.ZapGo.repositories;

import ZapGo.App.ZapGo.entities.ConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

}