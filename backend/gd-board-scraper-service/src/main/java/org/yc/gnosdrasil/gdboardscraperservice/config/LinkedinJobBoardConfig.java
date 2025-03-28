package org.yc.gnosdrasil.gdboardscraperservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.yc.gnosdrasil.gdboardscraperservice.config.board.JobBoardConfig;
import org.yc.gnosdrasil.gdboardscraperservice.config.board.JobBoardConfigProperties;

@Configuration
@ConfigurationProperties(prefix = "scraper.linkedin")
public class LinkedinJobBoardConfig extends JobBoardConfigProperties implements JobBoardConfig {}

