package org.yc.gnosdrasil.gdboardscraperservice.services;

import org.yc.gnosdrasil.gdboardscraperservice.entities.ScraperResult;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;

public interface JobBoardScraperService {
    ScraperResult scrapeJobs(SearchParams searchParams);
}
