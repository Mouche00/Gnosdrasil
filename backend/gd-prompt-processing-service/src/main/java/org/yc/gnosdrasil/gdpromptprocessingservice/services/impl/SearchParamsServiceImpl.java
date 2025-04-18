package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.client.JobAnalysisClient;
import org.yc.gnosdrasil.gdpromptprocessingservice.client.JobBoardScraperClient;
import org.yc.gnosdrasil.gdpromptprocessingservice.client.RoadmapClient;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.*;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.LanguageIntent;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.SearchParams;
import org.yc.gnosdrasil.gdpromptprocessingservice.exception.NLPProcessingException;
import org.yc.gnosdrasil.gdpromptprocessingservice.repository.SearchParamsRepository;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.NLPService;
import org.yc.gnosdrasil.gdpromptprocessingservice.services.SearchParamsService;
import org.yc.gnosdrasil.gdpromptprocessingservice.utils.mapper.SearchParamsMapper;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class SearchParamsServiceImpl implements SearchParamsService {

    private final NLPService nlpService;
    private final SearchParamsMapper searchParamsMapper;
    private final JobAnalysisClient jobAnalysisClient;
    private final RoadmapClient roadmapClient;

    private SearchParamsDTO generateSearchParams(String prompt) {
        try {
            NLPResult nlpResult = nlpService.processText(prompt);
            List<LanguageIntent> languageIntents = nlpResult.getLanguageIntents();
            log.info("Generated language intents: {}", languageIntents.stream().findFirst().orElse(null).isFocus());
            return searchParamsMapper.toDto(SearchParams.builder()
                    .keywords(languageIntents.stream().map(LanguageIntent::getLang).collect(Collectors.toList()))
                    .experienceLevel(languageIntents.stream().findFirst().map(LanguageIntent::getLevel).orElse(null))
                    .location("Morocco")
                    .build());
        } catch (Exception e) {
            log.error("Failed to generate search params", e);
            throw new NLPProcessingException("Failed to generate search params", e);
        }
    }

    @Override
    public JobAnalysisDTO getAnalysis(PromptRequestDTO promptRequestDTO) {
        SearchParamsDTO searchParamsDTO = generateSearchParams(promptRequestDTO.prompt());
        return jobAnalysisClient.analyse(searchParamsDTO);
    }

    public RoadmapResponseDTO getRoadmap(PromptRequestDTO promptRequestDTO) {
        SearchParamsDTO searchParamsDTO = generateSearchParams(promptRequestDTO.prompt());
        return roadmapClient.getRoadmap(searchParamsDTO);
    }

    public GlobalResponseDTO getGlobalResponse(PromptRequestDTO promptRequestDTO) {
        SearchParamsDTO searchParamsDTO = generateSearchParams(promptRequestDTO.prompt());
        return new GlobalResponseDTO(jobAnalysisClient.analyse(searchParamsDTO), roadmapClient.getRoadmap(searchParamsDTO));
    }
}
