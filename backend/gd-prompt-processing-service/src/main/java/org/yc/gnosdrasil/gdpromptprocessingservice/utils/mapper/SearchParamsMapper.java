package org.yc.gnosdrasil.gdpromptprocessingservice.utils.mapper;

import org.mapstruct.Mapper;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.SearchParamsRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.SearchParams;

@Mapper(componentModel = "spring")
public interface SearchParamsMapper {

    SearchParams toEntity(SearchParamsRequestDTO dto);

    SearchParamsRequestDTO toDto(SearchParams entity);
}