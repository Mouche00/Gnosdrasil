package org.yc.gnosdrasil.gdpromptprocessingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;

import java.util.Optional;

public interface NLPResultRepository extends JpaRepository<NLPResult, Long> {
    @Query("SELECT n FROM NLPResult n WHERE n.correctedText = ?1")
    Optional<NLPResult> findByCorrectedText(String correctedText);
}