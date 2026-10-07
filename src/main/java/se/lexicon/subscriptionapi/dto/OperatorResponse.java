package se.lexicon.subscriptionapi.dto;

import java.time.LocalDateTime;

public record OperatorResponse(

        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}