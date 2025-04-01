package org.yc.gnosdrasil.gdboardscraperservice.utils.helpers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import org.yc.gnosdrasil.gdboardscraperservice.dtos.SearchParamsDTO;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;
import org.yc.gnosdrasil.gdboardscraperservice.utils.records.SearchProperties;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
public class UrlHelper {

    /**
     * Build the search URL by replacing placeholders with search parameters
     */
    public static String buildSearchUrl(String baseUrl, SearchProperties searchProperties, SearchParamsDTO searchParamsDTO) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);

        Map<String, String> searchParamsMap = Map.of(
                searchProperties.keywordProperty(), String.join(" ", searchParamsDTO.keywords()),
                searchProperties.locationProperty(), searchParamsDTO.location()
//                searchProperties.experienceLevelProperty(), searchParams.getExperienceLevel(),
//                searchProperties.datePostedProperty(), searchParams.getDatePosted()
        );

        searchParamsMap.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().trim().isEmpty())
                .forEach(e -> uriBuilder.queryParam(e.getKey(), e.getValue()));

        return uriBuilder.build().encode().toUriString();
    }

    public String extractUrl(String linkedInUrl) {
        try {
            // Parse the URL to extract the query parameters
            java.net.URI uri = new java.net.URI(linkedInUrl);
            String query = uri.getQuery();

            // Split the query parameters
            String[] params = query.split("&");
            for (String param : params) {
                // Check if the parameter is the 'url' parameter
                if (param.startsWith("url=")) {
                    // Extract the URL and decode it
                    String encodedUrl = param.substring(4); // Remove 'url='
                    return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
                }
            }

            // Handle the case where the 'url' parameter is not found
            log.warn("URL parameter not found in the LinkedIn URL: {}", linkedInUrl);
        } catch (Exception e) {
            // Handle exceptions (e.g., log the error)
            log.error("Error extracting URL from LinkedIn URL: {}", linkedInUrl, e);
        }
        return null; // Return null or throw an exception if the URL is not found
    }


}
