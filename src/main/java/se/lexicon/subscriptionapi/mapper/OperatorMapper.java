package se.lexicon.subscriptionapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import se.lexicon.subscriptionapi.dto.OperatorRequest;
import se.lexicon.subscriptionapi.dto.OperatorResponse;
import se.lexicon.subscriptionapi.entity.Operator;

@Mapper(componentModel = "spring")
public interface OperatorMapper {

    OperatorResponse toResponse(Operator operator);

    Operator toEntity(OperatorRequest request);

    void updateEntity(
            OperatorRequest request,
            @MappingTarget Operator operator
    );
}