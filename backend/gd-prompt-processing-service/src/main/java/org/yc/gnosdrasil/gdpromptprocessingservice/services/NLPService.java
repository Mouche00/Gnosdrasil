package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.NLPResultDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;

public interface NLPService {
    NLPResult processText(PromptRequestDTO promptRequestDTO);
}
