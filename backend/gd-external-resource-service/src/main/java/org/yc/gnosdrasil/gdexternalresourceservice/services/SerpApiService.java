package org.yc.gnosdrasil.gdexternalresourceservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdexternalresourceservice.clients.SerpApiClient;
import org.yc.gnosdrasil.gdexternalresourceservice.config.SerpApiConfig;
import org.yc.gnosdrasil.gdexternalresourceservice.dtos.response.SerpApiResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
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
}