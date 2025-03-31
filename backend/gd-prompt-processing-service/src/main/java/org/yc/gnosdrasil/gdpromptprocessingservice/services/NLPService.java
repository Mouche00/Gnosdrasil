package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;

public interface NLPService {
    NLPResult processText(String prompt);
}
