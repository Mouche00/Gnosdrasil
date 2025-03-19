package org.yc.gnosdrasil.gdboardscraperservice.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDate datePosted;
    private String description;

    private String url;
//    private LocalDateTime scrapedAt;
}