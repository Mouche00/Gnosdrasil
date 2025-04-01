package org.yc.gnosdrasil.gdroadmapservice.services;

import org.yc.gnosdrasil.gdroadmapservice.entities.Roadmap;
import org.yc.gnosdrasil.gdroadmapservice.entities.Step;

import java.util.List;

public interface RoadmapService {
    List<Step> constructStepGraph(String language);
}
