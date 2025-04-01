package org.yc.gnosdrasil.gdpromptprocessingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yc.gnosdrasil.gdpromptprocessingservice.entity.SearchParams;

public interface SearchParamsRepository extends JpaRepository<SearchParams, Long> {
}
