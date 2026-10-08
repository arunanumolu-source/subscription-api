package se.lexicon.subscriptionapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.lexicon.subscriptionapi.dto.OperatorRequest;
import se.lexicon.subscriptionapi.dto.OperatorResponse;
import se.lexicon.subscriptionapi.service.OperatorService;

import java.util.List;

@RestController
@RequestMapping("/api/operators")
@RequiredArgsConstructor
public class OperatorController {

    private final OperatorService operatorService;

    @GetMapping
    public ResponseEntity<List<OperatorResponse>> getAllOperators() {
        return ResponseEntity.ok(
                operatorService.getAllOperators()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperatorResponse> getOperatorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                operatorService.getOperatorById(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OperatorResponse> createOperator(
            @Valid @RequestBody OperatorRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(operatorService.createOperator(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OperatorResponse> updateOperator(
            @PathVariable Long id,
            @Valid @RequestBody OperatorRequest request) {

        return ResponseEntity.ok(
                operatorService.updateOperator(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOperator(
            @PathVariable Long id) {

        operatorService.deleteOperator(id);

        return ResponseEntity.noContent().build();
    }
}