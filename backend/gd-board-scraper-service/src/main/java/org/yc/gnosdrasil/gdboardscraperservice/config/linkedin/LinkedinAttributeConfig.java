package org.yc.gnosdrasil.gdboardscraperservice.config.linkedin;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scraper.linkedin.attributes")
@Getter
public class LinkedinAttributeConfig {
    private String jobUrnAttribute;
    private String datePostedSelector;
}
