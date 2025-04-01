package org.yc.gnosdrasil.gdmarketanalysisservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdmarketanalysisservice.client.JobBoardScraperClient;
import org.yc.gnosdrasil.gdmarketanalysisservice.dtos.JobListingDTO;
import org.yc.gnosdrasil.gdmarketanalysisservice.dtos.SearchParamsDTO;
import org.yc.gnosdrasil.gdmarketanalysisservice.model.JobTrendAnalysis;
//import org.yc.gnosdrasil.gdmarketanalysisservice.repository.JobListingRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobAnalysisService {
//    private final JobListingRepository jobListingRepository;
    private final JobBoardScraperClient jobBoardScraperClient;

    public JobTrendAnalysis analyzeJobTrends(SearchParamsDTO searchParamsDTO) {
        JobTrendAnalysis analysis = new JobTrendAnalysis();
        
        // Get all job listings
        List<JobListingDTO> allJobs = jobBoardScraperClient.startScraping(searchParamsDTO);
        
        // Analyze top job titles
        analysis.setTopJobTitles(analyzeTopJobTitles(allJobs));
        
        // Analyze top skills
        analysis.setTopSkills(analyzeTopSkills(allJobs));
        
        // Analyze daily job counts
//        analysis.setDailyJobCounts(jobListingRepository.findDailyJobCounts());
//
//        // Analyze top companies
//        analysis.setTopCompanies(jobListingRepository.findTopCompanies());
        
        // Analyze experience level distribution
        analysis.setExperienceLevelDistribution(analyzeExperienceLevelDistribution(allJobs));
        
        // Analyze job type distribution
        analysis.setJobTypeDistribution(analyzeJobTypeDistribution(allJobs));
        
        return analysis;
    }

    private List<JobTrendAnalysis.TopJobTitle> analyzeTopJobTitles(List<JobListingDTO> jobs) {
        return jobs.stream()
                .collect(Collectors.groupingBy(JobListingDTO::title, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> {
                    JobTrendAnalysis.TopJobTitle topTitle = new JobTrendAnalysis.TopJobTitle();
                    topTitle.setTitle(entry.getKey());
                    topTitle.setCount(entry.getValue().intValue());
                    return topTitle;
                })
                .sorted(Comparator.comparing(JobTrendAnalysis.TopJobTitle::getCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<JobTrendAnalysis.TopSkill> analyzeTopSkills(List<JobListingDTO> jobs) {
        // Common technical skills to look for
        Set<String> technicalSkills = new HashSet<>(Arrays.asList(
            "java", "spring", "angular", "react", "python", "javascript", "typescript",
            "docker", "kubernetes", "aws", "azure", "devops", "ci/cd", "git",
            "sql", "nosql", "mongodb", "postgresql", "mysql", "redis",
            "microservices", "rest", "api", "agile", "scrum", "kanban"
        ));

        Map<String, Long> skillCounts = new HashMap<>();
        
        for (JobListingDTO job : jobs) {
            String description = job.description().toLowerCase();
            for (String skill : technicalSkills) {
                if (description.contains(skill)) {
                    skillCounts.merge(skill, 1L, Long::sum);
                }
            }
        }

        return skillCounts.entrySet().stream()
                .map(entry -> {
                    JobTrendAnalysis.TopSkill topSkill = new JobTrendAnalysis.TopSkill();
                    topSkill.setSkill(entry.getKey());
                    topSkill.setCount(entry.getValue().intValue());
                    return topSkill;
                })
                .sorted(Comparator.comparing(JobTrendAnalysis.TopSkill::getCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    private Map<String, Integer> analyzeExperienceLevelDistribution(List<JobListingDTO> jobs) {
        return jobs.stream()
                .collect(Collectors.groupingBy(
                    JobListingDTO::experienceLevel,
                    Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    private Map<String, Integer> analyzeJobTypeDistribution(List<JobListingDTO> jobs) {
        return jobs.stream()
                .collect(Collectors.groupingBy(
                    JobListingDTO::jobType,
                    Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }
} 