package org.yc.gnosdrasil.gdmarketanalysisservice.dtos;

import org.yc.gnosdrasil.gdmarketanalysisservice.model.JobTrendAnalysis;

import java.util.List;
import java.util.Map;

public record JobAnalysisDTO(
        List<JobTrendAnalysis.TopJobTitle> topJobTitles,
        List<JobTrendAnalysis.TopSkill> topSkills,
        List<JobTrendAnalysis.DailyJobCount> dailyJobCounts,
        List<JobTrendAnalysis.TopCompany> topCompanies,
        Map<String, Integer> experienceLevelDistribution,
        Map<String, Integer> jobTypeDistribution,
        List<JobListingDTO> jobs) {
}
