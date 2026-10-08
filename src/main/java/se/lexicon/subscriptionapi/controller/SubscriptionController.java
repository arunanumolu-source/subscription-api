package se.lexicon.subscriptionapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.lexicon.subscriptionapi.dto.ChangePlanRequest;
import se.lexicon.subscriptionapi.dto.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.SubscriptionResponse;
import se.lexicon.subscriptionapi.service.CurrentUserService;
import se.lexicon.subscriptionapi.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getMySubscriptions() {

        Long customerId = currentUserService.getCurrentCustomerId();

        return ResponseEntity.ok(
                subscriptionService.getCustomerSubscriptions(customerId)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<SubscriptionResponse>> getMyActiveSubscriptions() {

        Long customerId = currentUserService.getCurrentCustomerId();

        return ResponseEntity.ok(
                subscriptionService.getActiveSubscriptions(customerId)
        );
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody SubscriptionRequest request) {

        Long customerId = currentUserService.getCurrentCustomerId();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        subscriptionService.createSubscription(
                                customerId,
                                request
                        )
                );
    }

    @PatchMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(
            @PathVariable Long subscriptionId) {

        Long customerId = currentUserService.getCurrentCustomerId();

        return ResponseEntity.ok(
                subscriptionService.cancelSubscription(
                        customerId,
                        subscriptionId
                )
        );
    }

    @PatchMapping("/{subscriptionId}/change-plan")
    public ResponseEntity<SubscriptionResponse> changePlan(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody ChangePlanRequest request) {

        Long customerId = currentUserService.getCurrentCustomerId();

        return ResponseEntity.ok(
                subscriptionService.changePlan(
                        customerId,
                        subscriptionId,
                        request
                )
        );
    }
}