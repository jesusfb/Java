package architecture;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import architecture.shared.DatabaseService;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    OpenAPI openAPI() {
        final var securityScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
        
        final var components = new Components().addSecuritySchemes("JWT", securityScheme);
        
        return new OpenAPI().components(components).addSecurityItem(new SecurityRequirement().addList("JWT"));
    }
    
    @Bean
    CommandLineRunner commandLineRunner(DatabaseService databaseService) {
        return args -> databaseService.migrate();
    }
}