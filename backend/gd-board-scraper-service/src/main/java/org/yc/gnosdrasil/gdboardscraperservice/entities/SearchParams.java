package org.yc.gnosdrasil.gdboardscraperservice.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.LinkedInDatePostedEnum;

import java.util.List;

@Document(collection = "search_params")
@Getter
@Setter
public class SearchParams extends BaseEntity<String> {
    List<String> keywords;
    String location;
    String experienceLevel;
    LinkedInDatePostedEnum datePosted;
}
