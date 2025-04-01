package org.yc.gnosdrasil.gdroadmapservice.clients.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.yc.gnosdrasil.gdroadmapservice.clients.RoadmapClient;
import org.yc.gnosdrasil.gdroadmapservice.dtos.RoadmapResponseDTO;

@Component
public class RoadmapClientImpl implements RoadmapClient {
    public RoadmapResponseDTO fetch(String language) {
        String url = String.format("https://roadmap.sh/%s.json", language);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, RoadmapResponseDTO.class);
    }
}
