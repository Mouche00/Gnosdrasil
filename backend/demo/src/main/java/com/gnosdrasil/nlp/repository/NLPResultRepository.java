package com.gnosdrasil.nlp.repository;

import com.gnosdrasil.nlp.entity.NLPResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NLPResultRepository extends JpaRepository<NLPResultEntity, Long> {
} 