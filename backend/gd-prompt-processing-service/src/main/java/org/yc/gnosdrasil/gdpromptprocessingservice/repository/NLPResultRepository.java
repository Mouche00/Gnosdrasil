package org.yc.gnosdrasil.gdpromptprocessingservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.NLPResult;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NLPResultRepository extends JpaRepository<NLPResult, Long> {

    // Find by date range
    Page<NLPResult> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Find by sentiment
    List<NLPResult> findByOverallSentiment(String sentiment);

    // Find by programming language
    @Query("SELECT DISTINCT r FROM NLPResult r JOIN r.languageIntents l WHERE l.lang = :lang")
    List<NLPResult> findByProgrammingLanguage(@Param("lang") String lang);

    // Find by experience level
    @Query("SELECT DISTINCT r FROM NLPResult r JOIN r.languageIntents l WHERE l.level = :level")
    List<NLPResult> findByExperienceLevel(@Param("level") String level);

    // Find by language and level
    @Query("SELECT DISTINCT r FROM NLPResult r JOIN r.languageIntents l WHERE l.lang = :lang AND l.level = :level")
    List<NLPResult> findByLanguageAndLevel(@Param("lang") String lang, @Param("level") String level);

    // Count by sentiment
    @Query("SELECT r.overallSentiment, COUNT(r) FROM NLPResult r GROUP BY r.overallSentiment")
    List<Object[]> countBySentiment();

    // Count by programming language
    @Query("SELECT l.lang, COUNT(l) FROM LanguageIntent l GROUP BY l.lang")
    List<Object[]> countByProgrammingLanguage();

    // Count by experience level
    @Query("SELECT l.level, COUNT(l) FROM LanguageIntent l GROUP BY l.level")
    List<Object[]> countByExperienceLevel();
} 