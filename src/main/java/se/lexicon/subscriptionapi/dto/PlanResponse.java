package se.lexicon.subscriptionapi.dto;

import se.lexicon.subscriptionapi.enums.ServiceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PlanResponse(

        Long id,
        String name,
        BigDecimal price,
        ServiceType serviceType,
        Integer dataLimitGb,
        boolean active,
        Long operatorId,
        String operatorName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}