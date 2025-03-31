package org.yc.gnosdrasil.gdboardscraperservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yc.gnosdrasil.gdboardscraperservice.repositories.JobBoardScraperRepository;
import org.yc.gnosdrasil.gdboardscraperservice.services.JobBoardScraperService;
import org.yc.gnosdrasil.gdboardscraperservice.services.impl.JobBoardScraperServiceImpl;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.FieldExtractor;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.SeleniumHelper;

@Configuration
@RequiredArgsConstructor
public class ScrapersConfig {
    private final SeleniumHelper seleniumHelper;
    private final FieldExtractor fieldExtractor;
    private final LinkedinJobBoardConfig linkedinJobBoardConfig;
    private final JobBoardScraperRepository jobBoardScraperRepository;

    @Bean("linkedinScraperService")
    public JobBoardScraperService linkedinScraperService() {
        return new JobBoardScraperServiceImpl(seleniumHelper, fieldExtractor, linkedinJobBoardConfig, jobBoardScraperRepository);
    }
}
