package org.yc.gnosdrasil.gdboardscraperservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yc.gnosdrasil.gdboardscraperservice.repositories.JobBoardScraperRepository;
import org.yc.gnosdrasil.gdboardscraperservice.services.JobBoardScraperService;
import org.yc.gnosdrasil.gdboardscraperservice.services.impl.JobBoardScraperServiceImpl;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.FieldExtractor;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.SeleniumHelper;
import org.yc.gnosdrasil.gdboardscraperservice.utils.mappers.JobListingMapper;

@Configuration
@RequiredArgsConstructor
public class ScrapersConfig {
    private final SeleniumHelper seleniumHelper;
    private final FieldExtractor fieldExtractor;
    private final LinkedinJobBoardConfig linkedinJobBoardConfig;
    private final JobBoardScraperRepository jobBoardScraperRepository;
    private final JobListingMapper jobListingMapper;

    @Bean("linkedinScraperService")
    public JobBoardScraperService linkedinScraperService() {
        return new JobBoardScraperServiceImpl(seleniumHelper, fieldExtractor, linkedinJobBoardConfig, jobBoardScraperRepository, jobListingMapper);
    }
}
