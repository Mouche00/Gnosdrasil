package org.yc.gnosdrasil.gdboardscraperservice.config.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.ElementLocator;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.JobSelector;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.SearchProperties;

import java.util.List;

@Getter
@Setter
@ToString
public class JobBoardConfigProperties {
    protected String boardName;
    protected String baseUrl;
    protected SearchProperties searchProperties;
    protected ElementLocator jobListingsContainerElementLocator;
    protected ElementLocator jobItemElementLocator;
    protected ElementLocator jobDetailsElementLocator;
    protected List<JobSelector> jobSelectors;
    protected ElementLocator nextPageElementLocator;
    protected int pageLoadWaitTime;
    protected int elementWaitTime;
    protected int maxPages;
    protected int requestDelayMs;
}
