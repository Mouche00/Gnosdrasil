package org.yc.gnosdrasil.gdroadmapservice.entities;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Node
public class Roadmap {
    @Id
    public String title;

    @Relationship(type = "OF", direction = Relationship.Direction.INCOMING)
    public List<Step> steps;
}
