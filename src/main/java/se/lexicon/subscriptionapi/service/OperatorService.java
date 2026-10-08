package se.lexicon.subscriptionapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.subscriptionapi.dto.OperatorRequest;
import se.lexicon.subscriptionapi.dto.OperatorResponse;
import se.lexicon.subscriptionapi.entity.Operator;
import se.lexicon.subscriptionapi.exception.BusinessRuleException;
import se.lexicon.subscriptionapi.exception.DuplicateResourceException;
import se.lexicon.subscriptionapi.exception.ResourceNotFoundException;
import se.lexicon.subscriptionapi.mapper.OperatorMapper;
import se.lexicon.subscriptionapi.repository.OperatorRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OperatorService {

    private final OperatorRepository operatorRepository;
    private final PlanRepository planRepository;
    private final OperatorMapper operatorMapper;

    @Transactional(readOnly = true)
    public List<OperatorResponse> getAllOperators() {
        return operatorRepository.findAll()
                .stream()
                .map(operatorMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OperatorResponse getOperatorById(Long id) {
        Operator operator = findOperatorById(id);
        return operatorMapper.toResponse(operator);
    }

    public OperatorResponse createOperator(OperatorRequest request) {

        if (operatorRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException(
                    "Operator with name '" + request.name() + "' already exists"
            );
        }

        Operator operator = operatorMapper.toEntity(request);

        Operator savedOperator = operatorRepository.save(operator);

        return operatorMapper.toResponse(savedOperator);
    }

    public OperatorResponse updateOperator(
            Long id,
            OperatorRequest request) {

        Operator operator = findOperatorById(id);

        operatorRepository.findByNameIgnoreCase(request.name())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Operator with name '" + request.name() + "' already exists"
                    );
                });

        operatorMapper.updateEntity(request, operator);

        Operator updatedOperator = operatorRepository.save(operator);

        return operatorMapper.toResponse(updatedOperator);
    }

    public void deleteOperator(Long id) {

        Operator operator = findOperatorById(id);

        if (!planRepository.findByOperatorId(id).isEmpty()) {
            throw new BusinessRuleException(
                    "Operator cannot be deleted because it has associated plans"
            );
        }

        operatorRepository.delete(operator);
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