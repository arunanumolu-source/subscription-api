package se.lexicon.subscriptionapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import se.lexicon.subscriptionapi.enums.ServiceType;

import java.math.BigDecimal;

public record PlanRequest(

        @NotBlank(message = "Plan name is required")
        @Size(max = 100, message = "Plan name must not exceed 100 characters")
        String name,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Service type is required")
        ServiceType serviceType,

        @Positive(message = "Data limit must be greater than 0")
        Integer dataLimitGb,

        @NotNull(message = "Active status is required")
        Boolean active,

        @NotNull(message = "Operator ID is required")
        Long operatorId

) {
}