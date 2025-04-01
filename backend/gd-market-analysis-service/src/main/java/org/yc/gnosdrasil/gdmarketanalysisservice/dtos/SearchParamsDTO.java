package org.yc.gnosdrasil.gdmarketanalysisservice.dtos;

import java.util.List;

public record SearchParamsDTO(List<String> keywords, String experienceLevel, String location, String date) {
}
