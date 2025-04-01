package org.yc.gnosdrasil.gdroadmapservice.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import org.yc.gnosdrasil.gdroadmapservice.entities.Roadmap;
import org.yc.gnosdrasil.gdroadmapservice.entities.Step;

@Repository
public interface StepRepository extends Neo4jRepository<Step, String> {
}
