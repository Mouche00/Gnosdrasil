package org.yc.gnosdrasil.gdexternalresourceservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "serpApiClient", url = "https://serpapi.com")
public interface SerpApiClient {

    @GetMapping("/search.json")
    Map<String, Object> searchWithYandex(
            @RequestParam("engine") String engine,
            @RequestParam("text") String query,
            @RequestParam("api_key") String apiKey
    );
}