package se.lexicon.subscriptionapi.dto;

import jakarta.validation.constraints.NotNull;

public record SubscriptionRequest(

        @NotNull(message = "Plan ID is required")
        Long planId

) {
}