//package org.yc.gnosdrasil.gdmarketanalysisservice.repository;
//
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Aggregation;
//import org.yc.gnosdrasil.gdmarketanalysisservice.model.JobTrendAnalysis.TopJobTitle;
//import org.yc.gnosdrasil.gdmarketanalysisservice.model.JobTrendAnalysis.TopCompany;
//import org.yc.gnosdrasil.gdmarketanalysisservice.model.JobTrendAnalysis.DailyJobCount;
//import java.time.LocalDate;
//import java.util.List;
//
//public interface JobListingRepository extends MongoRepository<JobListing, String> {
//    List<JobListing> findByDatePostedBetween(LocalDate startDate, LocalDate endDate);
//
//    @Aggregation(pipeline = {
//        "{ $group: { _id: '$title', count: { $sum: 1 } } }",
//        "{ $sort: { count: -1 } }",
//        "{ $limit: 10 }"
//    })
//    List<TopJobTitle> findTopJobTitles();
//
//    @Aggregation(pipeline = {
//        "{ $group: { _id: '$company', count: { $sum: 1 } } }",
//        "{ $sort: { count: -1 } }",
//        "{ $limit: 10 }"
//    })
//    List<TopCompany> findTopCompanies();
//
//    @Aggregation(pipeline = {
//        "{ $group: { _id: '$datePosted', count: { $sum: 1 } } }",
//        "{ $sort: { _id: 1 } }"
//    })
//    List<DailyJobCount> findDailyJobCounts();
//}