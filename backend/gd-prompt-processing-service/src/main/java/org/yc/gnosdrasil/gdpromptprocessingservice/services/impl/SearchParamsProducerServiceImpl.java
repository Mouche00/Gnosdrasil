package org.yc.gnosdrasil.gdpromptprocessingservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.SearchParams;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchParamsProducerServiceImpl {

    private final KafkaTemplate<String, SearchParams> kafkaTemplate;

    @Value("${kafka.topic.search-params}")
    private String searchParamsTopic;

    public void sendSearchParams(SearchParams searchParams) {
        String requestId = UUID.randomUUID().toString();
        try {
            kafkaTemplate.send(searchParamsTopic, requestId, searchParams)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Successfully sent search params with requestId: {}", requestId);
                        } else {
                            log.error("Failed to send search params with requestId: {}", requestId, ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending search params to Kafka", e);
            throw new RuntimeException("Failed to send search params to Kafka", e);
        }
    }
} 