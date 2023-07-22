package architecture.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductRequest(
    @NotNull UUID id,
    @NotBlank String description,
    @Min(0) BigDecimal price) {
}
