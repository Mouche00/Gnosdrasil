package org.yc.gnosdrasil.gdboardscraperservice.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.LinkedInDatePostedEnum;

import java.util.List;

@Document(collection = "search_params")
@Getter
@Setter
public class SearchParams extends BaseEntity<String> {
    private List<String> keywords;
    private String location;
    private String experienceLevel;
    private String datePosted;

    @DBRef
    private List<JobListing> jobListings;
}
