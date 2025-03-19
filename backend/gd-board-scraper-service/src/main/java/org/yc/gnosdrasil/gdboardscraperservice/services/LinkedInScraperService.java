package org.yc.gnosdrasil.gdboardscraperservice.services;

import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;

import java.util.concurrent.CompletableFuture;

public interface LinkedInScraperService {
    public CompletableFuture<String> startScrapingJob(SearchParams searchParams);

}
