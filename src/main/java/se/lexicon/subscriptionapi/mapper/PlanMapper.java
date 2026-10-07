package se.lexicon.subscriptionapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import se.lexicon.subscriptionapi.dto.PlanRequest;
import se.lexicon.subscriptionapi.dto.PlanResponse;
import se.lexicon.subscriptionapi.entity.Plan;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    @Mapping(source = "operator.id", target = "operatorId")
    @Mapping(source = "operator.name", target = "operatorName")
    PlanResponse toResponse(Plan plan);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operator", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Plan toEntity(PlanRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operator", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            PlanRequest request,
            @MappingTarget Plan plan
    );
}