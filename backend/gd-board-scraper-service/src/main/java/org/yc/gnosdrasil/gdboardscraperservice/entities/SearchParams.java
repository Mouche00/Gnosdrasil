package org.yc.gnosdrasil.gdboardscraperservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "search_params")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchParams extends BaseEntity<String> {
    private List<String> keywords;
    private String location;
    private String experienceLevel;
    private String datePosted;

//    @DBRef
//    private List<JobListing> jobListings;
}
