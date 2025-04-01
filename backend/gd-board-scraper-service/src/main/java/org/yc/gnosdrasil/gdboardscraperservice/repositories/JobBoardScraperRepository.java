package org.yc.gnosdrasil.gdboardscraperservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yc.gnosdrasil.gdboardscraperservice.entities.JobListing;

import java.util.Optional;

@Repository
public interface JobBoardScraperRepository extends MongoRepository <JobListing, String> {

    Optional<JobListing> findByJobId(String jobId);
}
