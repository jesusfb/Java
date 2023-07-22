package architecture.shared;

import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import architecture.user.User;

@Service
@RequiredArgsConstructor
public final class DatabaseService {
    private final Environment environment;
    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    public void migrate() {
        if (Arrays.asList(environment.getActiveProfiles()).contains("test")) return;

        if (!mongoTemplate.exists(new Query(new Criteria("username").is("admin")), User.class))
            mongoTemplate.save(new User(UUID.randomUUID(), "Admin", "admin@mail.com", "admin", passwordEncoder.encode("123456")));
    }
}