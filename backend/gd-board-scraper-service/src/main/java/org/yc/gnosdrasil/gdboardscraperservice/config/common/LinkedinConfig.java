package org.yc.gnosdrasil.gdboardscraperservice.config.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.ElementLocator;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.JobSelector;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.SearchProperties;

import java.util.List;


//@PropertySource("classpath:scraper/linkedin-config.yml")
@Getter
@Setter
@ConfigurationProperties(prefix = "scraper.linkedin")
@ToString
public class LinkedinConfig implements JobBoardConfig {
    private String boardName;
    private String baseUrl;
    private SearchProperties searchProperties;
    private ElementLocator jobListingsContainerElementLocator;
    private ElementLocator jobItemElementLocator;
    private List<JobSelector> jobSelectors;
    //        ElementLocator nextPageElementLocator;
    private int pageLoadWaitTime;
    private int elementWaitTime;
    private int maxPages;
    private int requestDelayMs;
//    @Override
//    public String getBoardName() {
//        return boardName;
//    }
//
//    @Override
//    public String getBaseUrl() {
//        return baseUrl;
//    }
//
//    @Override
//    public SearchProperties getSearchProperties() {
//        return searchProperties;
//    }
//
//    @Override
//    public ElementLocator getJobListingsContainerElementLocator() {
//        return jobListingsContainerElementLocator;
//    }
//
//    @Override
//    public ElementLocator getJobItemElementLocator() {
//        return jobItemElementLocator;
//    }
//
//    @Override
//    public List<JobSelector> getJobSelectors() {
//        return jobSelectors;
//    }
//
//    @Override
//    public int getPageLoadWaitTime() {
//        return pageLoadWaitTime;
//    }
//
//    @Override
//    public int getElementWaitTime() {
//        return elementWaitTime;
//    }
//
//    @Override
//    public int getMaxPages() {
//        return maxPages;
//    }
//
//    @Override
//    public int getRequestDelayMs() {
//        return requestDelayMs;
//    }
}
