package org.yc.gnosdrasil.gdboardscraperservice.config.linkedin;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scraper.linkedin")
public record LinkedinSelectorConfig(
        String jobUrnSelector,
        String titleSelector,
        String companySelector,
        String locationSelector,
        String datePostedSelector
) {
}
