package org.yc.gnosdrasil.gdpromptprocessingservice.dtos;

public record JobListingDTO(
        String jobId,
        String title,
        String company,
        String location,
        String salary,
        String description,
        String datePosted,
        String url,
        String applyUrl,
        String jobType,
        String experienceLevel) {
}
