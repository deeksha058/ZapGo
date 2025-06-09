package ZapGo.App.ZapGo.services;

import ZapGo.App.ZapGo.entities.User;
import ZapGo.App.ZapGo.models.LoginRequest;
import ZapGo.App.ZapGo.repositories.ConfirmationTokenRepository;
import ZapGo.App.ZapGo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public String loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Email not verified");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        return "Login successful"; // Yahan JWT return kar sakte ho agar use kar rahe ho
    }
}
