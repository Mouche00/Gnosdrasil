package org.yc.gnosdrasil.gdboardscraperservice.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job_listings")
@Getter
@Setter
@ToString
@Builder
public class JobListing extends BaseEntity<String> {

    private String jobId;
    private String title;
    private String company;
    private String location;
    private String salary;
    private String description;
    private String datePosted;
    private String url;
    private String applyUrl;
    private String jobType;
    private String experienceLevel;

    @DBRef
    private ScraperResult scraperResult;

    @DBRef
    private SearchParams searchParams;
}