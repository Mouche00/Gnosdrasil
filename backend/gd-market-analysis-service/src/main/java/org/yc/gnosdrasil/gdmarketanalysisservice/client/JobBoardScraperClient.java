package org.yc.gnosdrasil.gdmarketanalysisservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yc.gnosdrasil.gdmarketanalysisservice.dtos.JobListingDTO;
import org.yc.gnosdrasil.gdmarketanalysisservice.dtos.SearchParamsDTO;

import java.util.List;

@FeignClient(name = "jobScraperClient", url = "${feign.client.url.jobScraper:http://localhost:8082}")
public interface JobBoardScraperClient {
    @PostMapping("/api/board/scrape")
    List<JobListingDTO> startScraping(@RequestBody SearchParamsDTO request);
}