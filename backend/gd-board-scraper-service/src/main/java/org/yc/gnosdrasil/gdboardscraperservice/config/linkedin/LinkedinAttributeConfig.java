package org.yc.gnosdrasil.gdboardscraperservice.config.linkedin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scraper.linkedin.attributes")
@Getter
@Setter
public class LinkedinAttributeConfig {
    private String jobUrnSelector;
    private String titleSelector;
    private String datePostedSelector;
}
