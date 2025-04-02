package org.yc.gnosdrasil.gdpromptprocessingservice.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

public record JobAnalysisDTO(
        List<TopJobTitle> topJobTitles,
        List<TopSkill> topSkills,
        List<DailyJobCount> dailyJobCounts,
        List<TopCompany> topCompanies,
        Map<String, Integer> experienceLevelDistribution,
        Map<String, Integer> jobTypeDistribution,
        List<JobListingDTO> jobs) {
    public record TopJobTitle(String title, int count) {}
    public record TopSkill(String skill, int count) {}
    public record DailyJobCount(String date, int count) {}
    public record TopCompany(String company, int count) {}
}
