package ZapGo.App.ZapGo.services;

import ZapGo.App.ZapGo.entities.ConfirmationToken;
import ZapGo.App.ZapGo.entities.User;
import ZapGo.App.ZapGo.repositories.UserRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s is not found.";

    private final UserRepository userRepository;

    private final ConfirmationTokenService confirmationTokenService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MongoTemplate mongoTemplate;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email).orElseThrow(
            () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email))
        );
    }

    public String signUpUser(User user) {

        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if (userExists) {

            // TODO: if email is not confirmed send confirmation email again.

            throw new IllegalStateException("email is already taken.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
            token, LocalDateTime.now(), LocalDateTime.now().plusDays(1), user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }


    public void enableUser(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        Update update = new Update().set("enabled", true);
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        result.getModifiedCount();
    }
}