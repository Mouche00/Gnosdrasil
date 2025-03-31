package org.yc.gnosdrasil.gdboardscraperservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.yc.gnosdrasil.gdboardscraperservice.config.board.JobBoardConfig;
import org.yc.gnosdrasil.gdboardscraperservice.entities.JobListing;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;
import org.yc.gnosdrasil.gdboardscraperservice.exceptions.ScraperException;
import org.yc.gnosdrasil.gdboardscraperservice.repositories.JobBoardScraperRepository;
import org.yc.gnosdrasil.gdboardscraperservice.services.JobBoardScraperService;
import org.yc.gnosdrasil.gdboardscraperservice.utils.enums.JobField;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.FieldExtractor;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.SeleniumHelper;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.JobSelector;
import org.yc.gnosdrasil.gdboardscraperservice.config.board.PopupConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.UrlHelper.buildSearchUrl;

/**
 * Generic job scraper that can work with any job board using a configuration
 */
@Slf4j
@RequiredArgsConstructor
public class JobBoardScraperServiceImpl implements JobBoardScraperService {

    private final SeleniumHelper seleniumHelper;
    private final FieldExtractor fieldExtractor;
    private final JobBoardConfig config;
    private final JobBoardScraperRepository jobBoardScraperRepository;

    @Async
    public Future<List<JobListing>> scrapeJobs(SearchParams searchParams) {
        List<JobListing> jobListings = new ArrayList<>();
//        ScraperResult result = new ScraperResult();

        log.info("Starting scraping job with config {}", config.getJobSelectors());

        try {
            log.info("Starting job scraping for {}", config.getBoardName());

            // Build and navigate to search URL
            String searchUrl = buildSearchUrl(config.getBaseUrl(), config.getSearchProperties(), searchParams);
            seleniumHelper.goToPage(searchUrl);

            // Wait for page to load
            seleniumHelper.waitForElement(config.getJobListingsContainerElementLocator());

            // Scrape multiple pages
            int currentPage = 1;
//            int maxPages = config.getMaxPages() > 0 ? config.getMaxPages() : Integer.MAX_VALUE;
//
//            while (currentPage <= maxPages) {
//                log.info("Scraping page {} of {}", currentPage, config.getBoardName());

            // Find job listings container
            Optional<WebElement> container = seleniumHelper.findElement(null,
                    config.getJobListingsContainerElementLocator());



//                if (containers.isEmpty()) {
//                    log.warn("Job listings container not found");
//                    break;
//                }
            // Find all job items
            List<WebElement> jobItems = container.map(c -> seleniumHelper.findElements(c, config.getJobItemElementLocator()))
                    .orElseThrow(() -> new RuntimeException("no job listing container found"));

            log.info("Found {} job listings on page {}", jobItems.size(), currentPage);

            // Handle popup if enabled and configured
            PopupConfig popupConfig = config.getPopupConfig();
            if (popupConfig != null && popupConfig.isEnabled()) {
                try {
                    log.info("Handling popup {}", popupConfig.getPopupLocator());
                    // Wait for popup to appear
                    seleniumHelper.waitForElement(popupConfig.getPopupLocator());

                    log.info("Clicking popup close button {}", popupConfig.getCloseButtonLocator());

                    // Find and click close button
                    Optional<WebElement> closeButton = seleniumHelper.findElement(null, popupConfig.getCloseButtonLocator());
                    closeButton.ifPresent((b) -> {
                        log.info("Found popup close button {}", b);
                        seleniumHelper.clickElement(b);
                    });

                    log.info("Waiting for popup to close");

                    // Wait configured time after closing
                    if (popupConfig.getWaitTimeMs() > 0) {
                        Thread.sleep(popupConfig.getWaitTimeMs());
                    }
                } catch (Exception e) {
                    log.debug("Error handling popup: {}", e.getMessage());
                }
            }

            // Process each job listing
            for (WebElement jobItem : jobItems) {
                try {
                    String jobId = fieldExtractor.extractValue(jobItem, config.getJobSelectors().stream().filter(js -> js.field() == JobField.JOB_ID).findFirst().orElse(null).selector());
                    if(jobBoardScraperRepository.findByJobId(jobId) != null) {
                        continue;
                    }

                    seleniumHelper.clickElement(jobItem);
                    seleniumHelper.waitForElement(config.getJobDetailsElementLocator());

                    JobListing job = extractJobListing(jobItem);
                    job.setSearchParams(searchParams);
                    jobListings.add(job);
                } catch (Exception e) {
                    log.error("Error extracting job listing", e);
                }

                // Add delay to avoid rate limiting
                if (config.getRequestDelayMs() > 0) {
                    TimeUnit.MILLISECONDS.sleep(config.getRequestDelayMs());
                }
            }

            // Check if there's a next page and navigate to it
//                if (!navigateToNextPage(driver)) {
//                    log.info("No more pages to scrape");
//                    break;
//                }

//                currentPage++;
//            }

            log.info("Job scraping completed. Found {} job listings", jobListings.size());

            return CompletableFuture.completedFuture(jobListings);
        } catch (Exception e) {
            log.error("Error during job scraping", e);
            throw new ScraperException("Error during job scraping: " + e);
        } finally {
            // Always release the driver
            seleniumHelper.releaseDriver();
        }
    }

    /**
     * Extract job details from a job listing element
     */
    private JobListing extractJobListing(WebElement jobElement) {
        JobListing.JobListingBuilder builder = JobListing.builder();

        // Extract each configured field
        for (JobSelector jobSelector : config.getJobSelectors()) {
            log.info("Extracting value for field: {}", jobSelector.field());

            String value = fieldExtractor.extractValue(jobElement, jobSelector.selector());

            // Also set the standard field if it's one of the predefined ones
            builder = setStandardField(builder, jobSelector.field(), value);
        }

        JobListing job = builder.build();
        log.info("Extracted job: {}", job);
        jobBoardScraperRepository.save(job);

        return job;
    }

    /**
     * Set a standard field in the builder
     */
    private JobListing.JobListingBuilder setStandardField(
            JobListing.JobListingBuilder builder,
            JobField field,
            String value) {

        return switch (field) {
            case JOB_ID -> builder.jobId(value);
            case TITLE -> builder.title(value);
            case COMPANY -> builder.company(value);
            case LOCATION -> builder.location(value);
            case SALARY -> builder.salary(value);
            case DESCRIPTION -> builder.description(value);
            case DATE_POSTED -> builder.datePosted(value);
            case URL -> builder.url(value);
            case APPLY_URL -> builder.applyUrl(value);
            case JOB_TYPE -> builder.jobType(value);
            case EXPERIENCE_LEVEL -> builder.experienceLevel(value);
            default -> builder;
        };
    }

    /**
     * Navigate to the next page of results
     * @return true if navigation was successful, false if there are no more pages
     */
//    private boolean navigateToNextPage(RemoteWebDriver driver) {
//        try {
//            // Find the next button
//            List<WebElement> nextButtons = findElements(null,
//                    config.getNextPageSelector(),
//                    config.getNextPageSelectorType());
//
//            if (nextButtons.isEmpty() || !nextButtons.get(0).isEnabled() ||
//                    !nextButtons.get(0).isDisplayed()) {
//                return false;
//            }
//
//            // Click next page
//            nextButtons.get(0).click();
//
//            // Wait for the next page to load
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getPageLoadWaitTime()));
//            wait.until(ExpectedConditions.presenceOfElementLocated(
//                    getByLocator(config.getJobListingsContainerSelector(), config.getJobListingsContainerSelectorType())));
//
//            return true;
//        } catch (Exception e) {
//            log.error("Error navigating to next page", e);
//            return false;
//        }
//    }
}