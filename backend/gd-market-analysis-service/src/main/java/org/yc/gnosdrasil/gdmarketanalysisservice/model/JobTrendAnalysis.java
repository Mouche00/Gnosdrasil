package org.yc.gnosdrasil.gdmarketanalysisservice.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class JobTrendAnalysis {
    private List<TopJobTitle> topJobTitles;
    private List<TopSkill> topSkills;
    private List<DailyJobCount> dailyJobCounts;
    private List<TopCompany> topCompanies;
    private Map<String, Integer> experienceLevelDistribution;
    private Map<String, Integer> jobTypeDistribution;

    @Data
    public static class TopJobTitle {
        private String title;
        private int count;
    }

    @Data
    public static class TopSkill {
        private String skill;
        private int count;
    }

    @Data
    public static class DailyJobCount {
        private String date;
        private int count;
    }

    @Data
    public static class TopCompany {
        private String company;
        private int count;
    }
} 