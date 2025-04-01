package org.yc.gnosdrasil.gdpromptprocessingservice.services;

import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.JobListingDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.PromptRequestDTO;
import org.yc.gnosdrasil.gdpromptprocessingservice.dtos.SearchParamsDTO;

import java.util.List;

public interface SearchParamsService {
    List<JobListingDTO> getJobListings(PromptRequestDTO promptRequestDTO);
}
