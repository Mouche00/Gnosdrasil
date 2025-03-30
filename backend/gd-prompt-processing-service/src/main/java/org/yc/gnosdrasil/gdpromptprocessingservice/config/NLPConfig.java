package org.yc.gnosdrasil.gdpromptprocessingservice.config;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class NLPConfig {

    @Bean
    public StanfordCoreNLP stanfordCoreNLP() {
        Properties props = new Properties();
        // Core annotators with enhanced settings
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,sentiment,coref,quote");
        
        // Tokenization settings
        props.setProperty("tokenize.options", "untokenizable=noneKeep,normalizeParentheses=true,normalizeOtherBrackets=true");
        props.setProperty("tokenize.language", "en");
        
        // Sentence splitting settings
        props.setProperty("ssplit.newlineIsSentenceBreak", "always");
//        props.setProperty("ssplit.boundaryMultiTokenRegex", "[.!?]+");
        
        // POS tagging settings
        props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words-distsim.tagger");
        
        // NER settings
        props.setProperty("ner.model", "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");
        props.setProperty("ner.useSUTime", "false");
        props.setProperty("ner.applyNumericClassifiers", "false");
        
        // Parser settings
        props.setProperty("parse.model", "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        props.setProperty("parse.maxlen", "100");
        
        // Dependency parsing settings
        props.setProperty("depparse.model", "edu/stanford/nlp/models/parser/nndep/english_UD.gz");
        
        // Sentiment settings
        props.setProperty("sentiment.model", "edu/stanford/nlp/models/sentiment/sentiment.ser.gz");
        
        // Coreference settings
        props.setProperty("coref.algorithm", "neural");
        props.setProperty("coref.neural.greedy", "true");
        
        return new StanfordCoreNLP(props);
    }
} 