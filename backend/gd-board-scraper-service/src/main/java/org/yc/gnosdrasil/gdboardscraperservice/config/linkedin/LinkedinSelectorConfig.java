package org.yc.gnosdrasil.gdboardscraperservice.config.linkedin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scraper.linkedin.selectors")
@Getter
@Setter
public class LinkedinSelectorConfig {
    private String jobUrnSelector;
    private String titleSelector;
    private String datePostedSelector;
}
