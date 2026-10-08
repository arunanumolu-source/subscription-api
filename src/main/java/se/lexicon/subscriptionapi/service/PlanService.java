package se.lexicon.subscriptionapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.subscriptionapi.dto.PlanRequest;
import se.lexicon.subscriptionapi.dto.PlanResponse;
import se.lexicon.subscriptionapi.entity.Operator;
import se.lexicon.subscriptionapi.entity.Plan;
import se.lexicon.subscriptionapi.exception.BusinessRuleException;
import se.lexicon.subscriptionapi.exception.DuplicateResourceException;
import se.lexicon.subscriptionapi.exception.ResourceNotFoundException;
import se.lexicon.subscriptionapi.mapper.PlanMapper;
import se.lexicon.subscriptionapi.repository.OperatorRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;
import se.lexicon.subscriptionapi.repository.SubscriptionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {

    private final PlanRepository planRepository;
    private final OperatorRepository operatorRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PlanMapper planMapper;

    @Transactional(readOnly = true)
    public List<PlanResponse> getAllPlans() {
        return planRepository.findAll()
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlanResponse> getActivePlans() {
        return planRepository.findByActiveTrue()
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlanResponse getPlanById(Long id) {
        Plan plan = findPlanById(id);
        return planMapper.toResponse(plan);
    }

    public PlanResponse createPlan(PlanRequest request) {

        Operator operator = findOperatorById(request.operatorId());

        if (planRepository.existsByOperatorIdAndNameIgnoreCase(
                operator.getId(),
                request.name()
        )) {
            throw new DuplicateResourceException(
                    "Plan with name '" + request.name() +
                            "' already exists for this operator"
            );
        }

        Plan plan = planMapper.toEntity(request);
        plan.setOperator(operator);

        Plan savedPlan = planRepository.save(plan);

        return planMapper.toResponse(savedPlan);
    }

    public PlanResponse updatePlan(
            Long id,
            PlanRequest request) {

        Plan plan = findPlanById(id);
        Operator operator = findOperatorById(request.operatorId());

        planRepository.findByOperatorId(operator.getId())
                .stream()
                .filter(existing ->
                        existing.getName().equalsIgnoreCase(request.name())
                                && !existing.getId().equals(id)
                )
                .findFirst()
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Plan with name '" + request.name() +
                                    "' already exists for this operator"
                    );
                });

        planMapper.updateEntity(request, plan);
        plan.setOperator(operator);

        Plan updatedPlan = planRepository.save(plan);

        return planMapper.toResponse(updatedPlan);
    }

    public void deletePlan(Long id) {

        Plan plan = findPlanById(id);

        boolean hasSubscriptions = subscriptionRepository.findAll()
                .stream()
                .anyMatch(subscription ->
                        subscription.getPlan().getId().equals(id)
                );

        if (hasSubscriptions) {
            throw new BusinessRuleException(
                    "Plan cannot be deleted because it has subscriptions"
            );
        }

        planRepository.delete(plan);
    }

    private Plan findPlanById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Plan not found with id: " + id
                        )
                );
    }

    private Operator findOperatorById(Long id) {
        return operatorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Operator not found with id: " + id
                        )
                );
    }
}