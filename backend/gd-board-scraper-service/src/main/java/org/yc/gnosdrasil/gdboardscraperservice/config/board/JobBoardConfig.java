package org.yc.gnosdrasil.gdboardscraperservice.config.board;

import org.yc.gnosdrasil.gdboardscraperservice.utils.records.ElementLocator;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.JobSelector;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.SearchProperties;

import java.util.List;

/**
 * Configuration for a specific job board scraper
 */
public interface JobBoardConfig {
    String getBoardName();
    String getBaseUrl();
    SearchProperties getSearchProperties();
    ElementLocator getJobListingsContainerElementLocator();
    ElementLocator getJobItemElementLocator();
    ElementLocator getJobDetailsElementLocator();
    List<JobSelector> getJobSelectors();
    int getPageLoadWaitTime();
    int getElementWaitTime();
    int getMaxPages();
    int getRequestDelayMs();
}
