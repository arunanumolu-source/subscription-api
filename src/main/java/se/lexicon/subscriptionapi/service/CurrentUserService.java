package se.lexicon.subscriptionapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.lexicon.subscriptionapi.entity.Customer;
import se.lexicon.subscriptionapi.exception.ResourceNotFoundException;
import se.lexicon.subscriptionapi.exception.UnauthorizedException;
import se.lexicon.subscriptionapi.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final CustomerRepository customerRepository;

    public Long getCurrentCustomerId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            throw new UnauthorizedException(
                    "Authentication is required"
            );
        }

        String email = authentication.getName();

        Customer customer = customerRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated customer not found"
                        )
                );

        return customer.getId();
    }
}