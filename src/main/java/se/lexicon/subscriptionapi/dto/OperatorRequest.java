package se.lexicon.subscriptionapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OperatorRequest(

        @NotBlank(message = "Operator name is required")
        @Size(max = 100, message = "Operator name must not exceed 100 characters")
        String name,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description

) {
}
