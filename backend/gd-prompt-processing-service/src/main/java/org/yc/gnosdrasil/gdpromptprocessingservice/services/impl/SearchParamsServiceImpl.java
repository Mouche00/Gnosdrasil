package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.SearchParamsRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.LanguageIntent;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.SearchParams;
import org.yc.gnosdrasil.gdpromptprocessingservice.exception.NLPProcessingException;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.NLPService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.SearchParamsService;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class SearchParamsServiceImpl implements SearchParamsService {

    private final NLPService nlpService;

    @Override
    public SearchParamsRequestDTO generateSearchParams(PromptRequestDTO promptRequestDTO) {
        try {
            NLPResult nlpResult = nlpService.processText(promptRequestDTO.prompt());
            List<LanguageIntent> languageIntents = nlpResult.getLanguageIntents();
            return SearchParams.builder()
                    .keywords(languageIntents.stream().map(LanguageIntent::getLang).collect(Collectors.toList()))
                    .experienceLevel(languageIntents.stream().findFirst().map(LanguageIntent::getLevel).orElse(null))
                    .build();
        } catch (Exception e) {
            log.error("Failed to generate search params", e);
            throw new NLPProcessingException("Failed to generate search params", e);
        }
    }
}
