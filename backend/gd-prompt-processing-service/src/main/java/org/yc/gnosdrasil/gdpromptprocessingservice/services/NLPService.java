package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.NLPResultDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;

public interface NLPService {
    NLPResultDTO processText(PromptRequestDTO promptRequestDTO);
}
