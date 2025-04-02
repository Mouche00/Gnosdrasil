package org.yc.gnosdrasil.gdexternalresourceservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdexternalresourceservice.clients.SerpApiClient;
import org.yc.gnosdrasil.gdexternalresourceservice.config.SerpApiConfig;
import org.yc.gnosdrasil.gdexternalresourceservice.dtos.request.StepRequestDTO;
import org.yc.gnosdrasil.gdexternalresourceservice.dtos.response.SerpApiResponseDTO;
import org.yc.gnosdrasil.gdexternalresourceservice.entities.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SerpApiService {

    private final SerpApiClient serpApiClient;
    private final SerpApiConfig serpApiConfig;

    public List<SerpApiResponseDTO> search(String query) {
        // Call SerpAPI using Feign client
        Map<String, Object> response = serpApiClient.searchWithYandex(serpApiConfig.getYandexEngine(), query, serpApiConfig.getApiKey());

        // Extract the first 5 results
        List<SerpApiResponseDTO> results = new ArrayList<>();
        if (response != null && response.containsKey("organic_results")) {
            List<Map<String, Object>> organicResults = (List<Map<String, Object>>) response.get("organic_results");
//            for (int i = 0; i < Math.min(5, organicResults.size()); i++) {
//                Map<String, Object> result = organicResults.get(i);
//                String title = (String) result.get("title");
//                String link = (String) result.get("link");
//                results.add("Title: " + title + ", Link: " + link);
//            }

            results = organicResults.stream().map(o -> new SerpApiResponseDTO((Integer) o.get("position"), (String) o.get("title"), (String) o.get("link"), (String) o.get("snippet"))).toList();
        }

        return results;
    }

    public List<Resource> getResources(StepRequestDTO stepRequestDTO) {
        log.info("StepRequestDTO: {}", stepRequestDTO);
        List<SerpApiResponseDTO> serpApiResponseDTOS = search(stepRequestDTO.label());

        return serpApiResponseDTOS.stream().map(s -> {
            try {
                Document document = Jsoup.connect(s.link())
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .get();

                return Resource.builder()
                        .title(s.title())
                        .link(s.link())
                        .snippet(s.snippet())
                        .pageSource(document.html())
                        .stepId(stepRequestDTO.id())
                        .build();
            } catch (IOException e) {
                // skip if error
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }
}