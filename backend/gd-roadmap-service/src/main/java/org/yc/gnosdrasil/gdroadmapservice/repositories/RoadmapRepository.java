package org.yc.gnosdrasil.gdroadmapservice.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import org.yc.gnosdrasil.gdroadmapservice.entities.Roadmap;

@Repository
public interface RoadmapRepository extends Neo4jRepository<Roadmap, String> {
}
