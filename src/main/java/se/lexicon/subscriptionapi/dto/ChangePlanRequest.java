package se.lexicon.subscriptionapi.dto;

import jakarta.validation.constraints.NotNull;

public record ChangePlanRequest(

        @NotNull(message = "New plan ID is required")
        Long newPlanId

) {
}