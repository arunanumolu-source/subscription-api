package se.lexicon.subscriptionapi.dto;

import se.lexicon.subscriptionapi.enums.Role;

public record AuthResponse(

        String token,
        String tokenType,
        Long customerId,
        String email,
        Role role

) {
}