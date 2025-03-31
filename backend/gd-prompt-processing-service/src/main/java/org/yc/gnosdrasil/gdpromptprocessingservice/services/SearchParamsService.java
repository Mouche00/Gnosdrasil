package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.SearchParamsRequestDTO;

public interface SearchParamsService {
    SearchParamsRequestDTO generateSearchParams(PromptRequestDTO promptRequestDTO);
}
