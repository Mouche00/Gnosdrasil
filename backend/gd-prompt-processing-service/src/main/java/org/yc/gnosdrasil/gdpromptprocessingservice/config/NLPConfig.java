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
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,sentiment");
        props.setProperty("tokenize.options", "untokenizable=noneKeep");
        props.setProperty("ssplit.newlineIsSentenceBreak", "always");
        return new StanfordCoreNLP(props);
    }
} 