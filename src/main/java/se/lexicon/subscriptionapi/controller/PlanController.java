package se.lexicon.subscriptionapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.lexicon.subscriptionapi.dto.PlanRequest;
import se.lexicon.subscriptionapi.dto.PlanResponse;
import se.lexicon.subscriptionapi.service.PlanService;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        return ResponseEntity.ok(
                planService.getAllPlans()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<PlanResponse>> getActivePlans() {
        return ResponseEntity.ok(
                planService.getActivePlans()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getPlanById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                planService.getPlanById(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanResponse> createPlan(
            @Valid @RequestBody PlanRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(planService.createPlan(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanResponse> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody PlanRequest request) {

        return ResponseEntity.ok(
                planService.updatePlan(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlan(
            @PathVariable Long id) {

        planService.deletePlan(id);

        return ResponseEntity.noContent().build();
    }
}