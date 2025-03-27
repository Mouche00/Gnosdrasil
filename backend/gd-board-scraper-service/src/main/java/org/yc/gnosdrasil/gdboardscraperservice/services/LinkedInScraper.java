package org.yc.gnosdrasil.gdboardscraperservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdboardscraperservice.config.common.JobBoardConfig;
import org.yc.gnosdrasil.gdboardscraperservice.config.common.LinkedinConfig;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.FieldExtractor;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.SeleniumHelper;

@Slf4j
@Service
@EnableConfigurationProperties(LinkedinConfig.class)
public class LinkedInScraper extends JobScraperEngine {

    private final LinkedinConfig config;
    public LinkedInScraper(SeleniumHelper seleniumHelper, FieldExtractor fieldExtractor, LinkedinConfig config) {
        super(seleniumHelper, fieldExtractor, config);
        this.config = config;
        log.info("LinkedInScraper initialized with config: {}", config);
    }
}
