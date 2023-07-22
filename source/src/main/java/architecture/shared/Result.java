package architecture.shared;

import java.util.Optional;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Getter
public final class Result<T> {
    private final Optional<T> value;
    private final Optional<String> error;
    
    private Result(T value, String error) {
        this.value = Optional.ofNullable(value);
        this.error = Optional.ofNullable(error);
    }
    
    public static <T> Result<T> error(String error) {
        return new Result<>(null, error);
    }

    public static <T> Result<T> success() {
        return new Result<>(null, null);
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public ResponseEntity<Object> response() {
        if (error.isPresent()) return ResponseEntity.badRequest().body(error.get());
        
        if (value.isPresent()) return ResponseEntity.ok(value.get());
        
        final var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        final var status = request.getMethod().equals("GET") ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT;
        
        return ResponseEntity.status(status).build();
    }
}
