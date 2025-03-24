package org.yc.gnosdrasil.gdboardscraperservice.scraper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.yc.gnosdrasil.gdboardscraperservice.config.common.JobBoardConfig;
import org.yc.gnosdrasil.gdboardscraperservice.entities.JobListing;
import org.yc.gnosdrasil.gdboardscraperservice.entities.ScraperResult;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;
import org.yc.gnosdrasil.gdboardscraperservice.factories.WebDriverFactory;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.FieldExtractor;
import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.SeleniumHelper;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.SearchProperties;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Generic job scraper that can work with any job board using a configuration
 */
@Slf4j
@RequiredArgsConstructor
public class JobScraperEngine {

//    private final WebDriverFactory webDriverFactory;
    private final SeleniumHelper seleniumHelper;
    private final FieldExtractor fieldExtractor;
    private final JobBoardConfig config;

    /**
     * Scrape job listings using the provided search parameters
     */
    public ScraperResult scrapeJobs(SearchParams searchParams) {
        List<JobListing> jobListings = new ArrayList<>();
//        ScraperResult result = new ScraperResult();

        try {
            log.info("Starting job scraping for {}", config.boardName());

            // Build and navigate to search URL
            String searchUrl = buildSearchUrl(searchParams);
            log.info("Navigating to search URL: {}", searchUrl);
            seleniumHelper.goToPage(searchUrl);

            // Wait for page to load

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getPageLoadWaitTime()));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    getByLocator(config.getJobListingsContainerSelector(), config.getJobListingsContainerSelectorType())));

            // Scrape multiple pages
            int currentPage = 1;
            int maxPages = config.getMaxPages() > 0 ? config.getMaxPages() : Integer.MAX_VALUE;

            while (currentPage <= maxPages) {
                log.info("Scraping page {} of {}", currentPage, config.getBoardName());

                // Find job listings container
                List<WebElement> containers = findElements(null,
                        config.getJobListingsContainerSelector(),
                        config.getJobListingsContainerSelectorType());

                if (containers.isEmpty()) {
                    log.warn("Job listings container not found");
                    break;
                }

                // Find all job items
                List<WebElement> jobItems = findElements(containers.get(0),
                        config.getJobItemSelector(),
                        config.getJobItemSelectorType());

                log.info("Found {} job listings on page {}", jobItems.size(), currentPage);

                // Process each job listing
                for (WebElement jobItem : jobItems) {
                    try {
                        JobListing job = extractJobListing(jobItem);
                        if (job != null) {
                            jobListings.add(job);
                        }
                    } catch (Exception e) {
                        log.error("Error extracting job listing", e);
                    }

                    // Add delay to avoid rate limiting
                    if (config.getRequestDelayMs() > 0) {
                        TimeUnit.MILLISECONDS.sleep(config.getRequestDelayMs());
                    }
                }

                // Check if there's a next page and navigate to it
                if (!navigateToNextPage(driver)) {
                    log.info("No more pages to scrape");
                    break;
                }

                currentPage++;
            }

            log.info("Job scraping completed. Found {} job listings", jobListings.size());

            result.setSuccess(true);
            result.setJobListings(jobListings);
            result.setTotalJobs(jobListings.size());
            result.setMessage("Successfully scraped " + jobListings.size() + " jobs from " + config.getBoardName());

        } catch (Exception e) {
            log.error("Error during job scraping", e);
            result.setSuccess(false);
            result.setMessage("Error during scraping: " + e.getMessage());
            result.setJobListings(jobListings); // Return any jobs that were successfully scraped
        } finally {
            // Always release the driver
            seleniumHelper.releaseDriver();
        }

        return result;
    }

    /**
     * Extract job details from a job listing element
     */
    private JobListing extractJobListing(WebElement jobElement) {
        JobListing.JobListingBuilder builder = JobListing.builder()
                .sourceBoardName(config.getBoardName());

        // Extract each configured field
        for (Map.Entry<JobBoardConfig.JobField, JobBoardConfig.FieldSelector> entry :
                config.getFieldSelectors().entrySet()) {

            JobBoardConfig.JobField field = entry.getKey();
            JobBoardConfig.FieldSelector selector = entry.getValue();

            String value = fieldExtractor.extractValue(jobElement, selector);
            builder.customFields(Map.of(field.name(), value));

            // Also set the standard field if it's one of the predefined ones
            builder = setStandardField(builder, field, value);
        }

        return builder.build();
    }

    /**
     * Set a standard field in the builder
     */
    private JobListing.JobListingBuilder setStandardField(
            JobListing.JobListingBuilder builder,
            JobBoardConfig.JobField field,
            String value) {

        switch (field) {
            case JOB_ID: return builder.id(value);
            case TITLE: return builder.title(value);
            case COMPANY: return builder.company(value);
            case LOCATION: return builder.location(value);
            case SALARY: return builder.salary(value);
            case DESCRIPTION: return builder.description(value);
            case DATE_POSTED: return builder.datePosted(value);
            case URL: return builder.url(value);
            case APPLY_URL: return builder.applyUrl(value);
            case JOB_TYPE: return builder.jobType(value);
            case EXPERIENCE_LEVEL: return builder.experienceLevel(value);
            default: return builder;
        }
    }

    /**
     * Build the search URL by replacing placeholders with search parameters
     */
    private String buildSearchUrl(SearchParams searchParams) {
        SearchProperties props = config.searchProperties();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(config.baseUrl());

        List<String> keywords = searchParams.getKeywords();
        if (keywords != null && !keywords.isEmpty()) {
            uriBuilder.queryParam(
                    props.keywordProperty(),
                    String.join(" ", keywords)  // Let URI builder handle encoding
            );
        }

        addParamIfPresent(uriBuilder, props.locationProperty(), searchParams.getLocation());
        addParamIfPresent(uriBuilder, props.experienceLevelProperty(), searchParams.getExperienceLevel());
        addParamIfPresent(uriBuilder, props.datePostedProperty(), searchParams.getDatePosted());

        return uriBuilder.build().encode().toUriString();
    }

    // Helper method to avoid null/empty parameters
    private void addParamIfPresent(UriComponentsBuilder builder, String paramName, String paramValue) {
        if (paramValue != null && !paramValue.trim().isEmpty()) {
            builder.queryParam(paramName, paramValue);
        }
    }

    /**
     * Navigate to the next page of results
     * @return true if navigation was successful, false if there are no more pages
     */
    private boolean navigateToNextPage(RemoteWebDriver driver) {
        try {
            // Find the next button
            List<WebElement> nextButtons = findElements(null,
                    config.getNextPageSelector(),
                    config.getNextPageSelectorType());

            if (nextButtons.isEmpty() || !nextButtons.get(0).isEnabled() ||
                    !nextButtons.get(0).isDisplayed()) {
                return false;
            }

            // Click next page
            nextButtons.get(0).click();

            // Wait for the next page to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getPageLoadWaitTime()));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    getByLocator(config.getJobListingsContainerSelector(), config.getJobListingsContainerSelectorType())));

            return true;
        } catch (Exception e) {
            log.error("Error navigating to next page", e);
            return false;
        }
    }

    /**
     * Find elements using the specified selector and selector type
     */
    private List<WebElement> findElements(WebElement parentElement, String selector, JobBoardConfig.SelectorType selectorType) {
        switch (selectorType) {
            case CSS:
                return seleniumHelper.findElementsByCssSelector(parentElement, selector);
            case XPATH:
                return seleniumHelper.findElementsByXPath(parentElement, selector);
            case CLASS_NAME:
                return seleniumHelper.findElementsByClassName(parentElement, selector);
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Get a By locator for the specified selector and selector type
     */
    private org.openqa.selenium.By getByLocator(String selector, JobBoardConfig.SelectorType selectorType) {
        switch (selectorType) {
            case CSS:
                return org.openqa.selenium.By.cssSelector(selector);
            case XPATH:
                return org.openqa.selenium.By.xpath(selector);
            case CLASS_NAME:
                return org.openqa.selenium.By.className(selector);
            default:
                throw new IllegalArgumentException("Unsupported selector type: " + selectorType);
        }
    }
}