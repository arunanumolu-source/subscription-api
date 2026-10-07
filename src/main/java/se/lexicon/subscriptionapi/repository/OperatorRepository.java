package se.lexicon.subscriptionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.subscriptionapi.entity.Operator;

import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator, Long> {

    Optional<Operator> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}