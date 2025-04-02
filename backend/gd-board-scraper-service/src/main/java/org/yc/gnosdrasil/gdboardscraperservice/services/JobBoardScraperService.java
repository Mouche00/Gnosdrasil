package org.yc.gnosdrasil.gdboardscraperservice.services;

import org.yc.gnosdrasil.gdboardscraperservice.dtos.JobListingDTO;
import org.yc.gnosdrasil.gdboardscraperservice.dtos.SearchParamsDTO;
import org.yc.gnosdrasil.gdboardscraperservice.entities.JobListing;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;

import java.util.List;
import java.util.concurrent.Future;

public interface JobBoardScraperService {

    List<JobListingDTO> getAllJobListings(SearchParamsDTO searchParamsDTO);
    Future<List<JobListingDTO>> scrapeJobs(SearchParamsDTO searchParamsDTO);
}
