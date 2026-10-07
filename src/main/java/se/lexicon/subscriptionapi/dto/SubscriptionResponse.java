package se.lexicon.subscriptionapi.dto;

import se.lexicon.subscriptionapi.enums.ServiceType;
import se.lexicon.subscriptionapi.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionResponse(

        Long id,
        Long planId,
        String planName,
        BigDecimal price,
        ServiceType serviceType,
        Long operatorId,
        String operatorName,
        SubscriptionStatus status,
        LocalDateTime startDate,
        LocalDateTime cancellationDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}