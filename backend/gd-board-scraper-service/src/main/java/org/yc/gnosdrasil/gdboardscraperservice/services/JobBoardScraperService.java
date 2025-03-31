package org.yc.gnosdrasil.gdboardscraperservice.services;

import org.yc.gnosdrasil.gdboardscraperservice.entities.JobListing;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;

import java.util.List;
import java.util.concurrent.Future;

public interface JobBoardScraperService {
    Future<List<JobListing>> scrapeJobs(SearchParams searchParams);
}
