package org.yc.gnosdrasil.gdpromptprocessingservice.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.SearchParamsDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.SearchParams;

@Mapper(componentModel = "spring")
public interface SearchParamsMapper {

    SearchParams toEntity(SearchParamsDTO dto);

    @Mappings({
            @Mapping(source = "createdAt", target = "date"),
    })
    SearchParamsDTO toDto(SearchParams entity);
}