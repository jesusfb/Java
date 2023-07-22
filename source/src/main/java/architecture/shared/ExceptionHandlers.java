package architecture.shared;

import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public final class ExceptionHandlers {
    private final MessageService messageService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageService.get("error"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleException(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageService.get("unauthorized"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleException(MethodArgumentNotValidException exception) {
        Function<FieldError, String> map = error -> Character.toUpperCase(error.getField().charAt(0)) + error.getField().substring(1) + " " + error.getDefaultMessage() + ".";
        
        final var message = exception.getBindingResult().getFieldErrors().stream().map(map).collect(Collectors.joining(" "));
        
        return ResponseEntity.badRequest().body(message);
    }
}