package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.SearchParamsResponseDTO;

public interface SearchParamsService {
    SearchParamsResponseDTO generateSearchParams(PromptRequestDTO promptRequestDTO);
}
