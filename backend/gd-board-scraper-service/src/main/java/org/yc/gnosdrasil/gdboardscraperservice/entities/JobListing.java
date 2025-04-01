package org.yc.gnosdrasil.gdboardscraperservice.entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "job_listings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

//    @DBRef
//    private ScraperResult scraperResult;

    @DBRef
    private SearchParams searchParams;
}