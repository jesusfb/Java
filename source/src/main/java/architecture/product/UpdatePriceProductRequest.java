package architecture.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdatePriceProductRequest(
    @NotNull UUID id,
    @Min(0) BigDecimal price) {
}