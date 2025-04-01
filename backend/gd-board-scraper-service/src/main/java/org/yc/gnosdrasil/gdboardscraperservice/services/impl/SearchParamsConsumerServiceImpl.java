package org.yc.gnosdrasil.gdboardscraperservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdboardscraperservice.entities.SearchParams;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchParamsConsumerServiceImpl {

    @KafkaListener(
            topics = "${kafka.topic.search-params}",
            groupId = "${spring.application.name}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeSearchParams(SearchParams searchParams) {
        try {
            log.info("Received search params with requestId");
            // TODO: Implement the board scraping logic here
            processSearchParams(searchParams);
        } catch (Exception e) {
            log.error("Error processing search params with requestId", e);
            throw new RuntimeException("Failed to process search params", e);
        }
    }

    private void processSearchParams(SearchParams searchParams) {
        // Implement the actual board scraping logic here
        log.info("Processing search params: {}", searchParams);
    }
} 