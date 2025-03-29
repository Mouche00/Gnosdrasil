package org.yc.gnosdrasil.gdpromptprocessingservice.dtos;

import java.util.List;

public record NLPResultDTO(
        String correctedText,
        List<LanguageIntentDTO> languageIntents) {
}
