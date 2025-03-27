//package org.yc.gnosdrasil.gdboardscraperservice.services.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.openqa.selenium.WebElement;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.yc.gnosdrasil.gdboardscraperservice.config.linkedin.LinkedinSelectorConfig;
//import org.yc.gnosdrasil.gdboardscraperservice.entities.JobListing;
//import org.yc.gnosdrasil.gdboardscraperservice.entities.ScraperResult;
//import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;
//import org.yc.gnosdrasil.gdboardscraperservice.services.LinkedInScraperService;
//import org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.SeleniumHelper;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static org.yc.gnosdrasil.gdboardscraperservice.utils.enums.ScraperJobStatus.*;
//import static org.yc.gnosdrasil.gdboardscraperservice.utils.helpers.StringHelper.getLastSubString;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@EnableConfigurationProperties(LinkedinSelectorConfig.class)
//public class LinkedInScraperServiceIml implements LinkedInScraperService {
//
//    @Value("${scraper.delay.ms:2000}")
//    private long delayBetweenRequests;
//
//    @Value("${scraper.max.pages:10}")
//    private int maxPagesToScrape;
//
//    @Value("${urn.separator::}")
//    private String urnSeparator;
//
////    private final RemoteWebDriver driver;
//    private final SeleniumHelper seleniumHelper;
//    private final LinkedinSelectorConfig selectors;
//
//    /**
//     * Navigate to LinkedIn search page with the given search parameters
//     */
//    public void navigateToSearchPage(SearchParams searchParams) {
//        try {
//            // Build search URL
//            String searchUrl = buildSearchUrl(searchParams);
//
//            // Navigate to the URL
//            seleniumHelper.goToPage(searchUrl);
//
//            // Wait for the page to load
//            seleniumHelper.waitUntilElementLoadsByClassName("jobs-search__results-list");
//
//            log.info("Successfully navigated to LinkedIn search page");
//        } catch (Exception e) {
//            log.error("Error navigating to LinkedIn search page", e);
//            throw new RuntimeException("Failed to navigate to LinkedIn search page", e);
//        }
//    }
//
//    public List<JobListing> extractJobListingsFromPage() throws InterruptedException {
//        List<JobListing> jobListings = new ArrayList<>();
//
//        // Find all job elements on the page
//        List<WebElement> jobElements = seleniumHelper.findElementsByCssSelector(null,".jobs-search__results-list > li");
//        log.info("Found {} job elements on current page", jobElements.size());
//
//        int i = 0;
//        // Extract details for each job
//        for (WebElement jobElement : jobElements) {
//            if(i >= 3) break;
//            try {
//                JobListing job = extractJobDetails(jobElement);
//                if (job != null) {
//                    jobListings.add(job);
//                }
//            } catch (Exception e) {
//                log.error("Error extracting job details", e);
//            }
//
//            // Add delay to avoid rate limiting
//            TimeUnit.MILLISECONDS.sleep(delayBetweenRequests);
//
//            i++;
//        }
//
//        return jobListings;
//    }
//
//    /**
//     * Start a new scraping job asynchronously
//     */
//    @Async
//    public CompletableFuture<String> startScrapingJob(SearchParams searchParams) {
//        // Create a new job
//        ScraperResult job = ScraperResult.builder()
//                .status(RUNNING)
//                .progress(0)
//                .searchParams(searchParams)
//                .build();
////        job.addLog("Starting LinkedIn job scraper...");
//
//        // Save the job
////        scraperJobRepository.save(job);
//
//        // Run the scraper in a separate thread
//        CompletableFuture.runAsync(() -> {
//            try {
//                List<JobListing> results = scrapeJobs(searchParams, job);
//
//                // Update job status
//                job.setStatus(COMPLETED);
//                job.setJobsFound(results.size());
////                job.addLog("Scraping completed. Found " + results.size() + " job listings.");
////                job.setEndTime(LocalDateTime.now());
//
//            } catch (Exception e) {
//                log.error("Error during LinkedIn job scraping", e);
//                job.setStatus(FAILED);
////                job.addLog("Error during scraping: " + e.getMessage());
////                job.setEndTime(LocalDateTime.now());
//            } finally {
////                scraperJobRepository.save(job);
//            }
//        });
//
//        return CompletableFuture.completedFuture(job.getId());
//    }
//
//    /**
//     * Main scraping method
//     */
//    private List<JobListing> scrapeJobs(SearchParams searchParams, ScraperResult job) {
//        List<JobListing> jobListings = new ArrayList<>();
//
//        try {
//            log.info("Starting LinkedIn job scraper with keywords: {}, location: {}",
//                    searchParams.getKeywords(), searchParams.getLocation());
////            job.addLog("Configuring WebDriver...");
//
//
//            // Navigate to LinkedIn jobs page
////            job.addLog("Navigating to LinkedIn search page...");
//            navigateToSearchPage(searchParams);
//            job.setProgress(10);
//
//            // Extract job listings from current page
//            jobListings = extractJobListingsFromPage();
//
//            // Scrape multiple pages
////            int currentPage = 1;
////            while (currentPage <= maxPagesToScrape) {
////                String logMessage = "Scraping page " + currentPage + " of LinkedIn job results";
////                log.info(logMessage);
////                job.addLog(logMessage);
////
////                // Update progress (distribute progress from 10% to 90% across pages)
////                int progressPerPage = 80 / maxPagesToScrape;
////                job.setProgress(10 + (currentPage - 1) * progressPerPage);
////
////                // Extract job listings from current page
////                List<JobListing> pageResults = extractorService.extractJobListingsFromPage(driver, delayBetweenRequests);
////
////                // Save results
////                for (JobListing listing : pageResults) {
////                    jobListings.add(listing);
////                    jobListingRepository.save(listing);
////                }
////
////                job.addLog("Found " + pageResults.size() + " jobs on page " + currentPage);
////
////                // Check if there's a next page and navigate to it
////                boolean hasNextPage = navigationService.navigateToNextPage(driver, currentPage);
////                if (!hasNextPage) {
////                    job.addLog("No more pages to scrape");
////                    break;
////                }
////
////                currentPage++;
////            }
////
////            String completionMessage = "LinkedIn job scraping completed. Found " + jobListings.size() + " job listings";
////            log.info(completionMessage);
////            job.addLog(completionMessage);
//
//        } catch (Exception e) {
//            log.error("Error during LinkedIn job scraping", e);
////            job.addLog("Error: " + e.getMessage());
//            throw new RuntimeException("Failed to scrape LinkedIn jobs", e);
//        } finally {
//            // Close the WebDriver
//            seleniumHelper.releaseDriver();
//        }
//
//        return jobListings;
//    }
//
//    /**
//     * Extract details for a single job listing
//     */
//    private JobListing extractJobDetails(WebElement jobElement) {
////        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//
//        try {
//            // Click on the job to view details
////            jobElement.click();
//
//            // Wait for job details to load
////            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".details-pane__content")));
//
//            // Extract job information
//            String jobUrn = seleniumHelper.getAttributeByClassName(jobElement, selectors.jobUrnSelector(),"data-entity-urn");
//            String title = seleniumHelper.getTextByClassName(jobElement, selectors.titleSelector());
//            String company = seleniumHelper.getTextByClassName(jobElement, selectors.companySelector());
//            String location = seleniumHelper.getTextByClassName(jobElement, selectors.locationSelector());
////            String salary = getTextSafely(".jobs-unified-top-card__salary-details");
////            String description = getTextSafely(".jobs-description-content__text");
//            String datePosted = seleniumHelper.getAttributeByClassName(jobElement,selectors.datePostedSelector(),"datetime");
////            String url = driver.getCurrentUrl();
//
//            JobListing job = JobListing.builder()
//                    .jobId(getLastSubString(jobUrn, urnSeparator))
//                    .title(title)
//                    .company(company)
//                    .location(location)
//                    .datePosted(LocalDate.parse(datePosted))
//                    .build();
//
//            log.info("Extracted job details: {}", job.toString());
//
//            return job;
//
//        } catch (Exception e) {
//            log.error("Error extracting job details", e);
//            return null;
//        }
//    }
//
//    /**
//     * Navigate to the next page of search results
//     * @return true if navigation was successful, false if there are no more pages
//     */
////    public boolean navigateToNextPage(int currentPage) {
////        try {
////            // Find the next button
////            List<WebElement> nextButtons = driver.findElements(By.cssSelector("button[aria-label='Next']"));
////            if (nextButtons.isEmpty() || !nextButtons.get(0).isEnabled()) {
////                log.info("No more pages to scrape");
////                return false;
////            }
////
////            // Get a reference to the current first job element to check for page change
////            List<WebElement> jobElements = driver.findElements(By.cssSelector(".jobs-search__results-list > li"));
////            if (jobElements.isEmpty()) {
////                log.warn("No job elements found on current page");
////                return false;
////            }
////            WebElement firstJobElement = jobElements.get(0);
////
////            // Click next page
////            nextButtons.get(0).click();
////
////            // Wait for the next page to load
////            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
////            wait.until(ExpectedConditions.stalenessOf(firstJobElement));
////            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".jobs-search__results-list")));
////
////            log.info("Successfully navigated to page {}", currentPage + 1);
////            return true;
////        } catch (Exception e) {
////            log.error("Error navigating to next page", e);
////            return false;
////        }
////    }
//
//    /**
//     * Build the LinkedIn search URL from search parameters
//     */
//    private String buildSearchUrl(SearchParams searchParams) {
//        return Stream.of(
//                        param("keywords", searchParams.getKeywords(), "%20"),
//                        param("location", searchParams.getLocation(), "%20"),
//                        param("f_TPR", searchParams.getDatePosted(), "")
//                )
//                .filter(Objects::nonNull) // Filter out null parameters
//                .collect(Collectors.joining("&", "https://www.linkedin.com/jobs/search/?", ""));
//    }
//
//    private String param(String name, Object value, String separator) {
//        if (value == null || (value instanceof String && ((String) value).isEmpty())) {
//            return null; // Skip null or empty values
//        }
//        if (value instanceof List) {
//            return name + "=" + String.join(separator, (List<String>) value);
//        }
//        return name + "=" + value;
//    }
//}
