package se.lexicon.subscriptionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.subscriptionapi.entity.Plan;
import se.lexicon.subscriptionapi.enums.ServiceType;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByActiveTrue();

    List<Plan> findByOperatorId(Long operatorId);

    List<Plan> findByOperatorIdAndActiveTrue(Long operatorId);

    List<Plan> findByServiceTypeAndActiveTrue(ServiceType serviceType);

    boolean existsByOperatorIdAndNameIgnoreCase(Long operatorId, String name);
}