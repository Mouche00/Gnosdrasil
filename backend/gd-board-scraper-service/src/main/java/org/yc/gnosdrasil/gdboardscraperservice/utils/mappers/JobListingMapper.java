package org.yc.gnosdrasil.gdboardscraperservice.utils.mappers;

import org.mapstruct.Mapper;
import org.yc.gnosdrasil.gdboardscraperservice.dtos.JobListingDTO;
import org.yc.gnosdrasil.gdboardscraperservice.entities.JobListing;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobListingMapper {

    JobListingDTO toDTO(JobListing jobListing);

    JobListing toEntity(JobListingDTO jobListingDTO);


    List<JobListingDTO> toDTOs(List<JobListing> jobListings);

    List<JobListing> toEntities(List<JobListingDTO> jobListingDTOs);
}
