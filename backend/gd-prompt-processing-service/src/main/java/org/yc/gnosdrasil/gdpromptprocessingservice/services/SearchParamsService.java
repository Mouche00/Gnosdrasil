package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.*;

import java.util.List;

public interface SearchParamsService {
    JobAnalysisDTO getAnalysis(PromptRequestDTO promptRequestDTO);
    GlobalResponseDTO getGlobalResponse(PromptRequestDTO promptRequestDTO);
}
