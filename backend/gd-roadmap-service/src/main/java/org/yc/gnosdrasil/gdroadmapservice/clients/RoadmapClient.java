package org.yc.gnosdrasil.gdroadmapservice.clients;

import org.yc.gnosdrasil.gdroadmapservice.dtos.RoadmapResponseDTO;

public interface RoadmapClient {
    RoadmapResponseDTO fetch(String language);
}
