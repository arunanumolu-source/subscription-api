package se.lexicon.subscriptionapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.subscriptionapi.entity.Customer;
import se.lexicon.subscriptionapi.entity.Operator;
import se.lexicon.subscriptionapi.entity.Plan;
import se.lexicon.subscriptionapi.enums.Role;
import se.lexicon.subscriptionapi.enums.ServiceType;
import se.lexicon.subscriptionapi.repository.CustomerRepository;
import se.lexicon.subscriptionapi.repository.OperatorRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final OperatorRepository operatorRepository;
    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        createAdminIfMissing();

        Operator telia = getOrCreateOperator(
                "Telia",
                "Telecommunications operator offering mobile and internet services"
        );

        Operator telenor = getOrCreateOperator(
                "Telenor",
                "Telecommunications operator providing mobile and broadband services"
        );

        createPlanIfMissing(
                telia,
                "Telia Mobile 20GB",
                new BigDecimal("299.00"),
                ServiceType.MOBILE,
                20,
                true
        );

        createPlanIfMissing(
                telia,
                "Telia Unlimited Mobile",
                new BigDecimal("499.00"),
                ServiceType.MOBILE,
                null,
                true
        );

        createPlanIfMissing(
                telia,
                "Telia Fiber 500",
                new BigDecimal("549.00"),
                ServiceType.INTERNET,
                null,
                false
        );

        createPlanIfMissing(
                telenor,
                "Telenor Mobile 30GB",
                new BigDecimal("329.00"),
                ServiceType.MOBILE,
                30,
                true
        );

        createPlanIfMissing(
                telenor,
                "Telenor Fiber 250",
                new BigDecimal("449.00"),
                ServiceType.INTERNET,
                null,
                true
        );

        createPlanIfMissing(
                telenor,
                "Telenor Legacy Mobile",
                new BigDecimal("199.00"),
                ServiceType.MOBILE,
                10,
                false
        );
    }

    private void createAdminIfMissing() {

        String adminEmail = "admin@subscription.com";

        if (!customerRepository.existsByEmailIgnoreCase(adminEmail)) {

            Customer admin = Customer.builder()
                    .firstName("System")
                    .lastName("Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("Admin123!"))
                    .role(Role.ADMIN)
                    .build();

            customerRepository.save(admin);
        }
    }

    private Operator getOrCreateOperator(
            String name,
            String description) {

        return operatorRepository.findByNameIgnoreCase(name)
                .orElseGet(() ->
                        operatorRepository.save(
                                Operator.builder()
                                        .name(name)
                                        .description(description)
                                        .build()
                        )
                );
    }

    private void createPlanIfMissing(
            Operator operator,
            String name,
            BigDecimal price,
            ServiceType serviceType,
            Integer dataLimitGb,
            boolean active) {

        if (!planRepository.existsByOperatorIdAndNameIgnoreCase(
                operator.getId(),
                name
        )) {

            Plan plan = Plan.builder()
                    .name(name)
                    .price(price)
                    .serviceType(serviceType)
                    .dataLimitGb(dataLimitGb)
                    .active(active)
                    .operator(operator)
                    .build();

            planRepository.save(plan);
        }
    }
}