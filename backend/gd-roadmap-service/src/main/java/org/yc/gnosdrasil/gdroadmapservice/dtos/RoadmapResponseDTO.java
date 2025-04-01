package org.yc.gnosdrasil.gdroadmapservice.dtos;

import java.util.List;

public record RoadmapResponseDTO(List<Node> nodes, List<Edge> edges) {
    public record Node(String id, String type, Data data) {
        public record Data(String label){

        }
    }

    public record Edge(String source, String target) {

    }
}
