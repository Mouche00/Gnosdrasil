package org.yc.gnosdrasil.gdpromptprocessingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.JobAnalysisDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.JobListingDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.SearchParamsDTO;

import java.util.List;

@FeignClient(name = "jobAnalysisClient", url = "${feign.client.url.jobAnalysis:http://localhost:8084}")
public interface JobAnalysisClient {
    @PostMapping("/api/analysis/trends")
    JobAnalysisDTO analyse(@RequestBody SearchParamsDTO request);
}