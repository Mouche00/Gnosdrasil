package org.yc.gnosdrasil.gdboardscraperservice.config.linkedin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scraper.linkedin.selectors")
public record LinkedinSelectorConfig(
        String jobUrnSelector,
        String titleSelector,
        String companySelector,
        String locationSelector,
        String datePostedSelector
) {
}
