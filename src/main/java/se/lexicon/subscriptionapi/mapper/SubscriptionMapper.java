package se.lexicon.subscriptionapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.lexicon.subscriptionapi.dto.SubscriptionResponse;
import se.lexicon.subscriptionapi.entity.Subscription;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "plan.id", target = "planId")
    @Mapping(source = "plan.name", target = "planName")
    @Mapping(source = "plan.price", target = "price")
    @Mapping(source = "plan.serviceType", target = "serviceType")
    @Mapping(source = "plan.operator.id", target = "operatorId")
    @Mapping(source = "plan.operator.name", target = "operatorName")
    SubscriptionResponse toResponse(Subscription subscription);
}