package ZapGo.App.ZapGo.services;

import ZapGo.App.ZapGo.entities.ConfirmationToken;
import ZapGo.App.ZapGo.entities.User;
import ZapGo.App.ZapGo.entities.enums.UserRole;
import ZapGo.App.ZapGo.models.RegistrationRequest;
import ZapGo.App.ZapGo.services.email.EmailSender;
import ZapGo.App.ZapGo.services.email.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailValidator emailValidator;

    private final EmailSender emailSender;

    public String register(RegistrationRequest registrationRequest) {

        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());

        if (!isValidEmail) {

            throw new IllegalStateException("email address is not valid.");
        }

        String token = userService.signUpUser(
            new User(
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                registrationRequest.getPassword(),
                UserRole.USER
            )
        );

        String link = "http://localhost:8080/api/v1/public/registration/confirm?token=" + token;

        String fullName = registrationRequest.getFirstName() + " " + registrationRequest.getLastName();

        emailSender.send(registrationRequest.getEmail(), emailSender.buildEmail(fullName, link));

        return link;
    }

    @Transactional
    public String confirmToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenService.findToken(token).orElseThrow(
            () -> new IllegalStateException("token is not found.")
        );

        if (confirmationToken.getConfirmedAt() != null) {

            throw new IllegalStateException("email is already confirmed.");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {

            throw new IllegalStateException("token is expired.");
        }

        confirmationTokenService.setConfirmedAt(token);

        userService.enableUser(confirmationToken.getUser().getEmail());

        return "confirmed";
    }

}