package ZapGo.App.ZapGo.services;

import ZapGo.App.ZapGo.entities.ConfirmationToken;
import ZapGo.App.ZapGo.repositories.ConfirmationTokenRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MongoTemplate mongoTemplate;

    public void updateConfirmedAt(String token, LocalDateTime confirmedAt) {
        Query query = new Query(Criteria.where("token").is(token));
        Update update = new Update().set("confirmedAt", confirmedAt);
        UpdateResult result = mongoTemplate.updateFirst(query, update, ConfirmationToken.class);
        result.getModifiedCount();
    }

    public void saveConfirmationToken(ConfirmationToken token) {

        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> findToken(String token) {

        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {

        updateConfirmedAt(token, LocalDateTime.now());
    }
}