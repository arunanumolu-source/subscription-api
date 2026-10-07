package se.lexicon.subscriptionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.subscriptionapi.entity.Subscription;
import se.lexicon.subscriptionapi.enums.ServiceType;
import se.lexicon.subscriptionapi.enums.SubscriptionStatus;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByCustomerId(Long customerId);

    List<Subscription> findByCustomerIdAndStatus(
            Long customerId,
            SubscriptionStatus status
    );

    boolean existsByCustomerIdAndPlanServiceTypeAndStatus(
            Long customerId,
            ServiceType serviceType,
            SubscriptionStatus status
    );
}