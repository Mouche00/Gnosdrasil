package org.yc.gnosdrasil.gdpromptprocessingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;

@Repository
public interface NLPResultRepository extends JpaRepository<NLPResult, Long> {

} 