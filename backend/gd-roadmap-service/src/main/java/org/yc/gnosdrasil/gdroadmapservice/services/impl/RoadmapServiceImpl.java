package org.yc.gnosdrasil.gdroadmapservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yc.gnosdrasil.gdroadmapservice.clients.RoadmapClient;
import org.yc.gnosdrasil.gdroadmapservice.dtos.RoadmapResponseDTO;
import org.yc.gnosdrasil.gdroadmapservice.entities.Roadmap;
import org.yc.gnosdrasil.gdroadmapservice.entities.Step;
import org.yc.gnosdrasil.gdroadmapservice.repositories.RoadmapRepository;
import org.yc.gnosdrasil.gdroadmapservice.repositories.StepRepository;
import org.yc.gnosdrasil.gdroadmapservice.services.RoadmapService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoadmapServiceImpl implements RoadmapService {
    private final RoadmapClient roadmapClient;
    private final RoadmapRepository roadmapRepository;
    private final StepRepository stepRepository;

    public RoadmapResponseDTO getRoadmap(String language) {
        return roadmapClient.fetch(language);
    }

    public List<Step> constructStepGraph(String language) {
        RoadmapResponseDTO roadmapDTO = getRoadmap(language);
        log.info("Constructing step graph for language {} with roadmap {}", language, roadmapDTO);
        List<Step> savedNodes = new ArrayList<>();

        // Create nodes from roadmapDTO
        List<Step> nodes = roadmapDTO.nodes().stream()
                .filter(n -> n.type().equals("topic") || n.type().equals("subtopic"))
                .map(node -> Step.builder().id(node.id()).label(node.data().label()).build())
                .toList();

        // Create edges from roadmapDTO
        List<RoadmapResponseDTO.Edge> edges = roadmapDTO.edges().stream()
                .filter(edge -> edge.source() != null && edge.target() != null)
                .toList();

        // Save nodes and establish connections
        for (Step node : nodes) {
            List<String> connectedStepIds = edges.stream()
                    .filter(edge -> edge.source().equals(node.getId()))
                    .map(RoadmapResponseDTO.Edge::target)
                    .toList();

            // Find connected steps
            List<Step> connectedSteps = connectedStepIds.stream()
                    .map(id -> nodes.stream().filter(node1 -> node1.getId().equals(id)).findFirst().orElse(null))
                    .filter(Objects::nonNull) // Filter out nulls
                    .toList();

            // Set connected steps
            node.setConnectedSteps(connectedSteps);

            // Save the node
            savedNodes.add(stepRepository.save(node));
        }

        Roadmap roadmap = Roadmap.builder().title(language).steps(savedNodes).build();

        // Optionally save the roadmap if needed
         roadmapRepository.save(roadmap);

        return stepRepository.findAll();
    }
}
