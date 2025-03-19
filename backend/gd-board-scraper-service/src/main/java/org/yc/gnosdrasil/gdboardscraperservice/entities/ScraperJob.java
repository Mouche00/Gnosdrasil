package org.yc.gnosdrasil.gdboardscraperservice.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.ScraperJobStatus;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "scraper_jobs")
@Getter
@Setter
@Builder
public class ScraperJob extends BaseEntity<String> {

    private ScraperJobStatus status;
    private int progress;
    private int jobsFound;

//    @Lob
//    @Column(columnDefinition = "TEXT")
//    private String logs;

    @Transient
    private List<String> logsList = new ArrayList<>();

    private SearchParams request;
    private List<JobListing> jobListings = new ArrayList<>();

//    public List<String> getLogsList() {
//        if (logs != null && logsList.isEmpty()) {
//            String[] logsArray = logs.split("\n");
//            for (String log : logsArray) {
//                logsList.add(log);
//            }
//        }
//        return logsList;
//    }
//
//    public void addLog(String log) {
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
//        String formattedLog = "[" + timestamp + "] " + log;
//        logsList.add(formattedLog);
//
//        // Update the logs string
//        if (logs == null) {
//            logs = formattedLog;
//        } else {
//            logs += "\n" + formattedLog;
//        }
//    }
}