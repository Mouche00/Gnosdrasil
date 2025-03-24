package org.yc.gnosdrasil.gdboardscraperservice.config.common;

import lombok.Builder;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;
import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.common.JobField;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.ElementLocator;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.FieldSelector;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.SearchProperties;

import java.util.Map;

/**
 * Configuration for a specific job board scraper
 */
@Builder
public record JobBoardConfig(
        String boardName,
        String baseUrl,
        SearchProperties searchProperties,
        ElementLocator jobListingsContainerElementLocator,
        ElementLocator jobItemElementLocator,
        Map<JobField, FieldSelector> fieldSelectors,
        ElementLocator nextPageElementLocator,
        int pageLoadWaitTime,
        int elementWaitTime,
        int maxPages,
        int requestDelayMs
) {
}
