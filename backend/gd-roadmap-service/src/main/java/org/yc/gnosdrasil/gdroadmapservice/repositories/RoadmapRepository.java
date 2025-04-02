package org.yc.gnosdrasil.gdroadmapservice.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import org.yc.gnosdrasil.gdroadmapservice.entities.Roadmap;

import java.util.Optional;

@Repository
public interface RoadmapRepository extends Neo4jRepository<Roadmap, String> {

    Optional<Roadmap> findByTitle(String title);
}
