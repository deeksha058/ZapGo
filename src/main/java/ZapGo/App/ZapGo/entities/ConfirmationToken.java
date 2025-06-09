package ZapGo.App.ZapGo.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Data
@Document(collection = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    private String id; // MongoDB ObjectId stored as String

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    // Option 1: Reference full User document (auto-handled by Spring Data)
    @DBRef
    private User user;

    // Constructor
    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
