package architecture.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class MessageService {
    private final MessageSource messageSource;

    public String get(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}
