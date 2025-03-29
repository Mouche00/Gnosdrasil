package org.yc.gnosdrasil.gdpromptprocessingservice.utils.mapper;

import org.mapstruct.Mapper;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.NLPResultDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;

@Mapper(componentModel = "spring")
public interface NLPMapper {

    NLPResult toEntity(NLPResultDTO dto);
    NLPResultDTO toDto(NLPResult entity);
}