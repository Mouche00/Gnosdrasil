package org.yc.gnosdrasil.gdpromptprocessingservice.utils.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.SearchParamsResponseDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.SearchParams;

@Mapper(componentModel = "spring")
public interface SearchParamsMapper {

    SearchParams toEntity(SearchParamsResponseDTO dto);

    @Mappings({
            @Mapping(source = "createdAt", target = "date"),
    })
    SearchParamsResponseDTO toDto(SearchParams entity);
}