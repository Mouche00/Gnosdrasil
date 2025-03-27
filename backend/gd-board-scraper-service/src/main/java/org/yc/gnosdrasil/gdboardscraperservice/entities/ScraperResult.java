package org.yc.gnosdrasil.gdboardscraperservice.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.ScraperJobStatus;

import java.util.List;

@Document(collection = "scraper_results")
@Getter
@Setter
@Builder
public class ScraperResult extends BaseEntity<String> {

    private ScraperJobStatus status;
    private String boardName;
    private int jobsFound;

    private List<String> logsList;

    @DBRef
    private SearchParams searchParams;
}