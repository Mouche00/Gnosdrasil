package org.yc.gnosdrasil.gdexternalresourceservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SerpApiConfig {

    @Value("${serpapi.api.key}")
    private String apiKey;

    private final String yandexEngine = "yandex";
}