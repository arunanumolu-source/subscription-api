package se.lexicon.subscriptionapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.lexicon.subscriptionapi.entity.Customer;
import se.lexicon.subscriptionapi.repository.CustomerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Customer customer = customerRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + email
                        )
                );

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(
                        "ROLE_" + customer.getRole().name()
                );

        return new User(
                customer.getEmail(),
                customer.getPassword(),
                List.of(authority)
        );
    }
}