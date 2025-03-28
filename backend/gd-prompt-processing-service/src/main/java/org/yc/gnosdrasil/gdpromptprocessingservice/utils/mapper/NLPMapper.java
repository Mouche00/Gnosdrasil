package org.yc.gnosdrasil.gdpromptprocessingservice.utils.mapper;//package org.yc.gnosdrasil.gdpromptprocessingservice.utils.mapper;
//
//import org.springframework.stereotype.Component;
//import org.yc.gnosdrasil.gdpromptprocessingservice.entity.LanguageIntent;
//import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;
//import org.yc.gnosdrasil.gdpromptprocessingservice.entity.SentenceAnalysis;
//import org.yc.gnosdrasil.gdpromptprocessingservice.entity.TokenInfo;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class NLPMapper {
//
//    public NLPResult toEntity(NLPResult dto, String originalText) {
//        NLPResult entity = new NLPResult();
//        entity.setOriginalText(originalText);
//        entity.setCorrectedText(dto.correctedText());
//        entity.setOverallSentiment(dto.overallSentiment());
//        entity.setCreatedAt(LocalDateTime.now());
//
//        // Map sentence analyses
//        List<SentenceAnalysis> sentenceAnalyses = dto.sentenceAnalyses().stream()
//                .map(sa -> toSentenceAnalysisEntity(sa, entity))
//                .collect(Collectors.toList());
//        entity.setSentenceAnalyses(sentenceAnalyses);
//
//        // Map language intents
//        List<LanguageIntent> languageIntents = dto.languageIntents().stream()
//                .map(li -> toLanguageIntentEntity(li, entity))
//                .collect(Collectors.toList());
//        entity.setLanguageIntents(languageIntents);
//
//        return entity;
//    }
//
//    private SentenceAnalysis toSentenceAnalysisEntity(SentenceAnalysis dto, NLPResult nlpResult) {
//        SentenceAnalysis entity = new SentenceAnalysis();
//        entity.setParseTree(dto.parseTree());
//        entity.setSentiment(dto.sentiment());
//        entity.setNlpResult(nlpResult);
//
//        // Map tokens
//        List<TokenInfo> tokens = dto.tokens().stream()
//                .map(t -> toTokenInfoEntity(t, entity))
//                .collect(Collectors.toList());
//        entity.setTokens(tokens);
//
//        return entity;
//    }
//
//    private TokenInfo toTokenInfoEntity(TokenInfo dto, SentenceAnalysis sentenceAnalysis) {
//        TokenInfo entity = new TokenInfo();
//        entity.setWord(dto.word());
//        entity.setPos(dto.pos());
//        entity.setLemma(dto.lemma());
//        entity.setNer(dto.ner());
//        entity.setSentenceAnalysis(sentenceAnalysis);
//        return entity;
//    }
//
//    private LanguageIntent toLanguageIntentEntity(LanguageIntent dto, NLPResult nlpResult) {
//        LanguageIntent entity = new LanguageIntent();
//        entity.setLang(dto.lang);
//        entity.setLevel(dto.level);
//        entity.setNlpResult(nlpResult);
//        return entity;
//    }
//
//    public NLPResult toDto(NLPResult entity) {
//        return new NLPResult(
//                entity.getCorrectedText(),
//                entity.getSentenceAnalyses().stream()
//                        .map(this::toSentenceAnalysisDto)
//                        .collect(Collectors.toList()),
//                entity.getLanguageIntents().stream()
//                        .map(this::toLanguageIntentDto)
//                        .collect(Collectors.toList()),
//                entity.getOverallSentiment()
//        );
//    }
//
//    private SentenceAnalysis toSentenceAnalysisDto(SentenceAnalysis entity) {
//        return new SentenceAnalysis(
//                entity.getTokens().stream()
//                        .map(this::toTokenInfoDto)
//                        .collect(Collectors.toList()),
//                entity.getParseTree(),
//                entity.getSentiment()
//        );
//    }
//
//    private TokenInfo toTokenInfoDto(TokenInfo entity) {
//        return new TokenInfo(
//                entity.getWord(),
//                entity.getPos(),
//                entity.getLemma(),
//                entity.getNer()
//        );
//    }
//
//    private LanguageIntent toLanguageIntentDto(LanguageIntent entity) {
//        LanguageIntent dto = new LanguageIntent();
//        dto.setLang(entity.getLang());
//        dto.setLevel(entity.getLevel());
//        return dto;
//    }
//}