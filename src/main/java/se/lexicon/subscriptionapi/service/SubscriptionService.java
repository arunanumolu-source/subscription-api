package se.lexicon.subscriptionapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.subscriptionapi.dto.ChangePlanRequest;
import se.lexicon.subscriptionapi.dto.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.SubscriptionResponse;
import se.lexicon.subscriptionapi.entity.Customer;
import se.lexicon.subscriptionapi.entity.Plan;
import se.lexicon.subscriptionapi.entity.Subscription;
import se.lexicon.subscriptionapi.enums.SubscriptionStatus;
import se.lexicon.subscriptionapi.exception.BusinessRuleException;
import se.lexicon.subscriptionapi.exception.ResourceNotFoundException;
import se.lexicon.subscriptionapi.mapper.SubscriptionMapper;
import se.lexicon.subscriptionapi.repository.CustomerRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;
import se.lexicon.subscriptionapi.repository.SubscriptionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getCustomerSubscriptions(Long customerId) {

        findCustomerById(customerId);

        return subscriptionRepository.findByCustomerId(customerId)
                .stream()
                .map(subscriptionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getActiveSubscriptions(Long customerId) {

        findCustomerById(customerId);

        return subscriptionRepository
                .findByCustomerIdAndStatus(
                        customerId,
                        SubscriptionStatus.ACTIVE
                )
                .stream()
                .map(subscriptionMapper::toResponse)
                .toList();
    }

    public SubscriptionResponse createSubscription(
            Long customerId,
            SubscriptionRequest request) {

        Customer customer = findCustomerById(customerId);
        Plan plan = findPlanById(request.planId());

        if (!plan.isActive()) {
            throw new BusinessRuleException(
                    "Cannot subscribe to an inactive plan"
            );
        }

        boolean alreadyHasActiveSubscription =
                subscriptionRepository
                        .existsByCustomerIdAndPlanServiceTypeAndStatus(
                                customerId,
                                plan.getServiceType(),
                                SubscriptionStatus.ACTIVE
                        );

        if (alreadyHasActiveSubscription) {
            throw new BusinessRuleException(
                    "Customer already has an active subscription for service type: "
                            + plan.getServiceType()
            );
        }

        Subscription subscription = Subscription.builder()
                .customer(customer)
                .plan(plan)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(LocalDateTime.now())
                .build();

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        return subscriptionMapper.toResponse(savedSubscription);
    }

    public SubscriptionResponse cancelSubscription(
            Long customerId,
            Long subscriptionId) {

        Subscription subscription =
                findCustomerSubscription(customerId, subscriptionId);

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new BusinessRuleException(
                    "Subscription is already cancelled"
            );
        }

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancellationDate(LocalDateTime.now());

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        return subscriptionMapper.toResponse(savedSubscription);
    }

    public SubscriptionResponse changePlan(
            Long customerId,
            Long subscriptionId,
            ChangePlanRequest request) {

        Subscription subscription =
                findCustomerSubscription(customerId, subscriptionId);

        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleException(
                    "Only active subscriptions can change plans"
            );
        }

        Plan currentPlan = subscription.getPlan();
        Plan newPlan = findPlanById(request.newPlanId());

        if (!newPlan.isActive()) {
            throw new BusinessRuleException(
                    "Cannot change to an inactive plan"
            );
        }

        if (!currentPlan.getOperator()
                .getId()
                .equals(newPlan.getOperator().getId())) {

            throw new BusinessRuleException(
                    "New plan must belong to the same operator"
            );
        }

        if (currentPlan.getServiceType()
                != newPlan.getServiceType()) {

            throw new BusinessRuleException(
                    "New plan must have the same service type"
            );
        }

        subscription.setPlan(newPlan);

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        return subscriptionMapper.toResponse(savedSubscription);
    }

    private Subscription findCustomerSubscription(
            Long customerId,
            Long subscriptionId) {

        Subscription subscription = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription not found with id: "
                                        + subscriptionId
                        )
                );

        if (!subscription.getCustomer()
                .getId()
                .equals(customerId)) {

            throw new ResourceNotFoundException(
                    "Subscription not found for this customer"
            );
        }

        return subscription;
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id: " + id
                        )
                );
    }

    private Plan findPlanById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Plan not found with id: " + id
                        )
                );
    }
}