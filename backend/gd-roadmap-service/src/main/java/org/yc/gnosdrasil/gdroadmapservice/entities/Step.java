package org.yc.gnosdrasil.gdroadmapservice.entities;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Node
public class Step {
    @Id
    private String id;
    private String label;

    @Relationship(type = "CONNECTED_TO", direction = Relationship.Direction.OUTGOING)
    private List<Step> connectedSteps = new ArrayList<>();

//    @Relationship(type = "OF", direction = Relationship.Direction.OUTGOING)
//    private Roadmap roadmap;
}
