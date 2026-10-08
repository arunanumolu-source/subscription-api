package se.lexicon.subscriptionapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.subscriptionapi.dto.AuthResponse;
import se.lexicon.subscriptionapi.dto.LoginRequest;
import se.lexicon.subscriptionapi.dto.RegisterRequest;
import se.lexicon.subscriptionapi.entity.Customer;
import se.lexicon.subscriptionapi.enums.Role;
import se.lexicon.subscriptionapi.exception.DuplicateResourceException;
import se.lexicon.subscriptionapi.exception.UnauthorizedException;
import se.lexicon.subscriptionapi.repository.CustomerRepository;
import se.lexicon.subscriptionapi.security.CustomUserDetailsService;
import se.lexicon.subscriptionapi.security.JwtService;
import se.lexicon.subscriptionapi.security.TokenBlacklistService;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthResponse register(RegisterRequest request) {

        if (customerRepository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateResourceException(
                    "Email is already registered"
            );
        }

        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.CUSTOMER)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(savedCustomer.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                "Bearer",
                savedCustomer.getId(),
                savedCustomer.getEmail(),
                savedCustomer.getRole()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        Customer customer = customerRepository
                .findByEmailIgnoreCase(request.email())
                .orElseThrow(() ->
                        new UnauthorizedException(
                                "Invalid email or password"
                        )
                );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(customer.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                "Bearer",
                customer.getId(),
                customer.getEmail(),
                customer.getRole()
        );
    }

    public void logout(String authorizationHeader) {

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            throw new UnauthorizedException(
                    "Valid Bearer token is required"
            );
        }

        String token = authorizationHeader.substring(7);

        long remainingExpiration =
                jwtService.getRemainingExpiration(token);

        tokenBlacklistService.blacklistToken(
                token,
                remainingExpiration
        );
    }
}