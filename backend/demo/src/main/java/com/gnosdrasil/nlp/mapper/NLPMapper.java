package com.gnosdrasil.nlp.mapper;

import com.gnosdrasil.nlp.entity.*;
import com.gnosdrasil.nlp.service.AdvancedNLPService.*;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NLPMapper {
    
    public NLPResultEntity toEntity(NLPResult dto, String originalText) {
        NLPResultEntity entity = new NLPResultEntity();
        entity.setOriginalText(originalText);
        entity.setCorrectedText(dto.correctedText());
        entity.setOverallSentiment(dto.overallSentiment());
        entity.setCreatedAt(LocalDateTime.now());
        
        // Map sentence analyses
        List<SentenceAnalysisEntity> sentenceAnalyses = dto.sentenceAnalyses().stream()
            .map(sa -> toSentenceAnalysisEntity(sa, entity))
            .collect(Collectors.toList());
        entity.setSentenceAnalyses(sentenceAnalyses);
        
        // Map language intents
        List<LanguageIntentEntity> languageIntents = dto.languageIntents().stream()
            .map(li -> toLanguageIntentEntity(li, entity))
            .collect(Collectors.toList());
        entity.setLanguageIntents(languageIntents);
        
        return entity;
    }
    
    private SentenceAnalysisEntity toSentenceAnalysisEntity(SentenceAnalysis dto, NLPResultEntity nlpResult) {
        SentenceAnalysisEntity entity = new SentenceAnalysisEntity();
        entity.setParseTree(dto.parseTree());
        entity.setSentiment(dto.sentiment());
        entity.setNlpResult(nlpResult);
        
        // Map tokens
        List<TokenInfoEntity> tokens = dto.tokens().stream()
            .map(t -> toTokenInfoEntity(t, entity))
            .collect(Collectors.toList());
        entity.setTokens(tokens);
        
        return entity;
    }
    
    private TokenInfoEntity toTokenInfoEntity(TokenInfo dto, SentenceAnalysisEntity sentenceAnalysis) {
        TokenInfoEntity entity = new TokenInfoEntity();
        entity.setWord(dto.word());
        entity.setPos(dto.pos());
        entity.setLemma(dto.lemma());
        entity.setNer(dto.ner());
        entity.setSentenceAnalysis(sentenceAnalysis);
        return entity;
    }
    
    private LanguageIntentEntity toLanguageIntentEntity(LanguageIntent dto, NLPResultEntity nlpResult) {
        LanguageIntentEntity entity = new LanguageIntentEntity();
        entity.setLang(dto.lang);
        entity.setLevel(dto.level);
        entity.setNlpResult(nlpResult);
        return entity;
    }
    
    public NLPResult toDto(NLPResultEntity entity) {
        return new NLPResult(
            entity.getCorrectedText(),
            entity.getSentenceAnalyses().stream()
                .map(this::toSentenceAnalysisDto)
                .collect(Collectors.toList()),
            entity.getLanguageIntents().stream()
                .map(this::toLanguageIntentDto)
                .collect(Collectors.toList()),
            entity.getOverallSentiment()
        );
    }
    
    private SentenceAnalysis toSentenceAnalysisDto(SentenceAnalysisEntity entity) {
        return new SentenceAnalysis(
            entity.getTokens().stream()
                .map(this::toTokenInfoDto)
                .collect(Collectors.toList()),
            entity.getParseTree(),
            entity.getSentiment()
        );
    }
    
    private TokenInfo toTokenInfoDto(TokenInfoEntity entity) {
        return new TokenInfo(
            entity.getWord(),
            entity.getPos(),
            entity.getLemma(),
            entity.getNer()
        );
    }
    
    private LanguageIntent toLanguageIntentDto(LanguageIntentEntity entity) {
        LanguageIntent dto = new LanguageIntent();
        dto.setLang(entity.getLang());
        dto.setLevel(entity.getLevel());
        return dto;
    }
} 