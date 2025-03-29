package org.yc.gnosdrasil.gdpromptprocessingservice.dtos;

public record LanguageIntentDTO(
        Long id,
        String lang,
        String level,
        boolean isFocus
) {
}
