package org.yc.gnosdrasil.gdboardscraperservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.yc.gnosdrasil.gdboardscraperservice.entities.JobListing;

@Repository
public interface JobBoardScraperRepository extends MongoRepository <JobListing, String> {

    JobListing findByJobId(String jobId);
}
